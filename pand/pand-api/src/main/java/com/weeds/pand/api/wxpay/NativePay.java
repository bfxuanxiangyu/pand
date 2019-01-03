package com.weeds.pand.api.wxpay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weeds.pand.utils.PandStringUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/10/13.
 */
public class NativePay {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 生成扫描支付模式一URL
     *
     * @param productId 商品ID
     * @return 模式一URL
     */
    public String getPrePayUrl(String productId) {
        logger.info("Native pay mode 1 url is producing...");

        WxPayData data = new WxPayData();
        data.setValue("appid", WxPayConfig.APPID);//公众帐号id
        data.setValue("mch_id", WxPayConfig.MCHID);//商户号
        data.setValue("time_stamp", WxPayApi.generateTimeStamp());//时间戳
        data.setValue("nonce_str", WxPayApi.generateNonceStr());//随机字符串
        data.setValue("product_id", productId);//商品ID
        data.setValue("sign", data.MakeSign());//签名
        String str = ToUrlParams(data.getValues());//转换为URL串
        String url = "weixin://wxpay/bizpayurl?" + str;

        logger.info("Get native pay mode 1 url : " + url);
        return url;
    }
    
    /**
     * 获取二维码的url
     *
     * @param orderNo
     * @param totalFee   总金额 单位：分
     * @param ip
     * @param notifyUrl
     * @return
     */
    public String getPayUrl(String orderNo, int totalFee, String ip, String notifyUrl) {
    	logger.info("Native pay mode 2 url is producing...");
    	
    	WxPayData data = new WxPayData();
    	data.setValue("body", "会费");//商品描述
//        data.setValue("attach", "");//附加数据
    	data.setValue("out_trade_no", orderNo);//
    	data.setValue("total_fee", totalFee);//总金额
    	data.setValue("spbill_create_ip", ip);//用户ip
//        data.setValue("time_start", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));//交易起始时间
//        data.setValue("time_expire", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));//交易结束时间
//        data.setValue("goods_tag", "jjj");//商品标记
    	data.setValue("trade_type", "NATIVE");//交易类型
    	data.setValue("product_id", "0000001");//商品ID
    	data.setValue("spbill_create_ip", ip);
    	data.setValue("notify_url", notifyUrl);
    	WxPayData result = WxPayApi.unifiedOrder(data, 30);//调用统一下单接口
    	if(PandStringUtils.isNotBlank((String)result.getValue("code_url"))){
    		String url = result.getValue("code_url").toString();//获得统一下单接口返回的二维码链接
    		
    		logger.info("Get native pay mode 2 url : " + url);
    		return url;
    	}else{
    		String errDes = result.getValue("err_code_des")+"";
    		if(PandStringUtils.isNotBlank(errDes) && errDes.equals("该订单已支付")){
    			return "orderSuccess";
    		}
    		return null;
    	}
    }

    /**
     * 获取h5微信支付package
     *
     * @param orderNo
     * @param totalFee   总金额 单位：分
     * @param ip
     * @param notifyUrl
     * @return
     */
    public String getH5PayPackage(String orderNo, int totalFee, String ip, String notifyUrl,String openId) {
        logger.info("JSAPI pay mode 2 url is producing...");

    	WxPayData data = new WxPayData();
    	data.setValue("body", "交易费");//商品描述
    	data.setValue("out_trade_no", orderNo);//
    	data.setValue("total_fee", totalFee);//总金额
    	data.setValue("spbill_create_ip", ip);//用户ip
        data.setValue("openid", openId);//支付者微信公开号
    	data.setValue("trade_type", "JSAPI");//交易类型
    	data.setValue("product_id", "0000001");//商品ID
    	data.setValue("spbill_create_ip", ip);
    	data.setValue("notify_url", notifyUrl);
    	WxPayData result = WxPayApi.unifiedOrder(data, 30);//调用统一下单接口
    	if(PandStringUtils.isNotBlank((String)result.getValue("prepay_id"))){
    		String prepayId = result.getValue("prepay_id").toString();//微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
    		logger.info("JSAPI pay Get native pay mode 2 prepay_id="+prepayId);
    		return prepayId;
    	}else{
    		String errDes = result.getValue("err_code_des")+"";
    		if(PandStringUtils.isNotBlank(errDes) && errDes.equals("该订单已支付")){
    			return "orderSuccess";
    		}
    		return null;
    	}
    }

    /**
     * 参数数组转换为url格式
     *
     * @param map 参数名与参数值的映射表
     * @return URL字符串
     */
    private String ToUrlParams(TreeMap<String, Object> map) {
        String buff = "";
        for (Map.Entry<String, Object> pair : map.entrySet()) {
            buff += pair.getKey() + "=" + pair.getValue() + "&";
        }
        buff = buff.substring(0, buff.length() - 1);
        return buff;
    }
}