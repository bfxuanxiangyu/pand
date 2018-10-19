package com.weeds.pand.service.system.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.weeds.pand.service.system.domain.PandImages;
import com.weeds.pand.service.system.mapper.PandImagesJpaDao;
import com.weeds.pand.service.system.mapper.PandImagesMapper;
import com.weeds.pand.service.system.service.PandImagesService;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandStringUtils;

@Service
public class PandImagesServiceImpl implements PandImagesService{
	
	Logger logger = LoggerFactory.getLogger(PandImagesServiceImpl.class);
	@Resource
	private PandImagesJpaDao pandImagesJpaDao;
	@Resource
	private PandImagesMapper pandImagesMapper;
	
	@Value("${img.savePath}")
	private String savePath;
	@Value("${img.imgUrl}")
	private String imgUrl;
	
	@Override
	public List<PandImages> listPandImages(Map<String, Object> parameters) {
		return pandImagesMapper.getPandImagesList(parameters);
	}

	@Override
	public void deletePandImages(String id) {
		pandImagesJpaDao.delete(id);
	}

	/**
	 * @param parameters
	 * @param imgModel  所属模块  1用户审核   2评论 3服务图片
	 * @param modelId
	 * @param imagesList 图片流集合
	 */
	@Override
	public void savePandImages(Map<String, Object> parameters,int imgModel,String modelId,List<String> imagesList) {
		
		if(imagesList==null || imagesList.isEmpty()){
			return ;
		}
		
		PandImages vo = new PandImages();
		vo.setImgModel(imgModel);
		vo.setModelId(modelId);
		String porder = "common/";
		if(imgModel==1){
			int imgType = (int) parameters.get("imgType");
			vo.setImgType(imgType);
			porder = "user/";
		}else if(imgModel==2){
			porder = "comment/";
		}else if(imgModel==3){
			porder = "service/";
		}
		for (String str : imagesList) {
			String imgUrl = saveImages(str, porder);
			//保存base64照片流
			if(PandStringUtils.isBlank(imgUrl)){
				continue ;
			}
			try {
				vo.setCreateTime(new Date());
				vo.setImgUrl(imgUrl);
				vo.setImgStatus(0);
				pandImagesJpaDao.save(vo);
			} catch (Exception e) {
				logger.error(imgModel+"类型图片保存异常"+e.getMessage(),e);
			}
		}
	}

	/**
	 * 保存图片
	 * @param str
	 * @param porder
	 * @return
	 */
	private String saveImages(String str,String porder){
		String httpStr = null;
		try {
			String porderPath = porder+PandDateUtils.dateToStr(new Date(), "yyyyMMdd")+File.separator;
			File file = new File(savePath+porderPath);
			if(!file.exists()){
				file.mkdirs();
			}
			String fileName = PandDateUtils.dateToStr(new Date(), "yyyyMMddHHmmss")+".png";
			byte[] bytes = Base64Utils.decode(str.getBytes());
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
	
	public static void main(String[] args) {
		try {
			String path = "d://opt//1.png";
			InputStream input = new FileInputStream(new File(path));
			byte [] data = new byte[input.available()];
			input.read(data);
			input.close();
			System.out.println(Base64Utils.encodeToString(data));
			
			
			String str = "";
			String porderPath = "/head/"+PandDateUtils.dateToStr(new Date(), "yyyyMMdd")+File.separator;
			File file = new File("d://opt//"+porderPath);
			if(!file.exists()){
				file.mkdirs();
			}
			String fileName = PandDateUtils.dateToStr(new Date(), "yyyyMMddHHmmss")+".png";
			byte[] bytes = Base64Utils.decode(str.getBytes());
			OutputStream out = new FileOutputStream("d://opt//"+porderPath+fileName);
			out.write(bytes);
			out.flush();
			out.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
