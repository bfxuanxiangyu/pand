package com.weeds.pand.api.wxpay;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2016/10/13.
 */
public class WxPayApi {
	
    private static Logger logger = Logger.getLogger(WxPayApi.class);
    /**
     * 提交被扫支付API
     * 收银员使用扫码设备读取微信用户刷卡授权码以后，二维码或条码信息传送至商户收银台，
     * 由商户收银台或者商户后台调用该接口发起支付。
     *
     * @param inputObj 提交给被扫支付API的参数
     * @param timeOut  超时时间
     * @return 成功时返回调用结果，其他抛异常
     * @throws
     */
    public static WxPayData micropay(WxPayData inputObj, int timeOut) {
        String url = "https://api.mch.weixin.qq.com/pay/micropay";
        //检测必填参数
        if (!inputObj.isSet("body")) {
            throw new RuntimeException("提交被扫支付API接口中，缺少必填参数body！");
        } else if (!inputObj.isSet("out_trade_no")) {
            throw new RuntimeException("提交被扫支付API接口中，缺少必填参数out_trade_no！");
        } else if (!inputObj.isSet("total_fee")) {
            throw new RuntimeException("提交被扫支付API接口中，缺少必填参数total_fee！");
        } else if (!inputObj.isSet("auth_code")) {
            throw new RuntimeException("提交被扫支付API接口中，缺少必填参数auth_code！");
        }

        inputObj.setValue("spbill_create_ip", WxPayConfig.IP);//终端ip
        inputObj.setValue("appid", WxPayConfig.APPID);//公众账号ID
        inputObj.setValue("mch_id", WxPayConfig.MCHID);//商户号
        inputObj.setValue("nonce_str", UUID.randomUUID().toString().replace("-", ""));//随机字符串
        inputObj.setValue("sign", inputObj.MakeSign());//签名
        String xml = inputObj.toXml();

        long start = System.currentTimeMillis();//请求开始时间

        logger.info("WxPayApi-MicroPay request : " + xml);
        String response = HttpService.post(xml, url, false, timeOut);//调用HTTP通信接口以提交数据到API
        logger.info("WxPayApi-MicroPay response : " + response);

        long end = System.currentTimeMillis();
        int timeCost = (int) (end - start);//获得接口耗时

        //将xml格式的结果转换为对象以返回
        WxPayData result = new WxPayData();
        result.fromXml(response);

        reportCostTime(url, timeCost, result);//测速上报

        return result;
    }


    /**
     * 查询订单
     *
     * @param inputObj 提交给查询订单API的参数
     * @param timeOut  超时时间
     * @return 成功时返回订单查询结果，其他抛异常
     * @throws
     */
    public static WxPayData orderQuery(WxPayData inputObj, int timeOut) {
        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        //检测必填参数
        if (!inputObj.isSet("out_trade_no") && !inputObj.isSet("transaction_id")) {
            throw new RuntimeException("订单查询接口中，out_trade_no、transaction_id至少填一个！");
        }

        inputObj.setValue("appid", WxPayConfig.APPID);//公众账号ID
        inputObj.setValue("mch_id", WxPayConfig.MCHID);//商户号
        inputObj.setValue("nonce_str", WxPayApi.generateNonceStr());//随机字符串
        inputObj.setValue("sign", inputObj.MakeSign());//签名

        String xml = inputObj.toXml();

        long start = System.currentTimeMillis();

        logger.info("WxPayApi-OrderQuery request : " + xml);
        String response = HttpService.post(xml, url, false, timeOut);//调用HTTP通信接口提交数据
        logger.info("WxPayApi-OrderQuery response : " + response);

        long end = System.currentTimeMillis();
        int timeCost = (int) (end - start);//获得接口耗时

        //将xml格式的数据转化为对象以返回
        WxPayData result = new WxPayData();
        result.fromXml(response);

        reportCostTime(url, timeCost, result);//测速上报

        return result;
    }


    /**
     * 撤销订单API接口
     *
     * @param inputObj 提交给撤销订单API接口的参数，out_trade_no和transaction_id必填一个
     * @param timeOut  接口超时时间
     * @return 成功时返回API调用结果，其他抛异常
     * @throws RuntimeException
     */
    public static WxPayData reverse(WxPayData inputObj, int timeOut) {
        String url = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
        //检测必填参数
        if (!inputObj.isSet("out_trade_no") && !inputObj.isSet("transaction_id")) {
            throw new RuntimeException("撤销订单API接口中，参数out_trade_no和transaction_id必须填写一个！");
        }

        inputObj.setValue("appid", WxPayConfig.APPID);//公众账号ID
        inputObj.setValue("mch_id", WxPayConfig.MCHID);//商户号
        inputObj.setValue("nonce_str", generateNonceStr());//随机字符串
        inputObj.setValue("sign", inputObj.MakeSign());//签名
        String xml = inputObj.toXml();

        long start = System.currentTimeMillis();//请求开始时间

        logger.info("WxPayApi-Reverse request : " + xml);

        String response = HttpService.post(xml, url, true, timeOut);

        logger.info("WxPayApi-Reverse response : " + response);

        long end = System.currentTimeMillis();
        int timeCost = (int) (end - start);

        WxPayData result = new WxPayData();
        result.fromXml(response);

        reportCostTime(url, timeCost, result);//测速上报

        return result;
    }


    /**
     * 申请退款
     *
     * @param inputObj 提交给申请退款API的参数
     * @param timeOut  超时时间
     * @return 成功时返回接口调用结果，其他抛异常
     * @throws RuntimeException
     */
    public static WxPayData refund(WxPayData inputObj, int timeOut) {
        String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
        //检测必填参数
        if (!inputObj.isSet("out_trade_no") && !inputObj.isSet("transaction_id")) {
            throw new RuntimeException("退款申请接口中，out_trade_no、transaction_id至少填一个！");
        } else if (!inputObj.isSet("out_refund_no")) {
            throw new RuntimeException("退款申请接口中，缺少必填参数out_refund_no！");
        } else if (!inputObj.isSet("total_fee")) {
            throw new RuntimeException("退款申请接口中，缺少必填参数total_fee！");
        } else if (!inputObj.isSet("refund_fee")) {
            throw new RuntimeException("退款申请接口中，缺少必填参数refund_fee！");
        } else if (!inputObj.isSet("op_user_id")) {
            throw new RuntimeException("退款申请接口中，缺少必填参数op_user_id！");
        }

        inputObj.setValue("appid", WxPayConfig.APPID);//公众账号ID
        inputObj.setValue("mch_id", WxPayConfig.MCHID);//商户号
        inputObj.setValue("nonce_str", UUID.randomUUID().toString().replace("-", ""));//随机字符串
        inputObj.setValue("sign", inputObj.MakeSign());//签名

        String xml = inputObj.toXml();
        long start = System.currentTimeMillis();

        logger.info("WxPayApi-Refund request : " + xml);
        String response = HttpService.post(xml, url, true, timeOut);//调用HTTP通信接口提交数据到API
        logger.info("WxPayApi-Refund response : " + response);

        long end = System.currentTimeMillis();
        int timeCost = (int) (end - start);//获得接口耗时

        //将xml格式的结果转换为对象以返回
        WxPayData result = new WxPayData();
        result.fromXml(response);

        reportCostTime(url, timeCost, result);//测速上报

        return result;
    }


    /**
     * 查询退款
     * 提交退款申请后，通过该接口查询退款状态。退款有一定延时，
     * 用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。
     * out_refund_no、out_trade_no、transaction_id、refund_id四个参数必填一个
     *
     * @param inputObj 提交给查询退款API的参数
     * @param timeOut  接口超时时间
     * @return 成功时返回，其他抛异常
     * @throws RuntimeException
     */
    public static WxPayData refundQuery(WxPayData inputObj, int timeOut) {
        String url = "https://api.mch.weixin.qq.com/pay/refundquery";
        //检测必填参数
        if (!inputObj.isSet("out_refund_no") && !inputObj.isSet("out_trade_no") &&
                !inputObj.isSet("transaction_id") && !inputObj.isSet("refund_id")) {
            throw new RuntimeException("退款查询接口中，out_refund_no、out_trade_no、transaction_id、refund_id四个参数必填一个！");
        }

        inputObj.setValue("appid", WxPayConfig.APPID);//公众账号ID
        inputObj.setValue("mch_id", WxPayConfig.MCHID);//商户号
        inputObj.setValue("nonce_str", generateNonceStr());//随机字符串
        inputObj.setValue("sign", inputObj.MakeSign());//签名

        String xml = inputObj.toXml();

        long start = System.currentTimeMillis();//请求开始时间

        logger.info("WxPayApi-RefundQuery request : " + xml);
        String response = HttpService.post(xml, url, false, timeOut);//调用HTTP通信接口以提交数据到API
        logger.info("WxPayApi-RefundQuery response : " + response);

        long end = System.currentTimeMillis();
        int timeCost = (int) (end - start);//获得接口耗时

        //将xml格式的结果转换为对象以返回
        WxPayData result = new WxPayData();
        result.fromXml(response);

        reportCostTime(url, timeCost, result);//测速上报

        return result;
    }


    /**
     * 下载对账单
     *
     * @param inputObj 提交给下载对账单API的参数
     * @param timeOut  接口超时时间
     * @return 成功时返回，其他抛异常
     * @throws RuntimeException
     */
    public static WxPayData downloadBill(WxPayData inputObj, int timeOut) {
        String url = "https://api.mch.weixin.qq.com/pay/downloadbill";
        //检测必填参数
        if (!inputObj.isSet("bill_date")) {
            throw new RuntimeException("对账单接口中，缺少必填参数bill_date！");
        }

        inputObj.setValue("appid", WxPayConfig.APPID);//公众账号ID
        inputObj.setValue("mch_id", WxPayConfig.MCHID);//商户号
        inputObj.setValue("nonce_str", generateNonceStr());//随机字符串
        inputObj.setValue("sign", inputObj.MakeSign());//签名

        String xml = inputObj.toXml();

        logger.info("WxPayApi-DownloadBill request : " + xml);
        String response = HttpService.post(xml, url, false, timeOut);//调用HTTP通信接口以提交数据到API
        logger.info("WxPayApi-DownloadBill result : " + response);

        WxPayData result = new WxPayData();
        //若接口调用失败会返回xml格式的结果
        if (response.substring(0, 5) == "<xml>") {
            result.fromXml(response);
        }
        //接口调用成功则返回非xml格式的数据
        else
            result.setValue("result", response);

        return result;
    }


    /**
     * 转换短链接
     * 该接口主要用于扫码原生支付模式一中的二维码链接转成短链接(weixin://wxpay/s/XXXXXX)，
     * 减小二维码数据量，提升扫描速度和精确度。
     *
     * @param inputObj 提交给转换短连接API的参数
     * @param timeOut  接口超时时间
     * @return 成功时返回，其他抛异常
     * @throws RuntimeException
     */
    public static WxPayData shortUrl(WxPayData inputObj, int timeOut) {
        String url = "https://api.mch.weixin.qq.com/tools/shorturl";
        //检测必填参数
        if (!inputObj.isSet("long_url")) {
            throw new RuntimeException("需要转换的URL，签名用原串，传输需URL encode！");
        }

        inputObj.setValue("appid", WxPayConfig.APPID);//公众账号ID
        inputObj.setValue("mch_id", WxPayConfig.MCHID);//商户号
        inputObj.setValue("nonce_str", generateNonceStr());//随机字符串
        inputObj.setValue("sign", inputObj.MakeSign());//签名
        String xml = inputObj.toXml();

        long start = System.currentTimeMillis();//请求开始时间

        logger.info("WxPayApi-ShortUrl request : " + xml);
        String response = HttpService.post(xml, url, false, timeOut);
        logger.info("WxPayApi-ShortUrl response : " + response);

        long end = System.currentTimeMillis();
        int timeCost = (int) (end - start);

        WxPayData result = new WxPayData();
        result.fromXml(response);
        reportCostTime(url, timeCost, result);//测速上报

        return result;
    }


    /**
     * 统一下单
     *
     * @param inputObj 提交给统一下单API的参数
     * @param timeOut  超时时间
     * @return 成功时返回，其他抛异常
     * @throws RuntimeException
     */
    public static WxPayData unifiedOrder(WxPayData inputObj, int timeOut) {
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        //检测必填参数
        if (!inputObj.isSet("out_trade_no")) {
            throw new RuntimeException("缺少统一支付接口必填参数out_trade_no！");
        } else if (!inputObj.isSet("body")) {
            throw new RuntimeException("缺少统一支付接口必填参数body！");
        } else if (!inputObj.isSet("total_fee")) {
            throw new RuntimeException("缺少统一支付接口必填参数total_fee！");
        } else if (!inputObj.isSet("trade_type")) {
            throw new RuntimeException("缺少统一支付接口必填参数trade_type！");
        }

        //关联参数
        if (inputObj.getValue("trade_type").toString() == "JSAPI" && !inputObj.isSet("openid")) {
            throw new RuntimeException("统一支付接口中，缺少必填参数openid！trade_type为JSAPI时，openid为必填参数！");
        }
        /*if (inputObj.getValue("trade_type").toString() == "NATIVE" && !inputObj.isSet("product_id")) {
            throw new RuntimeException("统一支付接口中，缺少必填参数product_id！trade_type为JSAPI时，product_id为必填参数！");
        }*/

        //异步通知url未设置，则使用配置文件中的url
        if (!inputObj.isSet("notify_url")) {
            inputObj.setValue("notify_url", WxPayConfig.NOTIFY_URL);//异步通知url
        }

        inputObj.setValue("appid", WxPayConfig.APPID);//公众账号ID
        inputObj.setValue("mch_id", WxPayConfig.MCHID);//商户号
//        inputObj.setValue("spbill_create_ip", inputObj.getValue("ip"));//终端ip
        inputObj.setValue("nonce_str", generateNonceStr());//随机字符串

        //签名
        inputObj.setValue("sign", inputObj.MakeSign());
        String xml = inputObj.toXml();

        long start = System.currentTimeMillis();

        logger.info("WxPayApi-UnfiedOrder request : " + xml);
        String response = HttpService.post(xml, url, false, timeOut);
        logger.info("WxPayApi-UnfiedOrder response : " + response);

        long end = System.currentTimeMillis();
        int timeCost = (int) (end - start);

        WxPayData result = new WxPayData();
        result.fromXml(response);

//        reportCostTime(url, timeCost, result);//测速上报

        return result;
    }


    /**
     * 关闭订单
     *
     * @param inputObj 提交给关闭订单API的参数
     * @param timeOut  接口超时时间
     * @return 成功时返回，其他抛异常
     * @throws RuntimeException
     */
    public static WxPayData closeOrder(WxPayData inputObj, int timeOut) {
        String url = "https://api.mch.weixin.qq.com/pay/closeorder";
        //检测必填参数
        if (!inputObj.isSet("out_trade_no")) {
            throw new RuntimeException("关闭订单接口中，out_trade_no必填！");
        }

        inputObj.setValue("appid", WxPayConfig.APPID);//公众账号ID
        inputObj.setValue("mch_id", WxPayConfig.MCHID);//商户号
        inputObj.setValue("nonce_str", generateNonceStr());//随机字符串
        inputObj.setValue("sign", inputObj.MakeSign());//签名
        String xml = inputObj.toXml();

        long start = System.currentTimeMillis();//请求开始时间

        String response = HttpService.post(xml, url, false, timeOut);

        long end = System.currentTimeMillis();
        int timeCost = (int) (end - start);

        WxPayData result = new WxPayData();
        result.fromXml(response);

        reportCostTime(url, timeCost, result);//测速上报

        return result;
    }


    /**
     * 测速上报
     *
     * @param interface_url 接口URL
     * @param timeCost      接口耗时
     * @param inputObj      参数数组
     */
    private static void reportCostTime(String interface_url, int timeCost, WxPayData inputObj) {
        //如果不需要进行上报
        if (WxPayConfig.REPORT_LEVENL == 0) {
            return;
        }

        //如果仅失败上报
        if (WxPayConfig.REPORT_LEVENL == 1 && inputObj.isSet("return_code") && inputObj.getValue("return_code").toString() == "SUCCESS" &&
                inputObj.isSet("result_code") && inputObj.getValue("result_code").toString() == "SUCCESS") {
            return;
        }

        //上报逻辑
        WxPayData data = new WxPayData();
        data.setValue("interface_url", interface_url);
        data.setValue("execute_time_", timeCost);
        //返回状态码
        if (inputObj.isSet("return_code")) {
            data.setValue("return_code", inputObj.getValue("return_code"));
        }
        //返回信息
        if (inputObj.isSet("return_msg")) {
            data.setValue("return_msg", inputObj.getValue("return_msg"));
        }
        //业务结果
        if (inputObj.isSet("result_code")) {
            data.setValue("result_code", inputObj.getValue("result_code"));
        }
        //错误代码
        if (inputObj.isSet("err_code")) {
            data.setValue("err_code", inputObj.getValue("err_code"));
        }
        //错误代码描述
        if (inputObj.isSet("err_code_des")) {
            data.setValue("err_code_des", inputObj.getValue("err_code_des"));
        }
        //商户订单号
        if (inputObj.isSet("out_trade_no")) {
            data.setValue("out_trade_no", inputObj.getValue("out_trade_no"));
        }
        //设备号
        if (inputObj.isSet("device_info")) {
            data.setValue("device_info", inputObj.getValue("device_info"));
        }

        try {
            report(data, 10);
        } catch (RuntimeException ex) {
            //不做任何处理
        }
    }


    /**
     * 测速上报接口实现
     *
     * @param inputObj 提交给测速上报接口的参数
     * @param timeOut  测速上报接口超时时间
     * @return 成功时返回测速上报接口返回的结果，其他抛异常
     * @throws RuntimeException
     */
    public static WxPayData report(WxPayData inputObj, int timeOut) {
        String url = "https://api.mch.weixin.qq.com/payitil/report";
        //检测必填参数
        if (!inputObj.isSet("interface_url")) {
            throw new RuntimeException("接口URL，缺少必填参数interface_url！");
        }
        if (!inputObj.isSet("return_code")) {
            throw new RuntimeException("返回状态码，缺少必填参数return_code！");
        }
        if (!inputObj.isSet("result_code")) {
            throw new RuntimeException("业务结果，缺少必填参数result_code！");
        }
        if (!inputObj.isSet("user_ip")) {
            throw new RuntimeException("访问接口IP，缺少必填参数user_ip！");
        }
        if (!inputObj.isSet("execute_time_")) {
            throw new RuntimeException("接口耗时，缺少必填参数execute_time_！");
        }

        inputObj.setValue("appid", WxPayConfig.APPID);//公众账号ID
        inputObj.setValue("mch_id", WxPayConfig.MCHID);//商户号
        inputObj.setValue("user_ip", WxPayConfig.IP);//终端ip
        inputObj.setValue("time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));//商户上报时间
        inputObj.setValue("nonce_str", generateNonceStr());//随机字符串
        inputObj.setValue("sign", inputObj.MakeSign());//签名
        String xml = inputObj.toXml();

        logger.info("WxPayApi-Report request : " + xml);

        String response = HttpService.post(xml, url, false, timeOut);

        logger.info("WxPayApi-Report response : " + response);

        WxPayData result = new WxPayData();
        result.fromXml(response);
        return result;
    }

    /**
     * 根据当前系统时间加随机序列来生成订单号
     *
     * @return 订单号
     */
    public static String generateOutTradeNo() {

        return String.format("{0}{1}{2}", WxPayConfig.MCHID, DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"), RandomUtils.nextInt(0, 1000));
    }

    /**
     * 生成时间戳，标准北京时间，时区为东八区，自1970年1月1日 0点0分0秒以来的秒数
     *
     * @return 时间戳
     */
    public static String generateTimeStamp() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 生成随机串，随机串包含字母或数字
     *
     * @return 随机串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    public static void main(String[] args) {
		System.out.println(RandomUtils.nextInt(0, 1000));
	}
}
