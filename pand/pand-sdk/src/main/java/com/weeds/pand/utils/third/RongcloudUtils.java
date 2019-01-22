package com.weeds.pand.utils.third;

import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.Maps;
import com.weeds.pand.utils.HttpUtils;

import io.rong.RongCloud;
import io.rong.methods.user.User;
import io.rong.models.Result;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;

public class RongcloudUtils {
	
	private static Logger logger = Logger.getLogger(RongcloudUtils.class);
	
	private static final String appKey = "25wehl3u2g80w";
	private static final String appSecret = "d2eAYx2bE8B";
//	private static final String rongcloudUrl = "http://api-cn.ronghub.com/user/getToken";
	
	public static void main(String[] args) {
		getRongcloudToken("ff8080816782751f016786b928f10007", "野草", "http://xxyweeds.top:8181/pand/img/user/20190107/d3d07fd5d92345c8bf9c873230e8fac5.png");
	}
	
	
	public static String getRongcloudToken(String userId,String name,String portraitUri){
		String token = null;
		try {
			RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
	        //自定义 api 地址方式
	        // RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret,api);
	        User User = rongCloud.user;

	        /**
	         * API 文档: http://www.rongcloud.cn/docs/server_sdk_api/user/user.html#register
	         *
	         * 注册用户，生成用户在融云的唯一身份标识 Token
	         */
	        UserModel user = new UserModel()
	                .setId(userId)
	                .setName(name)
	                .setPortrait(portraitUri);
	        TokenResult result = User.register(user);
	        if(result == null || result.getCode()!=200){
	        	return null;
	        }
	        token = result.getToken();
//	        logger.info("getToken:  " + result.toString());
	        /**
	         *
	         * API 文档: http://www.rongcloud.cn/docs/server_sdk_api/user/user.html#refresh
	         *
	         * 刷新用户信息方法
	         */
//	        Result refreshResult = User.update(user);
			
			logger.info("融云返回结果集："+result.toString());
		} catch (Exception e) {
			logger.error("获取融云token异常 "+e.getMessage(),e);
		}
		return token;
	}
	 

}
