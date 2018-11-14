package com.weeds.pand.service.pandcore.service;

import java.util.List;
import java.util.Map;

import com.weeds.pand.service.pandcore.domain.PandUserAddress;

/**
 * service接口
 * 由GenEntityMysql类自动生成
 * Wed Nov 14 12:48:47 CST 2018
 * @xuanxy
 */ 
public interface PandUserAddressService{
	
	void savePandUserAddress(PandUserAddress address);
	
	List<PandUserAddress> getPandUserAddressList(Map<String, Object> parameters);

	PandUserAddress getPandUserAddressById(String id);
	
}

