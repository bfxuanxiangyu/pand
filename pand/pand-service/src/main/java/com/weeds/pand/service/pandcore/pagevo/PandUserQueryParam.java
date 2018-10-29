/*
 * Copyright (C) 2017 Shanghai sinnren soft Co., Ltd
 *
 * All copyrights reserved by Shanghai sinnren.
 * Any copying, transferring or any other usage is prohibited.
 * Or else, Shanghai sinnren possesses the right to require legal 
 * responsibilities from the violator.
 * All third-party contributions are distributed under license by
 * Shanghai sinnren soft Co., Ltd.
 */
package com.weeds.pand.service.pandcore.pagevo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.weeds.pand.core.web.DataTablesParams;

/**
 * @author Jetory
 * @date 2017年10月18日 下午3:34:42	
 */
public class PandUserQueryParam extends DataTablesParams{

	private static final long serialVersionUID = 8910076059891771782L;

	private String startTime;
	
	private String endTime;
	
	private Integer userType;
	
	private Integer userStatus;
	
	private String contents;

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	

	
}
