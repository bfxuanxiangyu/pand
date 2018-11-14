package com.weeds.pand.service.pandcore.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.weeds.pand.service.pandcore.domain.PandUserAddress;
import com.weeds.pand.service.pandcore.mapper.PandUserAddressJpaDao;
import com.weeds.pand.service.pandcore.mapper.PandUserAddressMapper;
import com.weeds.pand.service.pandcore.service.PandUserAddressService; 


/**
 * service实现类
 * 由GenEntityMysql类自动生成
 * Wed Nov 14 12:48:47 CST 2018
 * @xuanxy
 */ 
@Service
public class PandUserAddressServiceImpl implements PandUserAddressService{
	
	@Resource
	private PandUserAddressJpaDao pandUserAddressJpaDao;
	@Resource
	private PandUserAddressMapper pandUserAddressMapper;

	@Override
	public void savePandUserAddress(PandUserAddress address) {
		address.setUpdateTime(new Date());
		pandUserAddressJpaDao.save(address);
	}

	@Override
	public List<PandUserAddress> getPandUserAddressList(Map<String, Object> parameters) {
		return pandUserAddressMapper.getPandUserAddressList(parameters);
	}

	@Override
	public PandUserAddress getPandUserAddressById(String id) {
		return pandUserAddressJpaDao.findOne(id);
	}

}

