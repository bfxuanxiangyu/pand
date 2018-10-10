package com.weeds.pand.service.system.service;

import com.weeds.pand.service.system.domain.AskFor;
import com.weeds.pand.service.system.domain.SystemVersionInfo;

public interface SystemVersionInfoService {
	
	void saveSystemVersionInfo(SystemVersionInfo systemVersionInfo);
	
	void deleteSystemVersionInfo(String id);
	
	SystemVersionInfo getSystemVersionInfoObj(String id);
	
	void saveAskForInfo(AskFor askFor);

}
