package com.weeds.pand.service.pandcore.service;

import java.util.List;
import java.util.Map;

import com.weeds.pand.service.pandcore.domain.PandService;

/**
 * service接口
 * 由GenEntityMysql类自动生成
 * Mon Oct 22 16:04:37 CST 2018
 * @xuanxy
 */ 
public interface PandServiceService{
	
	void savePandService(PandService ps);//发布 编辑服务
	
	List<PandService> getPandServiceList(Map<String, Object> parameters);//获取服务列表
	
	PandService getPandServiceObj(Map<String, Object> parameters);//获取服务详情
	
	PandService getPandServiceById(String id);//获取服务详情
	
}

