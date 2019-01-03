package com.weeds.pand.api.system.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.weeds.pand.api.utils.BeanUtils;
import com.weeds.pand.api.wxpay.H5Pay;
import com.weeds.pand.api.wxpay.NativePay;
import com.weeds.pand.api.wxpay.WxOpenId;
import com.weeds.pand.api.wxpay.WxPayData;
import com.weeds.pand.service.pandcore.domain.PandOrder;
import com.weeds.pand.service.pandcore.service.PandOrderService;
import com.weeds.pand.service.pay.domain.Wxpay;
import com.weeds.pand.service.pay.service.WxpayService;
import com.weeds.pand.utils.HttpUtils;
import com.weeds.pand.utils.PandResponseUtil;
import com.weeds.pand.utils.PandStringUtils;

@Controller
@RequestMapping("/api/wxpay")
public class WxPayController {

	private Logger logger = LoggerFactory.getLogger(WxPayController.class);
	
	@Resource
	private WxpayService wxpayService;
	@Resource
	private PandOrderService pandOrderService;
	
	@Value("${wx.pay.weixin_notify_url}")
	private String wxNotifyUrl;
	@Value("${wx.pay.weixin_ip}")
	private String wxWeixinIp;
	@Value("${wx.pay.jsappid}")
	private String wxJsappid;
	/**
	 * 微信回调方法
	 * @param outTradeNo
	 * @param response
	 * @param registrationId
	 * @throws IOException
	 */
	@RequestMapping (value = "notifyUrl/{outTradeNo}")
	public void notify(@PathVariable("outTradeNo")String outTradeNo,HttpServletRequest request, HttpServletResponse response)  throws IOException {
		
		String returnSuccess = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		try {
            InputStream inputStream = request.getInputStream();
            String xml = IOUtils.toString(inputStream, request.getCharacterEncoding());
            logger.info("订单号="+outTradeNo+"开始回调:" + xml);
            WxPayData wxPayData = new WxPayData();
            TreeMap<String, Object> map = wxPayData.fromXml(xml);
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("outTradeNo", outTradeNo);
            Wxpay wxpay = wxpayService.getWxpayObject(params);
            if (PandStringUtils.isNotBlank(wxpay.getReturnCode())) {
            	response.getWriter().println(returnSuccess);
				return;
            }
            BeanUtils.mapToBean(map, wxpay);
            wxpay.setUpdateTime(new Date());
            wxpayService.saveOrUpdateWxpay(wxpay);
            
            //修改报名信息状态
            String registrationId = wxpay.getRegistrationId();
            
            if(PandStringUtils.isNotBlank(registrationId)){//修改订单状态   修改成已支付
            	PandOrder obj = pandOrderService.getPandOrderById(registrationId);
            	
            	if(obj != null){
            		obj.setStatus(0);
            		pandOrderService.savePandOrder(obj);
            		logger.info("微信支付成功,更改订单状态,id="+registrationId);
            	}
            }
            
            response.getWriter().println(returnSuccess);
        } catch (Exception e) {
            logger.error("", e);
            response.getWriter().println("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[" + e.getMessage() + "]]></return_msg></xml>");
        }
		
	}
	
	
	
	/**
	 * 支付----微信js支付
	 * @param response
	 * @param registrationId
	 * @throws IOException
	 */
	@RequestMapping (value = "toPayByJs.json")
	public void toPayByJs(HttpServletResponse response,String orderId,String code,String token) {
		
		logger.info("微信支付参数：orderId="+orderId+",code="+code+",token="+token);
		try {
			
			if(PandStringUtils.isBlank(orderId) || PandStringUtils.isBlank(code) || PandStringUtils.isBlank(token)){
				PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS, "缺少参数",null);
				return;
			}
			logger.info("订单id="+orderId+"开始支付,code="+code);
			
			String openId = getPayOpenId(code);//oYmYxxMWnpv71ZUmmHJfq-oDnJ6o
			
			if(PandStringUtils.isBlank(openId) || openId.equals("fail")){
				PandResponseUtil.printFailJson(PandResponseUtil.no_pay_wx, "无权限支付",null);
				return;
			}
			
			PandOrder obj = pandOrderService.getPandOrderById(orderId);
        	
        	if(obj == null){
        		PandResponseUtil.printFailJson(PandResponseUtil.no_order, "订单不存",null);
				return;
        	}
			
			if(obj.getStatus() !=null && obj.getStatus()==0){
				PandResponseUtil.printFailJson(PandResponseUtil.ALREADYADD, "已支付",null);
				return;
			}
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("registrationId", orderId);
			Wxpay wxpay = wxpayService.getWxpayObject(params);
			boolean isNew = false;
			if (wxpay == null) {
				isNew = true;
			} else if (PandStringUtils.isNotBlank(wxpay.getReturnCode())) {
				isNew = true;
			} else if (PandStringUtils.isNotBlank(wxpay.getMyTotalFee()) 
					&& Double.valueOf(wxpay.getMyTotalFee()).compareTo(obj.getServiceMoney()) != 0) {
				isNew = true;
			} else {
				long createTime = wxpay.getCreateTime().getTime();
				long now = System.currentTimeMillis();
				//判断是否已经超过两小时了
				if (now - createTime >= 1.5 * 60 * 60 * 1000) {
					isNew = true;
				} else {
					isNew = false;
				}
			}
			String codeUrl = null;
			if (!isNew) {
				codeUrl = wxpay.getCodeUrl();
			} else {
				String orderNo = wxpayService.generateOutTradeNo();
				double price = obj.getServiceMoney();
				int totalFee = (int) (price * 100);
				NativePay nativePay = new NativePay();
				String finalNotifyUrl = wxNotifyUrl + "/" + orderNo;
				codeUrl = nativePay.getH5PayPackage(orderNo, totalFee, wxWeixinIp, finalNotifyUrl,openId);
				if(PandStringUtils.isBlank(codeUrl)){
					PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"支付失败", null);
					return ;
				}
				if(codeUrl.equals("orderSuccess")){
					logger.info("订单"+orderNo+"已支付");
					PandResponseUtil.printFailJson(PandResponseUtil.ALREADYADD,"订单已支付", null);
					return;
				}
				Wxpay saveWxpay = new Wxpay();
				saveWxpay.setMyTotalFee(obj.getServiceMoney()+"");
				saveWxpay.setOutTradeNo(orderNo);
				saveWxpay.setCodeUrl(codeUrl);
				saveWxpay.setCreateTime(new Date());
				saveWxpay.setDeleteFlag(0);
				saveWxpay.setRegistrationId(orderId);
				wxpayService.saveOrUpdateWxpay(saveWxpay);
			}
			
			if(PandStringUtils.isBlank(codeUrl)){
				PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"支付失败", null);
				return ;
			}
			String timeStamp = String.valueOf(System.currentTimeMillis()).toString().substring(0,10);
			String nonceStr = PandStringUtils.getRandomStr(32);
			String packageStr = "prepay_id="+codeUrl;
			String signType ="MD5";
			String paySign = "";
			/*String stringA="appId="+appId+"&nonceStr="+nonceStr+"&package="+packageStr+"&signType="+signType
					+"&timeStamp="+timeStamp+"&key="+WxPayConfig.KEY;
			paySign = StringUtil.getMD5(stringA).toUpperCase();*/
			H5Pay h5pay = new H5Pay();
			
			h5pay.setValue("appId", wxJsappid);
			h5pay.setValue("nonceStr", nonceStr);
			h5pay.setValue("package", packageStr);
			h5pay.setValue("signType", signType);
			h5pay.setValue("timeStamp", timeStamp);
			
			paySign = h5pay.MakeSign();
			logger.info("微信支付原始参数 make sign md5="+paySign);
			
			h5pay.setNonceStr(nonceStr);
			h5pay.setPackagep(packageStr);
			h5pay.setPaySign(paySign);
			h5pay.setSignType(signType);
			h5pay.setTimeStamp(timeStamp);
			
			PandResponseUtil.printJson("data", h5pay);
			
		} catch (Exception e) {
			PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务升级", null);
		}
		
	}
	
	//js支付   获取openId（静默状态下）
		private String getPayOpenId(String code)  throws IOException {
			String openId = "";
			try {
				String openUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx91e9350b8ea11c36&secret=9f7ec24b9014d8924fb7d433288c998a&grant_type=authorization_code&code="+code;
				String returnStr = HttpUtils.doGet(openUrl);
				logger.info("微信支付返回openId结果="+returnStr);
				
				WxOpenId openObject = JSON.parseObject(returnStr, WxOpenId.class);
				if(openObject != null){
					openId = openObject.getOpenid();
				}
				if(PandStringUtils.isBlank(openId)){
					openId = "fail";
				}
				logger.info("微信支付获取当前openId="+openId);
			} catch (Exception e) {
				logger.error("微信支付获取opendId异常"+e.getMessage(),e);
			}
			return openId;
		}
}
