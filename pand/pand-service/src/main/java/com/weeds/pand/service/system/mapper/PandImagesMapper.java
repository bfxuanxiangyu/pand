package com.weeds.pand.service.system.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper; 

import com.weeds.pand.service.system.domain.PandImages; 
/**
 * mapper接口公共接口
 * 由GenEntityMysql类自动生成
 * Thu Oct 18 15:03:41 CST 2018
 * @xuanxy
 */ 
@Mapper
public interface PandImagesMapper {

	/**
	* 根据条件获取所有PandImages集合
	*/ 
	List<PandImages>  getPandImagesList(Map<String, Object> parameters);


	/**
	* 根据条件获取所有PandImages总数
	*/ 
	int  getPandImagesCount(Map<String, Object> parameters);
	
	void deletePandImagesObj(Map<String, Object> parameters);
}

