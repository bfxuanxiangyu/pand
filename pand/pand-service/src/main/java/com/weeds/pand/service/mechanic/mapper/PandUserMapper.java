package com.weeds.pand.service.mechanic.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper; 

import com.weeds.pand.service.mechanic.domain.PandUser; 
/**
 * mapper接口公共接口
 * 由GenEntityMysql类自动生成
 * Thu Oct 18 16:59:05 CST 2018
 * @xuanxy
 */ 
@Mapper
public interface PandUserMapper {

	/**
	* 根据条件获取所有PandUser集合
	*/ 
	List<PandUser>  getPandUserList(Map<String, Object> parameters);

	PandUser getPandUserObj(Map<String, Object> parameters);

	/**
	* 根据条件获取所有PandUser总数
	*/ 
	int  getPandUserCount(Map<String, Object> parameters);
}

