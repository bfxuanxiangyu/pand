package com.weeds.pand.service.mechanic.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weeds.pand.service.mechanic.domain.PandUser;
import com.weeds.pand.service.mechanic.mapper.PandUserJpaDao;
import com.weeds.pand.service.mechanic.mapper.PandUserMapper;
import com.weeds.pand.service.mechanic.service.PandUserService;
import com.weeds.pand.service.pandcore.domain.PandAuditLog;
import com.weeds.pand.service.pandcore.mapper.PandAuditLogJpaDao;
import com.weeds.pand.service.pandcore.mapper.PandAuditLogMapper;
import com.weeds.pand.service.pandcore.pagevo.PandUserQueryParam; 


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
	@Resource
	private PandAuditLogJpaDao pandAuditLogJpaDao;
	@Resource
	private PandAuditLogMapper pandAuditLogMapper;
	
	
	@Override
	public void savePandUser(PandUser pandUser) {
		pandUser.setUpdateTime(new Date());
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

	@Override
	public PageInfo<PandUser> selectAllForPage(PandUserQueryParam params) {
		PageHelper.offsetPage(params.getStart(), params.getLength());
		List<PandUser> pandUserListPage = pandUserMapper.getPandUserListPage(params);
		PageInfo<PandUser> page = new PageInfo<PandUser>(pandUserListPage);
		return page;
	}

	@Override
	public PandUser getPandUserObjById(String id) {
		return pandUserJpaDao.findOne(id);
	}

	@Override
	public void savePandAuditLog(PandAuditLog pandAuditLog) {
		pandAuditLog.setCreateTime(new Date());
		pandAuditLog.setUpdateTime(new Date());
		pandAuditLogJpaDao.save(pandAuditLog);
	}

	@Override
	public List<PandAuditLog> selectPandAuditLogList(Map<String, Object> parameters) {
		return pandAuditLogMapper.getPandAuditLogList(parameters);
	}

	@Override
	public PandAuditLog getPandAuditLogObj(Map<String, Object> parameters) {
		return pandAuditLogMapper.getPandAuditLogObj(parameters);
	}

}

