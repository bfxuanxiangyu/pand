package com.weeds.pand.service.pandcore.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper; 

import com.weeds.pand.service.pandcore.domain.PandAuditLog; 
/**
 * mapper接口公共接口
 * 由GenEntityMysql类自动生成
 * Tue Oct 30 15:53:07 CST 2018
 * @xuanxy
 */ 
@Mapper
public interface PandAuditLogMapper {

	/**
	* 根据条件获取所有PandAuditLog集合
	*/ 
	List<PandAuditLog>  getPandAuditLogList(Map<String, Object> parameters);


	/**
	* 根据条件获取所有PandAuditLog总数
	*/ 
	int  getPandAuditLogCount(Map<String, Object> parameters);
	
	PandAuditLog getPandAuditLogObj(Map<String, Object> parameters);
}

