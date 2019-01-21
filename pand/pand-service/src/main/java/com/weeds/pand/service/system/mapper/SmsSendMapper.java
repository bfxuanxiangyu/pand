package com.weeds.pand.service.system.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper; 

import com.weeds.pand.service.system.domain.SmsSend; 
/**
 * mapper接口公共接口
 * 由GenEntityMysql类自动生成
 * Mon Jan 21 18:46:33 CST 2019
 * @xuanxy
 */ 
@Mapper
public interface SmsSendMapper {

	/**
	* 根据条件获取所有SmsSend集合
	*/ 
	List<SmsSend>  getSmsSendList(Map<String, Object> parameters);

	SmsSend  getSmsSendByPhone(String phone);

	/**
	* 根据条件获取所有SmsSend总数
	*/ 
	int  getSmsSendCount(Map<String, Object> parameters);
}

