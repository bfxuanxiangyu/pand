package com.weeds.pand.api.system.rest;

import static com.weeds.pand.utils.PandStringUtils.isBlank;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
	@Value("${img.imgUrl}")
	private String imgUrl;
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
		logger.info("删除地址参数:"+token);
		if(isBlank(token) || file==null || file.isEmpty()){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		String httpUrl = null;
		try{
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
	
	
}
