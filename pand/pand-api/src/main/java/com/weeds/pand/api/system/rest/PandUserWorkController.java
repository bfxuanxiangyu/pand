package com.weeds.pand.api.system.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.weeds.pand.service.mechanic.domain.PandUser;
import com.weeds.pand.service.mechanic.service.PandUserService;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandResponseUtil;
import com.weeds.pand.utils.PandStringUtils;

@Controller
@RequestMapping("/api/panduser")
public class PandUserWorkController {

	private Logger logger = LoggerFactory.getLogger(PandUserWorkController.class);
	
	@Value("${img.savePath}")
	private String savePath;
	@Value("${img.imgUrl}")
	private String imgUrl;
	
	@Resource
	private PandUserService pandUserService;
	
	/**
	 * 个人信息补全信息
	 *   1、修改手机   userPhone、anthCode不能为空
	 *   2、修改昵称  userNickname不能为空
	 *   3、绑定微信 userWeixin不能为空
	 *   4、编辑头像 userHeadpng   不能为空，base64字符串上传
	 * @param token
	 * @param pandUser
	 * @param authCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/panduser_completion")
	public String registerUser(String token,PandUser pandUser,String authCode) {
		try {
			if(PandStringUtils.isBlank(token) || PandStringUtils.isBlank(pandUser.getId())){
				return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
			}
			
			
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("id", pandUser.getId());
			PandUser oldObjById = pandUserService.getPandUserObj(parameters);
			if(oldObjById == null){
				return PandResponseUtil.printFailJson(PandResponseUtil.PHONENO,"用户不存在", null);
			}
			if(oldObjById.getUserStatus()==4){
				return PandResponseUtil.printFailJson(PandResponseUtil.PHONEALEDY,"用户已永久封号", null);
			}
			
			//绑定手机手机
			if(PandStringUtils.isNotBlank(pandUser.getUserPhone())){//修改手机
				logger.info("绑定手机");
				if(PandStringUtils.isBlank(authCode)){
					return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少手机绑定验证码", null);
				}
				//验证码校验
				
				//单独查询手机是否是独立用户
				parameters.clear();
				parameters.put("userPhone",pandUser.getUserPhone());
				PandUser phoneUser = pandUserService.getPandUserObj(parameters);
				if(phoneUser==null){
					oldObjById.setUserPhone(pandUser.getUserPhone());
				}else{
					if(!phoneUser.getId().equals(oldObjById.getId())){
						return PandResponseUtil.printFailJson(PandResponseUtil.passwd_binding_again,"手机号已被独立注册，不允许绑定", null);
					}else{
						oldObjById.setUserPhone(pandUser.getUserPhone());
					}
				}
			}
			//补全昵称
			if(PandStringUtils.isNotBlank(pandUser.getUserNickname())){
				oldObjById.setUserNickname(pandUser.getUserNickname());
			}
			//绑定微信
			if(PandStringUtils.isNotBlank(pandUser.getUserWeixin())){
				//单独查询微信是否是独立用户
				parameters.clear();
				parameters.put("userWeixin",pandUser.getUserWeixin());
				PandUser weixinUser = pandUserService.getPandUserObj(parameters);
				if(weixinUser==null){
					oldObjById.setUserWeixin(pandUser.getUserWeixin());
				}else{
					if(!weixinUser.getId().equals(oldObjById.getId())){
						return PandResponseUtil.printFailJson(PandResponseUtil.passwd_binding_again,"微信已被独立注册，不允许绑定", null);
					}else{
						oldObjById.setUserWeixin(pandUser.getUserWeixin());
					}
				}
			}
			//上传头像
			if(PandStringUtils.isNotBlank(pandUser.getUserHeadpng())){
				String httpStr = uploadHeadPng(pandUser.getUserHeadpng());
				oldObjById.setUserHeadpng(httpStr);
			}
			
			oldObjById.setUpdateTime(new Date());
			pandUserService.savePandUser(oldObjById);
			
			return PandResponseUtil.printJson("补全成功", oldObjById);//返回用户id
			
		} catch (Exception e) {
			logger.error("用户登录异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	
	private String uploadHeadPng(String baseStr){
		String httpStr = null;
		try {
			String porderPath = "headPng/"+PandDateUtils.dateToStr(new Date(), "yyyyMMdd")+File.separator;
			File file = new File(savePath+porderPath);
			if(!file.exists()){
				file.mkdirs();
			}
			String fileName = PandDateUtils.dateToStr(new Date(), "yyyyMMddHHmmss")+".png";
			byte[] bytes = Base64Utils.decode(baseStr.getBytes());
			OutputStream out = new FileOutputStream(savePath+porderPath+fileName);
			out.write(bytes);
			out.flush();
			out.close();
			
			httpStr = imgUrl+porderPath+fileName;
		} catch (Exception e) {
			logger.error("图片保存异常"+e.getMessage(),e);
		}
		return httpStr;
	}
	
	
	
}
