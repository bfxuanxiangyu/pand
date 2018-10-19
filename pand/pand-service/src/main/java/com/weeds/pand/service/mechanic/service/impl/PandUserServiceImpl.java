package com.weeds.pand.service.mechanic.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.weeds.pand.service.mechanic.domain.PandUser;
import com.weeds.pand.service.mechanic.mapper.PandUserJpaDao;
import com.weeds.pand.service.mechanic.mapper.PandUserMapper;
import com.weeds.pand.service.mechanic.service.PandUserService; 


/**
 * service实现类
 * 由GenEntityMysql类自动生成
 * Thu Oct 18 16:59:05 CST 2018
 * @xuanxy
 */ 
@Service
public class PandUserServiceImpl implements PandUserService{
	
	private Logger logger = LoggerFactory.getLogger(PandUserServiceImpl.class);
	
	@Resource
	private PandUserJpaDao pandUserJpaDao;
	@Resource
	private PandUserMapper pandUserMapper;

	@Override
	public void savePandUser(PandUser pandUser) {
		pandUserJpaDao.save(pandUser);
	}

	@Override
	public PandUser getPandUserObj(Map<String, Object> parameters) {
		return pandUserMapper.getPandUserObj(parameters);
	}

	@Override
	public List<PandUser> selectAll(Map<String, Object> parameters) {
		return pandUserMapper.getPandUserList(parameters);
	}

}

