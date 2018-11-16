package com.weeds.pand.api.system.rest;

import static com.weeds.pand.utils.PandStringUtils.isBlank;
import static com.weeds.pand.utils.PandStringUtils.isNotBlank;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.weeds.pand.service.mechanic.domain.PandUser;
import com.weeds.pand.service.mechanic.service.PandUserService;
import com.weeds.pand.service.pandcore.domain.PandAuditLog;
import com.weeds.pand.service.pandcore.domain.PandShop;
import com.weeds.pand.service.pandcore.domain.PandUserComment;
import com.weeds.pand.service.pandcore.service.PandShopService;
import com.weeds.pand.service.pandcore.service.PandUserCommentService;
import com.weeds.pand.service.system.domain.CardImage;
import com.weeds.pand.service.system.service.PandImagesService;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandResponseUtil;

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
	@Resource
	private PandImagesService pandImagesService;
	@Resource
	private PandShopService pandShopService; 
	@Resource
	private PandUserCommentService pandUserCommentService;
	
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
	public String completionUser(String token,PandUser pandUser,String authCode) {
		logger.info("个人信息补全信息参数:"+token+",pandUser:"+pandUser.toString()+",authCode="+authCode);
		try {
			if(isBlank(token) || isBlank(pandUser.getId())){
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
			if(isNotBlank(pandUser.getUserPhone())){//修改手机
				logger.info("绑定手机");
				if(isBlank(authCode)){
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
			if(isNotBlank(pandUser.getUserNickname())){
				oldObjById.setUserNickname(pandUser.getUserNickname());
			}
			//绑定微信
			if(isNotBlank(pandUser.getUserWeixin())){
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
			if(isNotBlank(pandUser.getUserHeadpng())){
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
	
	/**
	 * 成为商家  提交真实姓名、证件类型、证件号码、性别、所在区域（城市/区域）、详细地址、证件照
	 * @param token          登录token
	 * @param imagesJson 三种证件类型 1正面 2反面 3持证  baseStr是图片base64位后字符串  {[{"type": 1, "baseStr": "..."},{"type": 2, "baseStr": "..."},{"type": 3, "baseStr": "..."}]}
	 * @param id             用户id
	 * @param userRealname   用户真实姓名
	 * @param userCardtype   用户证件类型
	 * @param userCardcode   用户证件号码
	 * @param userSex        用户性别
	 * @param userProvinces  用户所在省份
	 * @param userCity       用户所在城市
	 * @param userArea       用户所在区域
	 * @param userAddress    用户所在详细地址
	 * @param userLat        用户服务地址纬度
	 * @param userLng        用户服务地址经度
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/panduser_enter_shopper")
	public String enterShopper(String token,PandUser pu,String imagesJson) {
		logger.info("成为商家参数:"+token+",pandUser:"+pu.toString());
		if(isBlank(token) || isBlank(pu.getId()) || isBlank(imagesJson)){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try {
			if(isBlank(pu.getUserRealname()) || pu.getUserCardtype()==null 
				|| isBlank(pu.getUserCardcode()) || pu.getUserSex()==null 
				|| isBlank(pu.getUserCity()) || isBlank(pu.getUserArea())
				|| isBlank(pu.getUserProvinces()) || isBlank(pu.getUserAddress())
				|| isBlank(pu.getUserLat()) || isBlank(pu.getUserLng())){
				return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
			}
			
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("id", pu.getId());
			PandUser oldObjById = pandUserService.getPandUserObj(parameters);
			if(oldObjById == null){
				return PandResponseUtil.printFailJson(PandResponseUtil.PHONENO,"用户不存在", null);
			}
			if(oldObjById.getUserType()==2 && oldObjById.getUserStatus()==2){
				return PandResponseUtil.printFailJson(PandResponseUtil.failed,"已经是商铺用户,请联系管理员", null);
			}
			
			//解析证件图片
			boolean ciFlag = false;
			try {
				List<CardImage> ciList = JSON.parseArray(imagesJson, CardImage.class);
				if(ciList!=null && !ciList.isEmpty()){
					List<String> baseStrList = null;
					//首先删除用户1下的照片
					Map<String, Object> dep = Maps.newConcurrentMap();
					dep.put("imgModel", 1);
					dep.put("modelId", pu.getId());
					pandImagesService.deletePandImagesObj(dep);
					for (CardImage cardImage : ciList) {
						if(cardImage.getType()==null || isBlank(cardImage.getBaseStr())){
							continue;
						}
						baseStrList = Lists.newArrayList();
						baseStrList.add(cardImage.getBaseStr());
						pandImagesService.savePandImages(cardImage.getType(), 1, pu.getId(), baseStrList);
						ciFlag = true;
					}
				}
			} catch (Exception e) {
				logger.error("证件照片异常"+e.getMessage(),e);
			}
			
			if(!ciFlag){
				return PandResponseUtil.printFailJson(PandResponseUtil.card_image_error,"证件照片异常", null);
			}
			
			
			oldObjById.setUpdateTime(new Date());
			oldObjById.setUserRealname(pu.getUserRealname());
			oldObjById.setUserCardtype(pu.getUserCardtype());
			oldObjById.setUserCardcode(pu.getUserCardcode());
			oldObjById.setUserSex(pu.getUserSex());
			oldObjById.setUserProvinces(pu.getUserProvinces());
			oldObjById.setUserCity(pu.getUserCity());
			oldObjById.setUserArea(pu.getUserArea());
			oldObjById.setUserAddress(pu.getUserAddress());
			oldObjById.setUserType(2);
			oldObjById.setUserStatus(1);
			oldObjById.setUserLat(pu.getUserLat());
			oldObjById.setUserLng(pu.getUserLng());
			pandUserService.savePandUser(oldObjById);
			
			//生成自己店铺信息
			savePandShopInfo(oldObjById);
			
			return PandResponseUtil.printJson("入驻成功，请等待2-3个工作日审核，期间请保持电话畅通", oldObjById);//返回用户id 
		} catch (Exception e) {
			logger.error("用户登录异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
		
	}
	
	private void savePandShopInfo(PandUser pu){
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("pandUserId", pu.getId());
		PandShop oldPs = pandShopService.getPandShopObject(parameters);
		if(oldPs!=null){
			return;
		}
		PandShop ps = new PandShop();
		ps.setCreateTime(new Date());
		ps.setPandUserId(pu.getId());
		ps.setShopDes("潘多菈授权荣誉店，旨在为您服务");
		ps.setShopImg(pu.getUserHeadpng());
		ps.setShopName(pu.getUserRealname()+"的店");
		ps.setShopStatus(0);
		ps.setShopTel(pu.getUserPhone());
		ps.setShopTime("全天候");
		pandShopService.savePandShop(ps);
	}
	
	
	/**
	 * 上传头像
	 * @param baseStr
	 * @return
	 */
	private String uploadHeadPng(String baseStr){
		String httpStr = null;
		try {
			String porderPath = "headPng/"+PandDateUtils.dateToStr(new Date(), "yyyyMMdd")+"/";
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
	
	
	
	/**
	 * 获取商户审核不通过原因
	 * @param token      用户token
	 * @param pandUserId 用户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/panduser_shoper_audit")
	public String shoperAudit(String token,String pandUserId) {
		logger.info("商户审核不通过原因参数:"+token+",pandUserId:"+pandUserId);
		try {
			if(isBlank(token) || isBlank(pandUserId)){
				return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
			}
			
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("pandUserId", pandUserId);
			parameters.put("auditType", 3);
			
			PandAuditLog pal = pandUserService.getPandAuditLogObj(parameters);
			
			return PandResponseUtil.printJson("获取成功", pal);//返回不通过原因
			
		} catch (Exception e) {
			logger.error("商户审核不通过原因"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	/**
	 * 商户审核状态
	 * @param token      用户token
	 * @param pandUserId 用户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/panduser_shoper_status")
	public String shoperAuditSatus(String token,String pandUserId) {
		logger.info("商户审核状态参数:"+token+",pandUserId:"+pandUserId);
		try {
			if(isBlank(token) || isBlank(pandUserId)){
				return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
			}
			
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("id", pandUserId);
			PandUser oldObjById = pandUserService.getPandUserObj(parameters);
			
			if(oldObjById == null){
				return PandResponseUtil.printFailJson(PandResponseUtil.PHONENO,"用户不存在", null);
			}
			
			return PandResponseUtil.printJson("获取成功", oldObjById.getUserStatus());
			
		} catch (Exception e) {
			logger.error("商户审核状态"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	
	/**
	 * 评论
	 * @param token          用户token 必填
	 * @param pandUserId     用户id 必填
	 * @param serviceId      服务id 必填
	 * @param skilledScore   技术熟练分数 必填
	 * @param attitudeScore  服务态度分数 必填
	 * @param efficiencyScore工作效率分数 必填
	 * @param anonymous      是否匿名  0是  1否 必填
	 * @param comment        评论内容 必填
	 * @param imagesJson     服务照片baseStr是图片base64位后字符串  {[{"baseStr": "..."},{"baseStr": "..."},{"baseStr": "..."}]} 选填
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/comment_save")
	public String commentSave(String token,PandUserComment comment,String imagesJson) {
		logger.info("评论参数:"+token+",comment:"+comment.toString());
		try {
			if(isBlank(token) || isBlank(comment.getPandUserId())  || isBlank(comment.getServiceId()) || comment.getSkilledScore() == null
					|| comment.getAttitudeScore()==null || comment.getEfficiencyScore()==null ||comment.getAnonymous()==null
					|| isBlank(comment.getComment())){
				return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
			}
			
			//如果本人已经评论过  不进行再次评论
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("pandUserId", comment.getPandUserId());
			parameters.put("serviceId", comment.getServiceId());
			parameters.put("status", 0);
			
			List<PandUserComment> commentList = pandUserCommentService.getPandUserCommentList(parameters);
			if(commentList!=null && !commentList.isEmpty()){
				return PandResponseUtil.printFailJson(PandResponseUtil.comment_again,"不能重复评论", null);
			}
			
			comment.setCreateTime(new Date());
			comment.setStatus(0);
			
			pandUserCommentService.savePandUserComment(comment);
			
			if(isNotBlank(imagesJson)){
				try {
					List<CardImage> ciList = JSON.parseArray(imagesJson, CardImage.class);
					if(ciList!=null && !ciList.isEmpty()){
						if(ciList!=null && !ciList.isEmpty()){
							List<String> baseStrList = null;
							for (CardImage ci : ciList) {
								if(isBlank(ci.getBaseStr())){
									continue;
								}
								baseStrList = Lists.newArrayList();
								baseStrList.add(ci.getBaseStr());
							}
							pandImagesService.savePandImages(0,2, comment.getId(), baseStrList);
						}
					}
				} catch (Exception e) {
					logger.error("服务照片上传异常"+e.getMessage(),e);
				}
			}
			
			return PandResponseUtil.printJson("评论成功", comment);
			
		} catch (Exception e) {
			logger.error("评论异常原因"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}

	/**
	 * 删除评论
	 * @param token
	 * @param id     评论id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/comment_delete")
	public String commentDelete(String token,String id) {
		logger.info("删除评论参数:"+token+",id:"+id);
		try {
			if(isBlank(token) || isBlank(id)){
				return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
			}
			
			PandUserComment comment = pandUserCommentService.getPandUserCommentById(id);
			
			if(comment == null){
				return PandResponseUtil.printFailJson(PandResponseUtil.no_comment,"评论不存在", null);
			}
			comment.setStatus(1);
			pandUserCommentService.savePandUserComment(comment);
			return PandResponseUtil.printJson("删除成功", null);
			
		} catch (Exception e) {
			logger.error("删除评论异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	
	
}
