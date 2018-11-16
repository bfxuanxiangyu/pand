package com.weeds.pand.service.pandcore.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper; 

import com.weeds.pand.service.pandcore.domain.PandUserComment; 
/**
 * mapper接口公共接口
 * 由GenEntityMysql类自动生成
 * Thu Nov 15 12:31:50 CST 2018
 * @xuanxy
 */ 
@Mapper
public interface PandUserCommentMapper {

	/**
	* 根据条件获取所有PandUserComment集合
	*/ 
	List<PandUserComment>  getPandUserCommentList(Map<String, Object> parameters);


	/**
	* 根据条件获取所有PandUserComment总数
	*/ 
	int  getPandUserCommentCount(Map<String, Object> parameters);
}

