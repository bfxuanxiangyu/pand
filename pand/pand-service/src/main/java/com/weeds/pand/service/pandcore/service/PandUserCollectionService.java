package com.weeds.pand.service.pandcore.service;


import java.util.List;
import java.util.Map;

import com.weeds.pand.service.pandcore.domain.PandUserCollection;

/**
 * service接口
 * 由GenEntityMysql类自动生成
 * Fri Nov 16 12:39:33 CST 2018
 * @xuanxy
 */ 
public interface PandUserCollectionService{


      List<PandUserCollection> getPandUserCollectionList(Map<String, Object> parameters);

      PandUserCollection getPandUserCollectionById(String id);

      void savePandUserCollection(PandUserCollection obj);

}

