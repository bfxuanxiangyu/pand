package com.weeds.pand.service.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.weeds.pand.service.system.domain.Banner;

@Mapper
public interface BannerMapper {

    List<Banner> selectAll(Map<String, Object> parameters);

}