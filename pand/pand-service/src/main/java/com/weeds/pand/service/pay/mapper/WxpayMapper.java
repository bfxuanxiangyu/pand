package com.weeds.pand.service.pay.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.weeds.pand.service.pay.domain.Wxpay;


/**
 * Created by admin on 2016/6/1.
 */
@Mapper
public interface WxpayMapper {

	public List<Wxpay> getWxpayList(Map<String, Object> parameters);

	public int getWxpayCount(Map<String, Object> parameters);
	
	public String getMaxOutTradeNo(Map<String, Object> parameters);
	
	public Wxpay getWxpayObject(Map<String, Object> parameters);

}
