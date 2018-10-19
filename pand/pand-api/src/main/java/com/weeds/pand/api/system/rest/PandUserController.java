package com.weeds.pand.api.system.rest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authc.credential.PasswordMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.weeds.pand.api.token.domain.AccessToken;
import com.weeds.pand.api.token.service.AccessTokenService;
import com.weeds.pand.service.mechanic.domain.PandUser;
import com.weeds.pand.service.mechanic.service.PandUserService;
import com.weeds.pand.utils.PandResponseUtil;
import com.weeds.pand.utils.PandStringUtils;
import com.weeds.pand.utils.PandValidationUtils;

@Controller
@RequestMapping("/api/freeuser")
public class PandUserController {

	private Logger logger = LoggerFactory.getLogger(PandUserController.class);
	
	@Resource
	private PandUserService pandUserService;
	@Resource
	private PasswordMatcher passwordMatcher;
	@Resource
	private AccessTokenService accessTokenService;
	
	/**
	 * 用户注册   
	 * @param regType 1提供微信注册   2用户名密码注册    3手机绑定
	 * @param userWeixin  regType=1 其他参数都可以为空
	 * @param userName    regType=2 userName、userPassword必传
	 * @param userPassword
	 * @param userPhone      regType=3 userPhone、authCode必传
	 * @param authCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/panduser_register")
	public String registerUser(String regType,String userWeixin,String userName,String userPassword,String userPhone,String authCode) {
		try {
			if(PandStringUtils.isBlank(regType)){
				return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
			}
			Map<String, Object> parameters = Maps.newHashMap();
			PandUser pandUser = new PandUser();
			if(regType.equals("1")){
				if(PandStringUtils.isBlank(userWeixin)){//微信注册
					return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
				}
				parameters.put("userWeixin", userWeixin);
				pandUser.setUserWeixin(userWeixin);
			}else if(regType.equals("2")){
				if(PandStringUtils.isBlank(userName)){//用户名密码注册
					return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
				}
				if(PandStringUtils.isBlank(userPassword)){
					return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"密码为空", null);
				}
				parameters.put("userName", userName);
				pandUser.setUserName(userName);
				pandUser.setUserPassword(passwordMatcher.getPasswordService().encryptPassword(userPassword));
			}else if(regType.equals("3")){
				if(PandStringUtils.isBlank(userPhone)){//手机验证码注册
					return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
				} 
				if(PandStringUtils.isBlank(authCode)){
					return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"验证码为空", null);
				}
				parameters.put("userPhone", userPhone);
				//验证码校验   暂留
				pandUser.setUserPhone(userPhone);
			}
			
			PandUser user = pandUserService.getPandUserObj(parameters);
			if(user != null){
				if(user.getUserStatus()==4){
					return PandResponseUtil.printFailJson(PandResponseUtil.PHONEALEDY,"用户已永久封号", null);
				}
				return PandResponseUtil.printFailJson(PandResponseUtil.PHONEALEDY,"用户已存在，请登录", null);
			}
			pandUser.setCreateTime(new Date());
			pandUser.setUserStatus(0);
			pandUser.setUserType(1);
			pandUserService.savePandUser(pandUser);
			
			return PandResponseUtil.printJson("注册成功", pandUser.getId());//返回用户id
			
		} catch (Exception e) {
			logger.error("用户登录异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	/**
	 * @param userName   用户名密码登录  userName不为空 userPassword必传
	 * @param userWeixin  微信免密登录  有userWeixin  其他可以不传
	 * @param userPhone    手机验证码登录    userPhone不为空  authCode必传
	 * @param userPassword
	 * @param authCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/panduser_login")
	public String userLogin(String userName,String userWeixin,String userPhone,String userPassword,String authCode,String platCode) {
		if(PandStringUtils.isBlank(platCode)){
			platCode = "1001";//小程序
		}
		try {
			Map<String, Object> returnMap = Maps.newHashMap();
			
			Map<String, Object> parameters = Maps.newHashMap();
			if(PandStringUtils.isNotBlank(userWeixin)){//微信登录
				parameters.put("userWeixin", userWeixin);
			}else if(PandStringUtils.isNotBlank(userName)){//用户名密码登录
				if(PandStringUtils.isBlank(userPassword)){
					return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"密码为空", null);
				}
				parameters.put("userName", userName);
			}else if(PandStringUtils.isNotBlank(userPhone)){//手机验证码登录
				if(PandStringUtils.isBlank(authCode)){
					return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"验证码为空", null);
				}
				parameters.put("userPhone", userPhone);
				//验证码校验   暂留
				
			} 
			PandUser user = pandUserService.getPandUserObj(parameters);
			if(user == null){
				return PandResponseUtil.printFailJson(PandResponseUtil.PHONENO,"用户不存在", null);
			}
			if(PandStringUtils.isNotBlank(userName)){//对比密码
				String enStr = passwordMatcher.getPasswordService().encryptPassword(userPassword);
				if(!enStr.equals(user.getUserPassword())){
					return PandResponseUtil.printFailJson(PandResponseUtil.passwd_match_fail,"密码不匹配", "no matching");
				}
			}
			if(user.getUserStatus()==4){
				return PandResponseUtil.printFailJson(PandResponseUtil.PHONEALEDY,"用户已永久封号", null);
			}
			//获取token
			AccessToken token = getAccessToken(user.getId(), platCode);
			
			returnMap.put("user", user);
			returnMap.put("token", token);
			
			return PandResponseUtil.printJson("登录成功", returnMap);
		} catch (Exception e) {
			logger.error("用户登录异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
		
	}
	
	
	/**
	 * 获取token对象
	 * @param userId
	 * @param platCode
	 * @return
	 */
	private AccessToken getAccessToken(String userId,String platCode){
		AccessToken token = null;
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("deviceId", userId);
		List<AccessToken> tokenList = accessTokenService.selectAll(parameters);
		if(tokenList!=null && !tokenList.isEmpty()){
			token = tokenList.get(0);
		}else{
			token = new AccessToken();
		}
		
		String accessToken = PandValidationUtils.getRandomString(32);
		String refreshToken = PandValidationUtils.getRandomString(32);
		token.setAccessToken(accessToken);
		token.setDeviceId(userId);
		token.setExpiresIn(System.currentTimeMillis());
		token.setPlatCode(platCode);
		token.setRefreshToken(refreshToken);
		token.setCreatedTime(System.currentTimeMillis());
		try {
			if(token.getId()==null){
				accessTokenService.insert(token);
			}else{
				accessTokenService.updateByPrimaryKey(token);
			}
		} catch (Exception e) {
			logger.error("token获取入库异常"+e.getMessage(),e);
		}
		return token;
	}
	
}
