package com.weeds.pand.api.system.rest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authc.credential.PasswordMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.weeds.pand.api.token.domain.AccessToken;
import com.weeds.pand.api.token.service.AccessTokenService;
import com.weeds.pand.service.mechanic.domain.PandUser;
import com.weeds.pand.service.mechanic.service.PandUserService;
import com.weeds.pand.service.pandcore.domain.PandUserComment;
import com.weeds.pand.service.pandcore.service.PandUserCommentService;
import com.weeds.pand.service.system.domain.Page;
import com.weeds.pand.service.system.domain.SmsSend;
import com.weeds.pand.service.system.service.SmsSendService;
import com.weeds.pand.utils.PandResponseUtil;
import com.weeds.pand.utils.PandStringUtils;
import com.weeds.pand.utils.PandValidationUtils;
import com.weeds.pand.utils.ali.AliSmsSendUtil;

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
	@Resource
	private PandUserCommentService pandUserCommentService;
	@Resource
	private SmsSendService smsSendService;
	
	
	@Value("${img.imgUrl}")
	private String imgUrl;
	
	@Value("${sms.key}")
	private String smsKey;
	@Value("${sms.flag}")
	private String smsFlag;
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
				SmsSend smsObj = smsSendService.getSmsSendByPhone(userPhone);
				if(smsObj==null){
					return PandResponseUtil.printFailJson(PandResponseUtil.validateError,"验证码不匹配", null);
				}
				if(!smsSendService.verifySms(authCode, smsObj)){
					return PandResponseUtil.printFailJson(PandResponseUtil.auth_code_already,"验证码无效", null);
				}
				
				pandUser.setUserPhone(userPhone);
				//并把手机号作为用户名保存,默认密码为123456
				pandUser.setUserName(userName);
				if(PandStringUtils.isNotBlank(userPassword)){
					pandUser.setUserPassword(passwordMatcher.getPasswordService().encryptPassword(userPassword));
				}else{
					pandUser.setUserPassword(passwordMatcher.getPasswordService().encryptPassword("123456"));
				}
			}
			
			PandUser user = pandUserService.getPandUserObj(parameters);
			if(user != null){
				if(user.getUserStatus()==4){
					return PandResponseUtil.printFailJson(PandResponseUtil.PHONEALEDY,"用户已永久封号", null);
				}
				return PandResponseUtil.printFailJson(PandResponseUtil.PHONEALEDY,"用户已存在，请登录", null);
			}
			pandUser.setCreateTime(new Date());
			pandUser.setUserNickname("pandid_"+PandStringUtils.getRandomStr(14));
			pandUser.setUserStatus(0);
			pandUser.setUserType(1);
			pandUser.setUserHeadpng(imgUrl+"headPng/pand_default_head.png");
			pandUserService.savePandUser(pandUser);
			
			return PandResponseUtil.printJson("注册成功", pandUser.getId());//返回用户id
			
		} catch (Exception e) {
			logger.error("用户登录异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	/**
	 * 忘记密码
	 * @param userPhone   用户手机  必填
	 * @param authCode    手机验证码  必填
	 * @param userPassword密码  必填
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/panduser_forgetpasswd")
	public String forgetpasswd(String userPassword,String userPhone,String authCode) {
		try {
			if(PandStringUtils.isBlank(userPhone) || PandStringUtils.isBlank(userPassword) || PandStringUtils.isBlank(authCode)){
				return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
			}
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("userPhone", userPhone);
			PandUser user = pandUserService.getPandUserObj(parameters);
			if(user == null){
				return PandResponseUtil.printFailJson(PandResponseUtil.PHONEALEDY,"未绑定手机", null);
			}
			if(user.getUserStatus()==4){
				return PandResponseUtil.printFailJson(PandResponseUtil.PHONEALEDY,"已永久封号", null);
			}
			user.setUserPassword(passwordMatcher.getPasswordService().encryptPassword(userPassword));
			
			return PandResponseUtil.printJson("密码修改成功", null);//返回用户id
			
		} catch (Exception e) {
			logger.error("密码修改异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	/**
	 * 获取验证码
	 * @param userPhone   用户手机  必填
	 * @param key         秘钥  必填
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/panduser_send_authcode")
	public String sendSms(String userPhone ,String key) {
		try {
			if(PandStringUtils.isBlank(userPhone) || PandStringUtils.isBlank(key)){
				return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
			}
			if(!smsKey.equals(key)){
				return PandResponseUtil.printFailJson(PandResponseUtil.failed,"key非法", null);
			}
			Map<String, String> parameters = Maps.newConcurrentMap();
			String authCode = PandStringUtils.getRandomNum(6);
			parameters.put("code", authCode);
			
			SmsSend obj = new SmsSend();
			obj.setContent(authCode);
			obj.setCreateTime(new Date());
			obj.setPhone(userPhone);
			obj.setType(1);
			smsSendService.saveSmsSend(obj);
			
			//短信发送
			if(smsFlag.equals("true")){//发送短信
				AliSmsSendUtil.sendSms(userPhone, "SMS_156465904","米米科技", PandStringUtils.getJsonObj(parameters));
				return PandResponseUtil.printJson("验证码发送成功", null);//返回用户id
			}else{
				return PandResponseUtil.printJson("验证码发送成功", authCode);//返回用户id
			}
			
		} catch (Exception e) {
			logger.error("验证码发送异常"+e.getMessage(),e);
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
				SmsSend smsObj = smsSendService.getSmsSendByPhone(userPhone);
				if(smsObj==null){
					return PandResponseUtil.printFailJson(PandResponseUtil.validateError,"验证码不匹配", null);
				}
				if(!smsSendService.verifySms(authCode, smsObj)){
					return PandResponseUtil.printFailJson(PandResponseUtil.auth_code_already,"验证码无效", null);
				}
				
			} 
			PandUser user = pandUserService.getPandUserObj(parameters);
			if(user == null){
				//用户不存在   自动补充进去
				user = new PandUser();
				user.setUserPhone(userPhone);
				user.setUserWeixin(userWeixin);
				user.setUserNickname("pandid_"+PandStringUtils.getRandomStr(14));
				user.setCreateTime(new Date());
				user.setUserStatus(0);
				user.setUserType(1);
				user.setUserHeadpng(imgUrl+"headPng/pand_default_head.png");
				pandUserService.savePandUser(user);
//				return PandResponseUtil.printFailJson(PandResponseUtil.PHONENO,"用户不存在", null);
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
	
	
	/**
	 * 评论查询 支持服务id和评论者查询  支持分页
	 * @param pageIndex     当前页 从1开始   必填
	 * @param pageSize      每页数量  默认为10 必填
	 * @param serviceId     服务id 必填
	 * @param pandUserId    评论者id 选填
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/comment_list")
	public String commentList(String serviceId,String pandUserId,Integer pageIndex,Integer pageSize,Integer current) {
		logger.info("评论查询参数serviceId:"+serviceId+",pandUser:"+pandUserId+",pageIndex="+pageIndex+",pageSize="+pageSize);
		if(pageIndex == null || pageSize==null){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try {
			Map<String, Object> parameters = Maps.newHashMap();
			parameters = Page.getPageMap(parameters, pageIndex, pageSize);
			parameters.put("status", 0);
			if(PandStringUtils.isNotBlank(serviceId)){
				parameters.put("serviceId", serviceId);
			}
			if(PandStringUtils.isNotBlank(pandUserId)){
				parameters.put("pandUserId", pandUserId);
			}
			if(current != null && current==1){//只查询带图数据
				parameters.put("haveImg", "haveImg");
			}
			List<PandUserComment> list = pandUserCommentService.getPandUserCommentList(parameters);
			return PandResponseUtil.printJson("评论列表获取成功", list);
		} catch (Exception e) {
			logger.error("评论查询异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
		
	}
	/**
	 * 评论详情
	 * @param id  评论id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/comment_detail")
	public String commentList(String id) {
		logger.info("评论查询参数id:"+id);
		if(PandStringUtils.isBlank(id)){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try {
			PandUserComment comment = pandUserCommentService.getPandUserCommentById(id);
			return PandResponseUtil.printJson("评论详情获取成功", comment);
		} catch (Exception e) {
			logger.error("评论详情查询异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
		
	}
	
	
}
