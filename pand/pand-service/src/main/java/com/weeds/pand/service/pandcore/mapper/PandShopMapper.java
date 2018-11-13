package com.weeds.pand.service.pandcore.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper; 

import com.weeds.pand.service.pandcore.domain.PandShop; 
/**
 * mapper接口公共接口
 * 由GenEntityMysql类自动生成
 * Tue Oct 23 12:35:31 CST 2018
 * @xuanxy
 */ 
@Mapper
public interface PandShopMapper {

	/**
	* 根据条件获取所有PandShop集合
	*/ 
	List<PandShop>  getPandShopList(Map<String, Object> parameters);

	PandShop  getPandShopObject(Map<String, Object> parameters);
	
	PandShop  getPandShopObjectByServiceId(String serviceId);

	/**
	* 根据条件获取所有PandShop总数
	*/ 
	int  getPandShopCount(Map<String, Object> parameters);
}

