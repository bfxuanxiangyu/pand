package com.weeds.pand.service.pandcore.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.weeds.pand.service.pandcore.domain.PandShop;
import com.weeds.pand.service.pandcore.mapper.PandShopJpaDao;
import com.weeds.pand.service.pandcore.mapper.PandShopMapper;
import com.weeds.pand.service.pandcore.service.PandShopService;
import com.weeds.pand.service.system.domain.Skills;
import com.weeds.pand.service.system.mapper.SkillsMapper; 


/**
 * service实现类
 * 由GenEntityMysql类自动生成
 * Tue Oct 23 12:35:31 CST 2018
 * @xuanxy
 */ 
@Service
public class PandShopServiceImpl implements PandShopService{
	
	@Resource
	private PandShopJpaDao pandShopJpaDao;
	
	@Resource
	private PandShopMapper pandShopMapper; 
	@Resource
	private SkillsMapper skillsMapper;

	@Override
	public void savePandShop(PandShop pandShop) {
		pandShop.setUpdateTime(new Date());
		pandShopJpaDao.save(pandShop);
	}

	@Override
	public List<PandShop> getPandShopList(Map<String, Object> parameters) {
		return pandShopMapper.getPandShopList(parameters);
	}

	@Override
	public PandShop getPandShopObject(Map<String, Object> parameters) {
		
		PandShop object = pandShopMapper.getPandShopObject(parameters);
		//获取技能列表
		if(object != null){
			List<Skills> skills = skillsMapper.selectAllByShopIds(parameters);
			if(skills!= null){
				object.setSkills(skills);
			}
		}
		
		
		return object;
	}

}

