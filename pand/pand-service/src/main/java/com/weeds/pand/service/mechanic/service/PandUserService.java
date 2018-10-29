package com.weeds.pand.service.mechanic.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.weeds.pand.service.mechanic.domain.PandUser;
import com.weeds.pand.service.pandcore.pagevo.PandUserQueryParam;

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
	
	PageInfo<PandUser> selectAllForPage(PandUserQueryParam params);

}

