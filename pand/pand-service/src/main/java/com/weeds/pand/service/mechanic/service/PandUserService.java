package com.weeds.pand.service.mechanic.service;

import java.util.List;
import java.util.Map;

import com.weeds.pand.service.mechanic.domain.PandUser;

/**
 * service接口
 * 由GenEntityMysql类自动生成
 * Thu Oct 18 16:59:05 CST 2018
 * @xuanxy
 */ 
public interface PandUserService{
	
	void savePandUser(PandUser pandUser);
	
	PandUser getPandUserObj(Map<String, Object> parameters);
	
	List<PandUser> selectAll(Map<String, Object> parameters);

}

