package com.weeds.pand.utils.weixin;

import java.util.Date;
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
	
	public static WeixinAccessTokenVo tokenObj = null;
//	private static String qrcode_url= "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";
	
	public static String isVailToken(String appid,String secret){
		String accessToken = null;
		try {
			
			if(tokenObj == null){
				return getAccessToken(appid, secret);
			}
			boolean vailetime = vailetime(tokenObj.getDate(), new Date());
			if(!vailetime || PandStringUtils.isBlank(tokenObj.getAccessToken())){//判断是否超过两小时，如果超过两小时则重新获取
				return getAccessToken(appid, secret);
			}
			return tokenObj.getAccessToken();
		} catch (Exception e) {
			logger.error("token对象校验异常"+e.getMessage(),e);
		}
		return accessToken;
	}
	
	private static String getAccessToken(String appid,String secret){
		String accessToken = null;
		try {
			String tokenUrl = token_url+"&appid="+appid+"&secret="+secret;
			String res = HttpUtils.doGet(tokenUrl);
			logger.info("httpUrl="+tokenUrl+",token info="+res);
			JSONObject obj = JSON.parseObject(res);
			if(obj==null){
				return null;
			}
			accessToken = obj.getString("access_token");
			if(PandStringUtils.isBlank(accessToken)){
				return null;
			}
			tokenObj = new WeixinAccessTokenVo();
			tokenObj.setAccessToken(accessToken);
			tokenObj.setDate(new Date());
		} catch (Exception e) {
			logger.error("获取微信token异常"+e.getMessage(),e);
		}
		return accessToken;
	}
	
	public static boolean getQrCodeUrl(String appid,String secret,String path,int width,String savePath,String fileName){
		boolean getQrcodeUrl = false;
		try {
			String accessToken = getAccessToken(appid, secret);
			String qrcodeUrl = qrcode_url+accessToken;
			Map<String, Object> params = Maps.newHashMap();
			params.put("access_token", accessToken);
			params.put("path", path);
			params.put("width", width);
			String paramsJson = PandStringUtils.getJsonObj(params);
			getQrcodeUrl = HttpUtils.doPostForFormat(qrcodeUrl, paramsJson,savePath,fileName);
			logger.info("微信分享二维码获取结果"+getQrcodeUrl);
		} catch (Exception e) {
			logger.error("获取微信token异常"+e.getMessage(),e);
		}
		return getQrcodeUrl;
	}
	public static boolean getQrCodeUrl(String accessToken,String path,int width,String savePath,String fileName){
		boolean getQrcodeUrl = false;
		try {
			String qrcodeUrl = qrcode_url+accessToken;
			Map<String, Object> params = Maps.newHashMap();
			params.put("path", path);
			params.put("width", width);
			String paramsJson = PandStringUtils.getJsonObj(params);
			getQrcodeUrl = HttpUtils.doPostForFormat(qrcodeUrl, paramsJson,savePath,fileName);
			logger.info("微信分享二维码获取结果"+getQrcodeUrl);
		} catch (Exception e) {
			logger.error("获取微信token异常"+e.getMessage(),e);
		}
		return getQrcodeUrl;
	}
	
	private static boolean vailetime(Date startDate,Date endDdate){
		boolean flag = false;
		try {
			long cha = endDdate.getTime() - startDate.getTime();
			double result = cha * 1.0 / (1000 * 60 * 60);
			if(result>1){
				return false; //说明小于24小时
			}else{
				return true;
			}
		} catch (Exception e) {
			logger.error("日期校验异常"+e.getMessage(),e);
		}
		return flag;
	}
	
	public static void main(String[] args) {
		String accessToken = "16_dITSQGtgTSm54PHp0ncg55VwW-3eve4y6qeTxBbxw37ckcAIsabFw3qArGJ8xu0HEvO0i0IWmo-WBus3CQaoNDe25qPRPfjNPV8eKlYwCs4q77JBvT7kvMYWfD2FGko7j5wwzRJDNfrN_dvxGSXbAGAYKP";
		String path="pages/service/servicedetails/servicedetails?id=ff808081674f602b016758c0fc3100a7";
		int width = 430;
		String savePath="d://opt//";
		String fileName="qrcode1.jpg";
//		getQrCodeUrl("wxcf96bc0f2160e79e","25631fbc0abfaf5986b08d83babad5d6", path, width,savePath,fileName);
		getQrCodeUrl(accessToken, path, width,savePath,fileName);
		
		/*String s = "2018-12-06 08:08:45";
		System.out.println(vailetime(PandDateUtils.parseStrToDate(s, "yyyy-MM-dd HH:mm:ss"), new Date()));*/
	}

}
