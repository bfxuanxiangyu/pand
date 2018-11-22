package com.weeds.pand.service.pandcore.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
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

	private Logger logger = LoggerFactory.getLogger(PandServiceServiceImpl.class);
	@Resource
	private PandServiceJpaDao pandServiceJpaDao;
	@Resource
	private PandServiceMapper pandServiceMapper;
	
	@Override
	public void savePandService(PandService ps) {
		ps.setUpdateTime(new Date());
		pandServiceJpaDao.save(ps);
		
		try {
			if(ps.getServiceStatus()==4 || ps.getServiceStatus()==5){
				pandServiceMapper.deleteCollectService(ps.getId());
			}
		} catch (Exception e) {
			logger.error("删除已经被收藏的服务异常"+e.getMessage(),e);
		}
		
	}

	@Override
	public List<PandService> getPandServiceList(Map<String, Object> parameters) {
		return pandServiceMapper.getPandServiceList(parameters);
	}

	@Override
	public PandService getPandServiceObj(Map<String, Object> parameters) {
		return pandServiceMapper.getPandServiceObject(parameters);
	}

	@Override
	public PandService getPandServiceById(String id) {
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("id", id);
		return pandServiceMapper.getPandServiceObject(parameters);
	}
	
}

