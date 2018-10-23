package com.weeds.pand.service.pandcore.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.weeds.pand.service.pandcore.domain.PandService;
import com.weeds.pand.service.pandcore.mapper.PandServiceJpaDao;
import com.weeds.pand.service.pandcore.mapper.PandServiceMapper;
import com.weeds.pand.service.pandcore.service.PandServiceService; 


/**
 * service实现类
 * 由GenEntityMysql类自动生成
 * Mon Oct 22 16:04:37 CST 2018
 * @xuanxy
 */ 
@Service
public class PandServiceServiceImpl implements PandServiceService{

	@Resource
	private PandServiceJpaDao pandServiceJpaDao;
	@Resource
	private PandServiceMapper pandServiceMapper;
	
	@Override
	public void savePandService(PandService ps) {
		ps.setUpdateTime(new Date());
		pandServiceJpaDao.save(ps);
	}

	@Override
	public List<PandService> getPandServiceList(Map<String, Object> parameters) {
		return pandServiceMapper.getPandServiceList(parameters);
	}

	@Override
	public PandService getPandServiceObj(Map<String, Object> parameters) {
		return pandServiceMapper.getPandServiceObject(parameters);
	}
	
}

