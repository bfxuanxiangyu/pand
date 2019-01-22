package com.weeds.pand.service.system.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper; 

import com.weeds.pand.service.system.domain.RongcloudToken; 
/**
 * mapper接口公共接口
 * 由GenEntityMysql类自动生成
 * Tue Jan 22 10:29:31 CST 2019
 * @xuanxy
 */ 
@Mapper
public interface RongcloudTokenMapper {

	/**
	* 根据条件获取所有RongcloudToken集合
	*/ 
	List<RongcloudToken>  getRongcloudTokenList(Map<String, Object> parameters);


	/**
	* 根据条件获取所有RongcloudToken总数
	*/ 
	int  getRongcloudTokenCount(Map<String, Object> parameters);
}

