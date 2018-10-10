package com.weeds.pand.auth.bi.service;

import java.util.List;

import com.weeds.pand.auth.bi.domain.TableVo;

public interface DbService {
	
	List<TableVo> getTables(String sql);
	
	List<String[]> resultSet(String sql);
	
	void execute(String sql);
	
}
