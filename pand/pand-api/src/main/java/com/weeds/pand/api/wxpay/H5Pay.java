package com.weeds.pand.api.wxpay;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H5Pay {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String timeStamp;//时间戳，自1970年以来的秒数     
	
	private String nonceStr;//随机串
	
	private String packagep;//预支付数据包
	
	private String signType; //微信签名方式：   
	
	private String paySign; //微信签名 
	
	//采用排序的Dictionary的好处是方便对数据包进行签名，不用再签名之前再做一次排序
    private TreeMap<String, Object> m_values = new TreeMap<String, Object>();


	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	public String getPackagep() {
		return packagep;
	}

	public void setPackagep(String packagep) {
		this.packagep = packagep;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	 /**
     * 设置某个字段的值
     *
     * @param key   字段名
     * @param value 字段值
     */
    public void setValue(String key, Object value) {
        m_values.put(key, value);
    }

    /**
     * 根据字段名获取某个字段的值
     *
     * @param key 字段名
     * @return key对应的字段值
     */
    public Object getValue(String key) {
        return m_values.get(key);
    }
    
    
    /**
     * 格式转化成url参数格式
     *
     * @ return url格式串, 该串不包含sign字段值
     */
    public String toUrl() {
        String buff = "";
        for (Map.Entry<String, Object> pair : m_values.entrySet()) {
            if (pair.getValue() == null) {
                logger.error("WxPayData内部含有值为null的字段!");
                throw new RuntimeException("WxPayData内部含有值为null的字段!");
            }
            if (pair.getKey() != "sign" && pair.getValue().toString() != "") {
                buff += pair.getKey() + "=" + pair.getValue() + "&";
            }
        }
        buff = buff.substring(0, buff.length() - 1);
        return buff;
    }
    
    
    /**
     * @return 签名, sign字段不参加签名
     * @生成签名，详见签名生成算法
     */

    public String MakeSign() {
        //转url格式
        String str = toUrl();
        //在string后加入API KEY
        str += "&key=" + WxPayConfig.KEY;
        logger.info("微信支付url拼接="+str);
        //MD5加密
        return DigestUtils.md5Hex(str).toUpperCase();
    }
	
}
