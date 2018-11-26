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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.weeds.pand.service.system.domain.Skills;
import com.weeds.pand.service.system.mapper.SkillsJpaDao;
import com.weeds.pand.service.system.mapper.SkillsMapper;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandStringUtils;

@Controller
@RequestMapping("/sys")
public class SkillsController {
	
	private Logger logger = LoggerFactory.getLogger(SkillsController.class);
	
	@Value("${img.savePath}")
	private String savePath;
	@Value("${img.imgUrl}")
	private String imgUrl;
	
	@Resource
	private SkillsMapper skillsMapper;
	@Resource
	private SkillsJpaDao skillsJpaDao;
	
	@RequestMapping("/skills_list")
	public ModelAndView skillsList() {
		ModelAndView view = new ModelAndView();
		List<Skills> skillsList = skillsMapper.selectAll(Maps.newHashMap());
		view.addObject("skillsList", skillsList);
		view.setViewName("sys/skills_list");
		return view;
	}
	
	@RequestMapping("/skills_add")
	public ModelAndView skillsAdd() {
		ModelAndView view = new ModelAndView();
		view.setViewName("sys/addskills");
		return view;
	}
	@RequestMapping("/skills_update")
	public ModelAndView skillsUpdate(String id) {
		ModelAndView view = new ModelAndView();
		Skills skills = skillsJpaDao.findOne(id);
		view.addObject("skills", skills);
		view.setViewName("sys/updateskills");
		return view;
	}
	@RequestMapping("/skills_status")
	public ModelAndView skillsStatus(String id,Integer status) {
		if(id.equals("skill_all")){
			return skillsList();
		}
		Skills skills = skillsJpaDao.findOne(id);
		skills.setStatus(status);
		skills.setUpdateTime(new Date());
		skillsJpaDao.save(skills);
		return skillsList();
	}
	@RequestMapping("/skills_save")
	public ModelAndView skillsSave(@RequestParam(value="file", required=false) MultipartFile file
			,Integer iconOrder,Integer status,String id,String content,String title) {
		
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
				logger.error("skills图片上传异常"+e.getMessage(),e);
			} 
		}
		//保存数据
		Skills skills = new Skills();
		skills.setIconOrder(iconOrder);
		skills.setStatus(status);
		skills.setUpdateTime(new Date());
		skills.setContent(content);
		skills.setTitle(title);
		if(PandStringUtils.isBlank(id)){
			if(PandStringUtils.isBlank(iu)){
				return skillsList();
			}
			skills.setCreateTime(new Date());
			skills.setIconUrl(iu);
		}else{
			Skills old = skillsJpaDao.findOne(id);
			skills.setId(id);
			skills.setCreateTime(old.getCreateTime());
			if(PandStringUtils.isBlank(iu)){
				skills.setIconUrl(old.getIconUrl());
			}else{
				skills.setIconUrl(iu);
			}
		}
		skillsJpaDao.save(skills);
		return skillsList();
	}

}
