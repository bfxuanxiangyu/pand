package com.weeds.pand.api.system.rest;

import static com.weeds.pand.utils.PandStringUtils.isBlank;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.weeds.pand.service.system.service.PandImagesService;
import com.weeds.pand.utils.HttpUtils;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandResponseUtil;
import com.weeds.pand.utils.PandStringUtils;

/**
 * @author user
 * pand 服务控制类
 *
 */
@Controller
@RequestMapping("/api/pand_image_upload")
public class PandImageController {

	private Logger logger = LoggerFactory.getLogger(PandImageController.class);
	
	@Value("${img.savePath}")
	private String savePath;
	@Value("${img.face.http}")
	private String faceHttp;
	@Value("${img.imgUrl}")
	private String imgUrl;
	@Resource
	private PandImagesService pandImagesService;
	/**
	 * 上传图片
	 * @param token
	 * @param files     图片数组流
	 * @param imgModel  所属模块  1用户审核   2评论 3服务图片
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/image_upload")
	public String toBuy(String token,@RequestParam(value="file", required=false) MultipartFile file,Integer imgModel) {
		logger.info("图片上传参数:"+token);
		if(isBlank(token) || file==null || file.isEmpty()){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		String httpUrl = null;
		try{
			if(imgModel==null){
				imgModel = -1;
			}
			String porder = "common/";
			if(imgModel==1){
				porder = "user/";
			}else if(imgModel==2){
				porder = "comment/";
			}else if(imgModel==3){
				porder = "service/";
			}
			if (file != null && !file.isEmpty()) {    
				try {    
					String porderPath = porder+PandDateUtils.dateToStr(new Date(), "yyyyMMdd")+"/";
					File imgFile = new File(savePath+porderPath);
					if(!imgFile.exists()){
						imgFile.mkdirs();
					}
					String fileName = PandStringUtils.getUUID()+".png";
					file.transferTo(new File(savePath+porderPath+fileName));
					httpUrl = imgUrl+porderPath+fileName;
					logger.info("保存本次照片imgModel="+imgModel+",路径："+httpUrl);
				} catch (Exception e) {    
					logger.error("图片上传异常"+e.getMessage(),e);
				} 
			}
			if(isBlank(httpUrl)){
				return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
			}
			return PandResponseUtil.printJson("上传成功",httpUrl);
		} catch (Exception e) {
			logger.error("图片流上传异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	/**
	 * 人脸比对照片上传
	 * @param token
	 * @param files     图片数组流
	 * @param imgModel  所属模块  1用户审核   2评论 3服务图片
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/face_upload")
	public String imageFace(String token,@RequestParam(value="file", required=false) MultipartFile file
				,Integer imgModel,String userId,String userName) {
		logger.info("人脸图片上传参数 token="+token+",userId="+userId+",userName="+userName);
		if(isBlank(token) || file==null || file.isEmpty() || isBlank(userId)){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		String httpUrl = null;
		try{
			if(imgModel==null || imgModel != 4){
				return PandResponseUtil.printFailJson(PandResponseUtil.no_faceimg,"服务器升级", null);
			}
			String porder = "faceinfo/";
			if (file != null && !file.isEmpty()) {    
				try {    
					String porderPath = porder+PandDateUtils.dateToStr(new Date(), "yyyyMMdd")+"/";
					File imgFile = new File(savePath+porderPath);
					if(!imgFile.exists()){
						imgFile.mkdirs();
					}
					String fileName = PandStringUtils.getUUID()+".jpg";
					file.transferTo(new File(savePath+porderPath+fileName));
					httpUrl = imgUrl+porderPath+fileName;
					logger.info("保存本次照片imgModel="+imgModel+",路径："+httpUrl);
					List<String> baseStrList = Lists.newArrayList();
					baseStrList.add(httpUrl);
					pandImagesService.savePandImages(0,4, userId, baseStrList);
					//加入注册服务器
					faceRegister(savePath+porderPath+fileName,userName,userId);
				} catch (Exception e) {    
					logger.error("人脸图片上传异常"+e.getMessage(),e);
				} 
			}
			if(isBlank(httpUrl)){
				return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
			}
			return PandResponseUtil.printJson("上传成功",userId);
		} catch (Exception e) {
			logger.error("人脸图片流上传异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	/**
	 * 本地照片流注册到人脸比对中心
	 * @param localFileName
	 * @return
	 */
	private String faceRegister(String localFileName,String userName,String userId){
		String groupId = null;
		try {
			String url = faceHttp+"face/face_register";//注册接口
			Map<String,ContentBody> reqParam = new HashMap<String,ContentBody>();
			reqParam.put("file", new FileBody(new File(localFileName)));
			reqParam.put("name", new StringBody(userName, ContentType.APPLICATION_JSON));
			reqParam.put("groupId", new StringBody(userId, ContentType.MULTIPART_FORM_DATA));
			String httpRes = HttpUtils.postFileMultiPart(url,reqParam);
			logger.info("人脸注册接口返回结果："+httpRes);//{"result":1,"data":"085ba5ec70ce4d71a5b8d2494843d17f","message":"人脸注册成功"}
			if(isBlank(httpRes)){
				return groupId;
			}
			JSONObject obj = JSON.parseObject(httpRes);
			Integer status =  (Integer) obj.get("result");
			if(status==null){
				return groupId;
			}else if(status==1){
				groupId = (String) obj.get("data");
			}
			
		} catch (Exception e) {
			logger.error("人脸注册异常"+e.getMessage(),e);
		}
		return groupId;
	}
	/**
	 * 本地照片流注册到人脸比对中心
	 * @param localFileName
	 * @return
	 */
	private Map<String, Object> faceCompare(String localFileName,String userId){
		Map<String, Object> map = Maps.newHashMap();
		try {
			String url = faceHttp+"face/aoi_face_compare";//注册接口
			Map<String,ContentBody> reqParam = new HashMap<String,ContentBody>();
			reqParam.put("file", new FileBody(new File(localFileName)));
			reqParam.put("groupId", new StringBody(userId, ContentType.MULTIPART_FORM_DATA));
			String httpRes = HttpUtils.postFileMultiPart(url,reqParam);
			logger.info("人脸比对接口返回结果："+httpRes);//{"result":1,"data":"085ba5ec70ce4d71a5b8d2494843d17f","message":"人脸注册成功"}
			if(isBlank(httpRes)){
				return map;
			}
			JSONObject obj = JSON.parseObject(httpRes);
			Integer status =  (Integer) obj.get("result");
			if(status==null){
				return map;
			}else if(status==1){
				JSONObject data =  obj.getJSONObject("data");
				map.put("similarValue", data.get("similarValue"));
				map.put("name", data.get("name"));
				map.put("age", data.get("age"));
				map.put("gender", data.get("gender"));
			}/*else if(status==0){
				map.put("errorCode", obj.get("errorCode"));
				map.put("message", obj.get("message"));
			}*/
			
		} catch (Exception e) {
			logger.error("人脸比对异常"+e.getMessage(),e);
		}
		return map;
	}
	
	
	/**
	 * 人脸比对接口
	 * @param token
	 * @param files     图片数组流
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/face_compare")
	public String faceCompare(String token,@RequestParam(value="file", required=false) MultipartFile file,String userId) {
		logger.info("人脸图片上传参数 token="+token+",userId="+userId);
		if(isBlank(token) || file==null || file.isEmpty() || isBlank(userId)){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		Map<String, Object> faceCompare = null;
		try{
			String porder = "facecompare/";
			if (file != null && !file.isEmpty()) {    
				try {    
					String porderPath = porder+PandDateUtils.dateToStr(new Date(), "yyyyMMdd")+"/";
					File imgFile = new File(savePath+porderPath);
					if(!imgFile.exists()){
						imgFile.mkdirs();
					}
					String fileName = PandStringUtils.getUUID()+".jpg";
					file.transferTo(new File(savePath+porderPath+fileName));
					//照片数据比较
					faceCompare = faceCompare(savePath+porderPath+fileName,userId);
					if(faceCompare==null || faceCompare.isEmpty()){
						return PandResponseUtil.printFailJson(PandResponseUtil.no_faceimg,"照片不匹配", null);
					}
				} catch (Exception e) {    
					logger.error("人脸图片上传异常"+e.getMessage(),e);
				} 
			}
			if(faceCompare==null){
				return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
			}
			return PandResponseUtil.printJson("对比成功",faceCompare);
		} catch (Exception e) {
			logger.error("人脸图片流上传异常"+e.getMessage(),e);
			return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
		}
	}
	
	
}
