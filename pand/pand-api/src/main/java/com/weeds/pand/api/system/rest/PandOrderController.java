package com.weeds.pand.api.system.rest;

import static com.weeds.pand.utils.PandStringUtils.isBlank;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.weeds.pand.service.pandcore.domain.PandOrder;
import com.weeds.pand.service.pandcore.domain.PandShop;
import com.weeds.pand.service.pandcore.domain.PandUserAddress;
import com.weeds.pand.service.pandcore.service.PandOrderService;
import com.weeds.pand.service.pandcore.service.PandUserAddressService;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandResponseUtil;
import com.weeds.pand.utils.PandStringUtils;

/**
 * @author user
 * pand 服务控制类
 *
 */
@Controller
@RequestMapping("/api/pandorder")
public class PandOrderController {

	private Logger logger = LoggerFactory.getLogger(PandOrderController.class);
	
	@Resource
	private PandOrderService pandOrderService;
	@Resource
	private PandUserAddressService pandUserAddressService;
	
	/**
	 * 用户下单
	 * token        用户token
	 * pandUserId   用户id 必传
	 * serviceId    发布服务的id 必传
	 * address      需要服务的地址 必传
	 * timePeriod   需要服务的时间段 必传
	 * serviceCount 需要服务的数量 必传
	 * serviceMoney 需要服务的价格 必传
	 * message      服务留言 选填
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/to_buy")
	public String toBuy(String token,PandOrder order) {
		logger.info("下单参数:"+token+",order:"+order.toString());
		if(isBlank(token) || isBlank(order.getPandUserId()) || isBlank(order.getServiceId()) || isBlank(order.getAddress())
				|| isBlank(order.getTimePeriod()) || order.getServiceCount()== null || order.getServiceMoney()== null){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try{
			//获取shop的用户id和shopid
			PandShop shop = pandOrderService.getPandShopObjByServiceId(order.getServiceId());
			
			if(shop==null){
				logger.info("无法获取该服务的店铺数据 serviceId="+order.getServiceId());
				return PandResponseUtil.printFailJson(PandResponseUtil.no_shop,"店铺不存在", null);
			}
			order.setShopId(shop.getId());
			order.setPandShoperId(shop.getPandUserId());
			order.setOrderNum(getOrderNum());//生成订单号
			order.setCreateTime(new Date());
			order.setStatus(0);
			pandOrderService.savePandOrder(order);
			
			return PandResponseUtil.printJson("下单成功",order);
		} catch (Exception e) {
			logger.error("下单异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	/**
	 * 修改订单    支持取消订单，删除订单，商户接单，完工后确认订单(关闭订单)
	 * token     用户token
	 * orderId   订单id
	 * status    订单状态  1取消订单  2删除订单  3商户接单  4确认完工关闭订单
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/order_update")
	public String toBuy(String token,String orderId,Integer status) {
		logger.info("修改订单参数:"+token+",status:"+status);
		if(isBlank(token) || status== null){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try{
			PandOrder dbOrder = pandOrderService.getPandOrderById(orderId);
			if(dbOrder==null){
				logger.info("订单不存在 无法修改 orderId="+orderId);
				return PandResponseUtil.printFailJson(PandResponseUtil.no_order,"订单不存在", null);
			}
			dbOrder.setStatus(status);
			
			pandOrderService.savePandOrder(dbOrder);
			
			return PandResponseUtil.printJson("成功",null);
		} catch (Exception e) {
			logger.error("下单异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	/**
	 * 订单列表  支持查询普通用户订单列表   商户查询自己所属列表
	 * @param token      用户token 必填
	 * @param pageIndex     当前页 必填
	 * @param pageSize      每页数量  默认为10 必填
	 * @param pandUserId 普通用户id 选填
	 * @param shoperId   商家用户店铺id 选填
	 * @param status     订单状态   0已下单  1已取消订单 2已删除订单 3已接单  4已完成订单 选填
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/order_list")
	public String orderList(String token,String pandUserId,String shoperId,Integer status,
			Integer pageIndex, Integer pageSize) {
		logger.info("订单列表参数:"+token+",pandUserId:"+pandUserId+",shoperId:"+shoperId+",status:"+status
				+",pageIndex:"+pageIndex+",pageSize:"+pageSize);
		if(isBlank(token) || pageIndex==null || pageSize==null){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try{
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("pandUserId", pandUserId);
			parameters.put("shoperId", shoperId);
			parameters.put("status", status);
			
			//分页
			if(pageIndex<=1){
				pageIndex = 0;
			}else{
				pageIndex = pageIndex-1;
			}
			parameters.put("begin", pageIndex * pageSize);
			parameters.put("end", pageSize);
			
			List<PandOrder> orderList = pandOrderService.getPandOrderList(parameters);
			
			return PandResponseUtil.printJson("订单列表获取成功",orderList);
		} catch (Exception e) {
			logger.error("订单列表获取异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	/**
	 * 地址列表
	 * @param token      用户token 必填
	 * @param pandUserId 普通用户id 选填
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/address_list")
	public String addressList(String token,String pandUserId) {
		logger.info("订单列表参数:"+token+",pandUserId:"+pandUserId);
		if(isBlank(token) || isBlank(pandUserId)){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try{
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("pandUserId", pandUserId);
			parameters.put("status", 0);
			List<PandUserAddress> list = pandUserAddressService.getPandUserAddressList(parameters);
			
			return PandResponseUtil.printJson("地址列表获取成功",list);
		} catch (Exception e) {
			logger.error("地址列表获取异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	/**
	 * 新增/编辑地址
	 * token        用户token   必填
	 * pandUserId   用户id   必填
	 * personAddress地址   必填
	 * addressLat   地址纬度   必填
	 * addressLng   地址经度   必填
	 * personName   联系人姓名   选填
	 * personPhone  联系人手机   选填
	 * houseNumber  门牌号   选填
	 * id           地址id   选填
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/address_save")
	public String toBuy(String token,PandUserAddress address) {
		logger.info("修改地址参数:"+token+",addressObj:"+address.toString());
		if(isBlank(token) || isBlank(address.getPandUserId()) || isBlank(address.getAddressLat()) 
				|| isBlank(address.getAddressLng()) || isBlank(address.getPersonAddress())){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try{
			address.setStatus(0);
			if(isBlank(address.getId())){
				address.setCreateTime(new Date());
			}else{
				PandUserAddress pua = pandUserAddressService.getPandUserAddressById(address.getId());
				if(pua != null){
					address.setCreateTime(pua.getCreateTime());
				}
			}
			
			pandUserAddressService.savePandUserAddress(address);
			
			return PandResponseUtil.printJson("编辑成功",address);
		} catch (Exception e) {
			logger.error("地址异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	/**
	 * 删除地址
	 * token        用户token   必填
	 * id           地址id   必填
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/address_delete")
	public String toBuy(String token,String id) {
		logger.info("删除地址参数:"+token+",id:"+id);
		if(isBlank(token) || isBlank(id)){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try{
			PandUserAddress pua = pandUserAddressService.getPandUserAddressById(id);
			if(pua == null){
				return PandResponseUtil.printFailJson(PandResponseUtil.no_address,"地址不存在", null);
			}
			pua.setStatus(1);
			pandUserAddressService.savePandUserAddress(pua);
			
			return PandResponseUtil.printJson("删除成功",null);
		} catch (Exception e) {
			logger.error("删除地址异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	private String getOrderNum(){
		String orderStr = PandStringUtils.getUUID();
		
		try {
			String dateStr = PandDateUtils.dateToStr(new Date(), "yyyy-MM-dd");
			Long orderNum = pandOrderService.getMaxPandOrder(dateStr);
			
			if(orderNum==null){
				orderStr = PandDateUtils.dateToStr(new Date(), "yyyyMMddHHmmss")+"0001";
			}else{
				orderStr = (orderNum+1)+"";
			}
		} catch (Exception e) {
			logger.error("订单编号获取异常"+e.getMessage(),e);
		}
		return orderStr;
	}
	
}
