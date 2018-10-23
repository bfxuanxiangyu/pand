package com.weeds.pand.service.pandcore.service;

import java.util.List;
import java.util.Map;

import com.weeds.pand.service.pandcore.domain.PandShop;

/**
 * service接口
 * 由GenEntityMysql类自动生成
 * Tue Oct 23 12:35:31 CST 2018
 * @xuanxy
 */ 
public interface PandShopService{
	
	void savePandShop(PandShop pandShop);
	
	List<PandShop> getPandShopList(Map<String, Object> parameters);
	
	PandShop getPandShopObject(Map<String, Object> parameters);
}

