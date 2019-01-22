package com.weeds.pand.utils;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

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
	
	public static Map<String,Object> getAddress(String location,String key){
		Map<String,Object> map = Maps.newHashMap();
		try {
			String httpUrl = url+key+"&location="+location;
			String resultStr = HttpUtils.doGet(httpUrl);
//			logger.info("腾讯地图返回结果集:"+resultStr);
			if(PandStringUtils.isNotBlank(resultStr)){
				// 转JSON格式
				JSONObject jsonObject = JSONObject.parseObject(resultStr).getJSONObject("result");
				if(jsonObject==null){
					return map;
				}
				// 获取地址（行政区划信息） 包含有国籍，省份，城市
				JSONObject adcInfo = jsonObject.getJSONObject("address_component");
				map.put("address", jsonObject.get("address"));
				map.put("nation", adcInfo.get("nation"));
				map.put("city", adcInfo.get("city"));
				map.put("district", adcInfo.get("district"));
				map.put("street", adcInfo.get("street"));
				map.put("street_number", adcInfo.get("street_number"));
			}
		} catch (Exception e) {
			logger.error("根据经纬度获取地址异常"+e.getMessage(),e);
		}
		return map;
	}
	public static String getAddressPOI(String location,String key){
		String pois = null;
		try {
			String httpUrl = url+key+"&location="+location+"&get_poi=1";
			String resultStr = HttpUtils.doGet(httpUrl);
//			logger.info("腾讯地图返回结果集:"+resultStr);
			if(PandStringUtils.isNotBlank(resultStr)){
				// 转JSON格式
		        JSONObject jsonObject = JSONObject.parseObject(resultStr).getJSONObject("result");
		        if(jsonObject==null){
		        	return pois;
		        }
		        // 获取地址（行政区划信息） 包含有国籍，省份，城市
		        JSONArray jsonArray = jsonObject.getJSONArray("pois");
		        if(jsonArray != null){
		        	pois = jsonArray.toJSONString();
		        }
		        
			}
		} catch (Exception e) {
			logger.error("根据经纬度获取地址异常"+e.getMessage(),e);
		}
		return pois;
	}
}
