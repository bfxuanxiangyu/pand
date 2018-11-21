package com.weeds.pand.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 腾讯地图类
 * @author user
 *
 */
public class TencentMapUtils {
	
	private static Logger logger = LoggerFactory.getLogger(TencentMapUtils.class);

	private static final String url = "http://apis.map.qq.com/ws/geocoder/v1/?key=";
	
	public static void main(String[] args) {
	}
	
	public static String getAddress(String location,String key){
		String address = null;
		try {
			String httpUrl = url+key+"&location="+location;
			String resultStr = HttpUtils.doGet(httpUrl);
			if(PandStringUtils.isNotBlank(resultStr)){
				// 转JSON格式
		        JSONObject jsonObject = JSONObject.parseObject(resultStr).getJSONObject("result");
		        // 获取地址（行政区划信息） 包含有国籍，省份，城市
		        JSONObject adcInfo = jsonObject.getJSONObject("address_component");
		        String district = (String) adcInfo.get("district");
		        address = (String) adcInfo.get("street");
		        if(PandStringUtils.isBlank(address)){
		        	address = district;
		        }
			}
		} catch (Exception e) {
			logger.error("根据经纬度获取地址异常"+e.getMessage(),e);
		}
		return address;
	}
}
