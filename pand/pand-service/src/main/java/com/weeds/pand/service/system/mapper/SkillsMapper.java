package com.weeds.pand.service.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.weeds.pand.service.system.domain.Skills;

@Mapper
public interface SkillsMapper {

	List<Skills> selectAll(Map<String, Object> parameters);
	
    List<Skills> selectAllByShopIds(Map<String, Object> parameters);

}