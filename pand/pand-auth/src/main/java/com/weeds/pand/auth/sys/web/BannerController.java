package com.weeds.pand.auth.sys.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.weeds.pand.service.system.domain.Banner;
import com.weeds.pand.service.system.mapper.BannerJpaDao;
import com.weeds.pand.service.system.mapper.BannerMapper;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandStringUtils;

@Controller
@RequestMapping("/sys")
public class BannerController {
	
	private Logger logger = LoggerFactory.getLogger(BannerController.class);
	
	@Value("${img.savePath}")
	private String savePath;
	@Value("${img.imgUrl}")
	private String imgUrl;
	
	@Resource
	private BannerMapper bannerMapper;
	@Resource
	private BannerJpaDao bannerJpaDao;
	
	@RequestMapping("/banner_list")
	public ModelAndView bannerList() {
		ModelAndView view = new ModelAndView();
		List<Banner> bannerList = bannerMapper.selectAll(Maps.newHashMap());
		view.addObject("bannerList", bannerList);
		view.setViewName("sys/banner_list");
		return view;
	}
	
	@RequestMapping("/banner_add")
	public ModelAndView bannerAdd() {
		ModelAndView view = new ModelAndView();
		view.setViewName("sys/addbanner");
		return view;
	}
	@RequestMapping("/banner_update")
	public ModelAndView bannerUpdate(String id) {
		ModelAndView view = new ModelAndView();
		Banner banner = bannerJpaDao.findOne(id);
		view.addObject("banner", banner);
		view.setViewName("sys/updatebanner");
		return view;
	}
	@RequestMapping("/banner_status")
	public ModelAndView bannerStatus(String id,Integer status) {
		Banner banner = bannerJpaDao.findOne(id);
		banner.setStatus(status);
		banner.setUpdateTime(new Date());
		bannerJpaDao.save(banner);
		return bannerList();
	}
	@RequestMapping("/banner_save")
	public ModelAndView bannerSave(@RequestParam(value="file", required=false) MultipartFile file,Integer imgOrder,Integer status,String id) {
		
		String iu = "";
		if (file != null && !file.isEmpty()) {    
			try {    
				String fileName = PandDateUtils.dateToStr(new Date(), "yyyyMMddHHmmss")+".png";
				BufferedOutputStream out = new BufferedOutputStream(    
						new FileOutputStream(new File(savePath+fileName)));    
				System.out.println(file.getName());  
				out.write(file.getBytes());    
				out.flush();    
				out.close(); 
				iu = imgUrl+fileName;
			} catch (Exception e) {    
				logger.error("banner图片上传异常"+e.getMessage(),e);
			} 
		}
		//保存数据
		Banner banner = new Banner();
		banner.setImgOrder(imgOrder);
		banner.setStatus(status);
		banner.setUpdateTime(new Date());
		if(PandStringUtils.isBlank(id)){
			if(PandStringUtils.isBlank(iu)){
				return bannerList();
			}
			banner.setCreateTime(new Date());
			banner.setImgUrl(iu);
		}else{
			Banner old = bannerJpaDao.findOne(id);
			banner.setId(id);
			banner.setCreateTime(old.getCreateTime());
			if(PandStringUtils.isBlank(iu)){
				banner.setImgUrl(old.getImgUrl());
			}else{
				banner.setImgUrl(iu);
			}
		}
		bannerJpaDao.save(banner);
		return bannerList();
	}

}
