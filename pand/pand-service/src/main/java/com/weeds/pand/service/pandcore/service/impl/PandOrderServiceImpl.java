package com.weeds.pand.service.pandcore.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.weeds.pand.service.pandcore.domain.PandOrder;
import com.weeds.pand.service.pandcore.domain.PandShop;
import com.weeds.pand.service.pandcore.mapper.PandOrderJpaDao;
import com.weeds.pand.service.pandcore.mapper.PandOrderMapper;
import com.weeds.pand.service.pandcore.mapper.PandShopMapper;
import com.weeds.pand.service.pandcore.service.PandOrderService; 


/**
 * service实现类
 * 由GenEntityMysql类自动生成
 * Tue Nov 13 14:26:25 CST 2018
 * @xuanxy
 */ 
@Service
public class PandOrderServiceImpl implements PandOrderService{
	
	@Resource
	private PandOrderJpaDao pandOrderJpaDao;
	@Resource
	private PandOrderMapper pandOrderMapper;
	@Resource
	private PandShopMapper pandShopMapper;
	
	@Override
	public void savePandOrder(PandOrder pandOrder) {
		pandOrder.setUpdateTime(new Date());
		pandOrderJpaDao.save(pandOrder);
	}

	@Override
	public List<PandOrder> getPandOrderList(Map<String, Object> parameters) {
		return pandOrderMapper.getPandOrderList(parameters);
	}

	@Override
	public PandShop getPandShopObjByServiceId(String serviceId) {
		return pandShopMapper.getPandShopObjectByServiceId(serviceId);
	}

	@Override
	public PandOrder getPandOrderById(String id) {
		return pandOrderJpaDao.findOne(id);
	}

	@Override
	public Long getMaxPandOrder(String dateStr) {
		return pandOrderMapper.getMaxOrderNumNowDay(dateStr);
	}

}

