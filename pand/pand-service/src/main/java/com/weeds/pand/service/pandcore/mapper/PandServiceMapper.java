package com.weeds.pand.service.pandcore.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper; 

import com.weeds.pand.service.pandcore.domain.PandService; 
/**
 * mapper接口公共接口
 * 由GenEntityMysql类自动生成
 * Mon Oct 22 16:04:37 CST 2018
 * @xuanxy
 */ 
@Mapper
public interface PandServiceMapper {

	/**
	* 根据条件获取所有PandService集合
	*/ 
	List<PandService>  getPandServiceList(Map<String, Object> parameters);
	List<PandService>  getPandServiceListQrNull();

	PandService  getPandServiceObject(Map<String, Object> parameters);

	/**
	* 根据条件获取所有PandService总数
	*/ 
	int  getPandServiceCount(Map<String, Object> parameters);
	
	void deleteCollectService(String serviceId);
}

