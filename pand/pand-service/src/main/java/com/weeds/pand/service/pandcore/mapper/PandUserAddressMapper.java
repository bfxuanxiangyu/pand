package com.weeds.pand.service.pandcore.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper; 

import com.weeds.pand.service.pandcore.domain.PandUserAddress; 
/**
 * mapper接口公共接口
 * 由GenEntityMysql类自动生成
 * Wed Nov 14 12:48:47 CST 2018
 * @xuanxy
 */ 
@Mapper
public interface PandUserAddressMapper {

	/**
	* 根据条件获取所有PandUserAddress集合
	*/ 
	List<PandUserAddress>  getPandUserAddressList(Map<String, Object> parameters);


	/**
	* 根据条件获取所有PandUserAddress总数
	*/ 
	int  getPandUserAddressCount(Map<String, Object> parameters);
}

