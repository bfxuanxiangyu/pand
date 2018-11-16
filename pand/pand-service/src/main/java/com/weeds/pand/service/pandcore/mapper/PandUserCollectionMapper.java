package com.weeds.pand.service.pandcore.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper; 

import com.weeds.pand.service.pandcore.domain.PandUserCollection; 
/**
 * mapper接口公共接口
 * 由GenEntityMysql类自动生成
 * Fri Nov 16 12:39:33 CST 2018
 * @xuanxy
 */ 
@Mapper
public interface PandUserCollectionMapper {

	/**
	* 根据条件获取所有PandUserCollection集合
	*/ 
	List<PandUserCollection>  getPandUserCollectionList(Map<String, Object> parameters);


	/**
	* 根据条件获取所有PandUserCollection总数
	*/ 
	int  getPandUserCollectionCount(Map<String, Object> parameters);
}

