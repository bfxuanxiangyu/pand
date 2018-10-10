package com.weeds.pand.service.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.weeds.pand.service.system.domain.AskFor;
import com.weeds.pand.service.system.domain.SystemVersionInfo;
import com.weeds.pand.service.system.mapper.AskForJpaDao;
import com.weeds.pand.service.system.mapper.SystemVersionInfoJpaDao;
import com.weeds.pand.service.system.service.SystemVersionInfoService;

@Service
public class SystemVersionInfoServiceImpl implements SystemVersionInfoService{

	@Resource
	private SystemVersionInfoJpaDao systemVersionInfoJpaDao;
	@Resource
	private AskForJpaDao askForJpaDao;
	
	@Override
	public void saveSystemVersionInfo(SystemVersionInfo systemVersionInfo) {
		systemVersionInfoJpaDao.save(systemVersionInfo);
	}

	@Override
	public void deleteSystemVersionInfo(String id) {
		systemVersionInfoJpaDao.delete(id);
	}

	@Override
	public SystemVersionInfo getSystemVersionInfoObj(String id) {
		return systemVersionInfoJpaDao.findOne(id);
	}

	@Override
	public void saveAskForInfo(AskFor askFor) {
		askForJpaDao.save(askFor);
	}

}
