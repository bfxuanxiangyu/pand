package com.weeds.pand.service.pandcore.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.weeds.pand.service.pandcore.domain.PandOrder;
import com.weeds.pand.service.pandcore.domain.PandShop;
import com.weeds.pand.service.pandcore.pagevo.PandOrderQueryParam;

/**
 * service接口
 * 由GenEntityMysql类自动生成
 * Tue Nov 13 14:26:25 CST 2018
 * @xuanxy
 */ 
public interface PandOrderService{
	
	void savePandOrder(PandOrder pandOrder);
	
	List<PandOrder> getPandOrderList(Map<String, Object>  parameters);
	
	PandShop getPandShopObjByServiceId(String serviceId);
	
	PandOrder getPandOrderById(String id);
	
	Long getMaxPandOrder(String nowDay);//获取当前日期下最大值
	
	PageInfo<PandOrder> selectAllForPage(PandOrderQueryParam params);

}

