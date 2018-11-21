package com.weeds.pand.api.system.rest;

import static com.weeds.pand.utils.PandStringUtils.isBlank;
import static com.weeds.pand.utils.PandStringUtils.isNotBlank;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.weeds.pand.service.mechanic.domain.PandUser;
import com.weeds.pand.service.mechanic.service.PandUserService;
import com.weeds.pand.service.pandcore.domain.PandService;
import com.weeds.pand.service.pandcore.domain.PandShop;
import com.weeds.pand.service.pandcore.service.PandServiceService;
import com.weeds.pand.service.pandcore.service.PandShopService;
import com.weeds.pand.service.system.domain.CardImage;
import com.weeds.pand.service.system.service.PandImagesService;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandResponseUtil;

/**
 * @author user
 * pand 服务控制类
 *
 */
@Controller
@RequestMapping("/api/pandwork")
public class PandServiceController {

	private Logger logger = LoggerFactory.getLogger(PandServiceController.class);
	
	@Resource
	private PandUserService pandUserService;
	@Resource
	private PandImagesService pandImagesService;
	@Resource
	private PandServiceService pandServiceService;
	@Resource
	private PandShopService pandShopService; 
	
	@Value("${pand.service.status : 2}")
	private Integer serviceStatus;
	
	@Value("${img.savePath}")
	private String savePath;
	@Value("${img.imgUrl}")
	private String imgUrl;
	
	/**
	 * 发布服务 需要token 图片、发布者id、服务技能id、标题、描述、须知、所属城市、所属区域、价格、区间、是否开具发票
	 * @param token          登录token
	 * @param imagesJson     服务照片baseStr是图片base64位后字符串  {[{"baseStr": "..."},{"baseStr": "..."},{"baseStr": "..."}]}
	 * @param pandUserId     用户id
	 * @param serviceTypeId  技能id
	 * @param serviceTilte   服务标题
	 * @param serviceDes     服务描述
	 * @param serviceInfo    服务须知
	 * @param serviceAddress 服务所在地址
	 * @param serviceLat     服务地址所在纬度
	 * @param serviceLng     服务地址所在经度
	 * @param servicePrice   服务价格
	 * @param serviceScope   服务区间
	 * @param serviceInvoice 是否开具发票
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pand_issue")
	public String enterShopper(String token,PandService ps,String imagesJson) {
		logger.info("发布服务参数:"+token+",对象:"+ps.toString()+",图片:"+imagesJson);
		if(isBlank(token) || isBlank(ps.getPandUserId()) || isBlank(imagesJson)){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try {
			if(isBlank(ps.getPandUserId()) || isBlank(ps.getServiceTypeId()) 
				|| isBlank(ps.getServiceTilte()) || isBlank(ps.getServiceDes())
				|| isBlank(ps.getServiceAddress()) || isBlank(ps.getServicePrice())
				|| isBlank(ps.getServiceScope()) || ps.getServiceInvoice()==null){
				return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
			}
			
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("id", ps.getPandUserId());
			PandUser oldObjById = pandUserService.getPandUserObj(parameters);
			if(oldObjById == null){
				return PandResponseUtil.printFailJson(PandResponseUtil.PHONENO,"用户不存在", null);
			}
			//检测该用户下是否发布过改标题的服务
			parameters.clear();
			parameters.put("pandUserId", ps.getPandUserId());
			parameters.put("serviceTilte", ps.getServiceTilte());
			List<PandService> serviceList = pandServiceService.getPandServiceList(parameters);
			if(serviceList!=null && !serviceList.isEmpty()){
				return PandResponseUtil.printFailJson(PandResponseUtil.issue_repeat,"不能重复发布", null);
			}
			
			//发布服务
			ps.setServiceStatus(serviceStatus);//是否自动审核   用户提交状态
			if(isBlank(ps.getId())){
				ps.setCreateTime(new Date());
			}else{
				PandService psOld = pandServiceService.getPandServiceById(ps.getId());
				if(psOld != null){
					ps.setCreateTime(psOld.getCreateTime());
				}
			}
			
			pandServiceService.savePandService(ps);
			
			if(isBlank(ps.getId())){
				return PandResponseUtil.printFailJson(PandResponseUtil.issue_error,"发布异常", null);
			}
			try {
				List<CardImage> ciList = JSON.parseArray(imagesJson, CardImage.class);
				if(ciList!=null && !ciList.isEmpty()){
					//首先删除改类型下的图片
					Map<String, Object> dep = Maps.newConcurrentMap();
					dep.put("imgModel", 3);
					dep.put("modelId", ps.getId());//根据服务id进行删除原有图片
					pandImagesService.deletePandImagesObj(dep);
					if(ciList!=null && !ciList.isEmpty()){
						List<String> baseStrList = null;
						for (CardImage ci : ciList) {
							if(isBlank(ci.getBaseStr())){
								continue;
							}
							baseStrList = Lists.newArrayList();
							baseStrList.add(ci.getBaseStr());
						}
						pandImagesService.savePandImages(0,3, ps.getId(), baseStrList);
					}
				}
			} catch (Exception e) {
				logger.error("服务照片上传异常"+e.getMessage(),e);
			}
			
			return PandResponseUtil.printJson("发布成功", ps);
		} catch (Exception e) {
			logger.error("发布服务异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
		
	}
	
	/**
	 * 服务下架    快速上架  删除
	 * @param token   用户token
	 * @param status  下架、快速上架状态值  4下架  2快速上架 5删除
	 * @param id      服务id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/service_sold_out")
	public String enterShopper(String token,Integer status,String id) {
		logger.info("服务上下架参数:"+token+",status:"+status+",id:"+id);
		if(isBlank(token) || isBlank(id) || status== null){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try{
			if(status!=4 && status !=2 && status != 5){
				return PandResponseUtil.printFailJson(PandResponseUtil.sold_out_error,"上下架状态异常", null);
			}
			String info = "下架成功";
			if(status==2){
				info = "快速上架成功";
			}if(status==5){
				info = "删除成功";
			}
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("id", id);
			PandService oldPs = pandServiceService.getPandServiceObj(parameters);
			if(oldPs == null){
				return PandResponseUtil.printFailJson(PandResponseUtil.sold_out_error,"上下架异常", null);
			}
			oldPs.setServiceStatus(status);
			pandServiceService.savePandService(oldPs);
			return PandResponseUtil.printJson(info, null);
		} catch (Exception e) {
			logger.error("服务上下架异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	/**
	 * 修改服务价格
	 * @param token         用户token
	 * @param servicePrice  服务价格
	 * @param id            服务id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/service_update_price")
	public String serviceUpdatePrice(String token,String servicePrice,String id) {
		logger.info("快速修改服务价格参数:"+token+",servicePrice:"+servicePrice+",id:"+id);
		if(isBlank(token) || isBlank(id) || isBlank(servicePrice)){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try{
			
			PandService pand = pandServiceService.getPandServiceById(id);
			if(pand == null){
				return PandResponseUtil.printFailJson(PandResponseUtil.no_service,"服务不存在", null);
			}
			pand.setServicePrice(servicePrice);
			pandServiceService.savePandService(pand);
			return PandResponseUtil.printJson("修改成功", null);
		} catch (Exception e) {
			logger.error("快速修改服务价格异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	/**
	 * 服务详情
	 * @param id            服务id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/service_detail")
	public String serviceUpdatePrice(String id) {
		logger.info("服务详情参数id:"+id);
		if( isBlank(id) ){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try{
			
			PandService pand = pandServiceService.getPandServiceById(id);
			if(pand == null){
				return PandResponseUtil.printFailJson(PandResponseUtil.no_service,"服务不存在", null);
			}
			return PandResponseUtil.printJson("获取成功", pand);
		} catch (Exception e) {
			logger.error("服务详情异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	/**
	 * 获取店铺详情
	 * @param token      用户token
	 * @param pandUserId 用户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/shop_detail")
	public String shopDetail(String pandUserId) {
		logger.info("店铺详情参数:pandUserId:"+pandUserId);
		if(isBlank(pandUserId)){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try {
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("pandUserId", pandUserId);
			PandShop oldPs = pandShopService.getPandShopObject(parameters);
			if(oldPs==null){
				return PandResponseUtil.printFailJson(PandResponseUtil.no_shop,"店铺不存在", null);
			}
			return PandResponseUtil.printJson("店铺信息获取成功", oldPs);
		} catch (Exception e) {
			logger.error("店铺获取异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	/**
	 * 编辑店铺    可以更新店铺头像、名称、描述、联系方式、服务时间段
	 * @param token        用户token
	 * @param pandUserId   用户id
	 * @param id           店铺id
	 * @param shopDes      店铺描述
	 * @param shopName     店铺名称
	 * @param shopTel      店铺联系方式
	 * @param shopTime     店铺服务时间段
	 * @param shopImg      店铺图片
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/edit_shop")
	public String editShop(String token,PandShop ps) {
		logger.info("编辑店铺参数:"+token+",ps:"+ps.toString());
		if(isBlank(token) || isBlank(ps.getPandUserId()) || isBlank(ps.getId())){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try {
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("id", ps.getId());
			PandShop oldPs = pandShopService.getPandShopObject(parameters);
			if(oldPs==null){
				return PandResponseUtil.printFailJson(PandResponseUtil.no_shop,"店铺不存在", null);
			}
			//编辑店铺头像
			if(isNotBlank(ps.getShopImg())){
				String httpStr = uploadShopImg(ps.getShopImg());
				oldPs.setShopImg(httpStr);
			}
			
			//更新店铺信息
			oldPs.setShopDes(ps.getShopDes());
			oldPs.setShopName(ps.getShopName());
			oldPs.setShopTel(ps.getShopTel());
			oldPs.setShopTime(ps.getShopTime());
			
			pandShopService.savePandShop(oldPs);
			return PandResponseUtil.printJson("店铺编辑成功", oldPs);
		} catch (Exception e) {
			logger.error("编辑店铺异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	/**
	 * 上传头像
	 * @param baseStr
	 * @return
	 */
	private String uploadShopImg(String baseStr){
		String httpStr = null;
		try {
			String porderPath = "shopImg/"+PandDateUtils.dateToStr(new Date(), "yyyyMMdd")+"/";
			File file = new File(savePath+porderPath);
			if(!file.exists()){
				file.mkdirs();
			}
			String fileName = PandDateUtils.dateToStr(new Date(), "yyyyMMddHHmmss")+".png";
			byte[] bytes = Base64Utils.decode(baseStr.getBytes());
			OutputStream out = new FileOutputStream(savePath+porderPath+fileName);
			out.write(bytes);
			out.flush();
			out.close();
			
			httpStr = imgUrl+porderPath+fileName;
		} catch (Exception e) {
			logger.error("图片保存异常"+e.getMessage(),e);
		}
		return httpStr;
	}
	
	/**
	 * 服务列表   支持综合、销量、上门速度、高级筛选选择，支持分页  请仔细阅读以下参数
	 * @param pageIndex     当前页 必填
	 * @param pageSize      每页数量  默认为10 必填
	 * @param token         用户token  必填
	 * @param serviceTypeId 服务种类  技能列表中的各自id 选填
	 * @param pandUserId    用户id  选填
	 * @param searchType    查询类型  1综合查询 2销量  3上门速度 4筛选 选填
	 * @param sortType      综合筛选  1综合 2评分最高 3距离最近 4价格降序 5价格升序 选填
	 * @param lat           当前纬度 (选择综合查询距离最近时lat lng必传) 选填
	 * @param lng           当前经度 选填
	 * @param contents      关键字 选填
	 * @param startPrice    价格区间 起始价 选填
	 * @param endPrice      价格区间 最高价 选填
	 * @param serviceInvoice是否开具发票 0不开具   1开具 选填
	 * @param orderType     是否下过单  0未下过   1下过 选填
	 * @param shopType		商家类型 1新商家  2企业商家  选填
	 * @param serviceStatus	商品状态 2出售中  4 已下架
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pand_service_list")
	public String pandServiceList(String pandUserId,String serviceTypeId,Integer searchType,
								  Integer sortType,String lat,String lng,String contents,Integer startPrice,
								  Integer endPrice,Integer serviceInvoice,Integer orderType,Integer shopType,
								  Integer serviceStatus, Integer pageIndex, Integer pageSize) {
		logger.info("服务列表参数 pandUserId="+pandUserId+",serviceTypeId:"+serviceTypeId+",searchType:"+searchType
				+",sortType:"+sortType+",lat:"+lat+",lng:"+lng+",contents:"+contents
				+",startPrice:"+startPrice+",endPrice:"+endPrice+",serviceInvoice:"+serviceInvoice
				+",orderType:"+orderType+",shopType:"+shopType+",pageIndex:"+pageIndex+",pageSize:"+pageSize);
		if(pageIndex==null || pageSize==null){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try {
			Map<String, Object> parameters = getBaseMap(pandUserId, serviceTypeId, contents, startPrice, endPrice, serviceInvoice, pageIndex, pageSize);
			//排序
			if(sortType==null){
			}else if(sortType==4 || sortType==5){//价格升降序
				parameters.put("sortType", sortType);
			}else if(sortType==2){//评分最高
				
			}else if(sortType==3){//距离最近
				if(isBlank(lat) || isBlank(lng)){
					return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少当前经纬度参数", null);
				}
				Double serviceLat = Double.valueOf(lat);
				Double serviceLng = Double.valueOf(lng);
				parameters.put("distanceSelect", "select");
				parameters.put("serviceLat", serviceLat);
				parameters.put("serviceLng", serviceLng);
				parameters.put("sortType", 3);
				
			}
			
			if(serviceStatus != null){
				parameters.put("serviceStatus", serviceStatus);
			}
			
			List<PandService> serviceList = pandServiceService.getPandServiceList(parameters);
			return PandResponseUtil.printJson("服务列表获取成功", serviceList);
		} catch (Exception e) {
			logger.error("服务列表获取异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	/**
	 * 获取基础map参数
	 * @return
	 */
	private Map<String, Object> getBaseMap(String pandUserId,String serviceTypeId,String contents,Integer startPrice,
			  Integer endPrice,Integer serviceInvoice,
			  Integer pageIndex, Integer pageSize){
		Map<String, Object> parameters = Maps.newHashMap();
		if(pageIndex<=1){
			pageIndex = 0;
		}else{
			pageIndex = pageIndex-1;
		}
		parameters.put("begin", pageIndex * pageSize);
		parameters.put("end", pageSize);
		
		parameters.put("serviceTypeId", serviceTypeId);//服务种类
		//价格区间查询
		if(startPrice != null){	parameters.put("startPrice", startPrice);}
		if(endPrice != null){	parameters.put("endPrice", endPrice);}
		//是否开具发票
		if(serviceInvoice != null){	parameters.put("serviceInvoice", serviceInvoice);}
		//关键字查询
		if(isNotBlank(contents)){	parameters.put("contents", contents);}
		//指定某人查询
		if(isNotBlank(pandUserId)){	parameters.put("pandUserId", pandUserId);}
		
		parameters.put("sortType", 0);//默认排序
		return parameters;
	}
	
}
