package com.weeds.pand.service.pay.service;

import java.util.Map;

import com.weeds.pand.service.pay.domain.Wxpay;

public interface WxpayService {

	String saveOrUpdateWxpay(Wxpay wxpay);
	
	Wxpay getWxpayObject(String id);
	
	Wxpay getWxpayObject(Map<String, Object> parameters);
	
	String generateOutTradeNo();
	
}
