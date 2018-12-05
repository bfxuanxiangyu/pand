package com.weeds.pand.utils.weixin;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.weeds.pand.utils.HttpUtils;
import com.weeds.pand.utils.PandStringUtils;

public class GetWeixinInfoUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(GetWeixinInfoUtils.class);
	
	private static String token_url= "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
	private static String qrcode_url= "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=";
	
	
	public static String getAccessToken(String appid,String secret){
		String accessToken = null;
		try {
			token_url = token_url+"&appid="+appid+"&secret="+secret;
			String res = HttpUtils.doGet(token_url);
			JSONObject obj = JSON.parseObject(res);
			if(obj==null){
				return null;
			}
			accessToken = obj.getString("access_token");
			logger.info("获取微信成功token="+accessToken);
		} catch (Exception e) {
			logger.error("获取微信token异常"+e.getMessage(),e);
		}
		return accessToken;
	}
	
	public static boolean getQrCodeUrl(String accessToken,String path,int width,String savePath,String fileName){
		boolean getQrcodeUrl = false;
		try {
			qrcode_url = qrcode_url+accessToken;
			Map<String, Object> params = Maps.newHashMap();
			params.put("path", path);
			params.put("width", width);
			String paramsJson = PandStringUtils.getJsonObj(params);
			getQrcodeUrl = HttpUtils.doPostForFormat(qrcode_url, paramsJson,savePath,fileName);
			System.out.println(getQrcodeUrl);
		} catch (Exception e) {
			logger.error("获取微信token异常"+e.getMessage(),e);
		}
		return getQrcodeUrl;
	}
	/*public static void main(String[] args) {
		String accessToken = "2316_wZ64ctiwog8qva4Nb_Zc7NUewgG7JCoAw67bFldvuEqLeDon1Ahwtz4lURPheUzOwihCyWDWaCuKJeFfusnpk7IjncbrIXL0JOcle6My30IuYvX292o_PZy3rOQLp0nu8FbxVyMfeXsPHI1MKBSeAAAPGX";
		String path="pages/service/servicedetails/servicedetails?id=ff808081674f602b016758c0fc3100a7";
		int width = 430;
		String savePath="d://opt//";
		String fileName="qrcode1.jpg";
		getQrCodeUrl(accessToken, path, width,savePath,fileName);
	}*/

}
