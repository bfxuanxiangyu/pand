package com.weeds.pand.service.pandcore.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;


import javax.annotation.Resource;


import org.springframework.stereotype.Service;

import com.weeds.pand.service.pandcore.service.PandUserCollectionService; 
import com.weeds.pand.service.pandcore.domain.PandUserCollection;
import com.weeds.pand.service.pandcore.mapper.PandUserCollectionMapper;
import com.weeds.pand.service.pandcore.mapper.PandUserCollectionJpaDao;


/**
 * service实现类
 * 由GenEntityMysql类自动生成
 * Fri Nov 16 12:39:33 CST 2018
 * @xuanxy
 */ 
@Service
public class PandUserCollectionServiceImpl implements PandUserCollectionService{


      @Resource
      private PandUserCollectionJpaDao pandUserCollectionJpaDao;
      @Resource
      private PandUserCollectionMapper pandUserCollectionMapper;


      @Override
      public List<PandUserCollection> getPandUserCollectionList(Map<String, Object> parameters) {
      		return pandUserCollectionMapper.getPandUserCollectionList(parameters);
      }


      @Override
      public PandUserCollection getPandUserCollectionById(String id) {
      		return pandUserCollectionJpaDao.findOne(id);
      }


      @Override
      public void savePandUserCollection(PandUserCollection obj) {
      		obj.setUpdateTime(new Date());
      		pandUserCollectionJpaDao.save(obj);
      }


}

