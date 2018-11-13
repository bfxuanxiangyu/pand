package com.weeds.pand.service.pandcore.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper; 

import com.weeds.pand.service.pandcore.domain.PandOrder; 
/**
 * mapper接口公共接口
 * 由GenEntityMysql类自动生成
 * Tue Nov 13 14:26:25 CST 2018
 * @xuanxy
 */ 
@Mapper
public interface PandOrderMapper {

	/**
	* 根据条件获取所有PandOrder集合
	*/ 
	List<PandOrder>  getPandOrderList(Map<String, Object> parameters);


	/**
	* 根据条件获取所有PandOrder总数
	*/ 
	int  getPandOrderCount(Map<String, Object> parameters);
	
	Long getMaxOrderNumNowDay(String dateStr);
}

