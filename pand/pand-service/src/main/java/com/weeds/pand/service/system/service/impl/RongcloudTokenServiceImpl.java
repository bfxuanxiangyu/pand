package com.weeds.pand.service.system.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;


import javax.annotation.Resource;


import org.springframework.stereotype.Service;

import com.weeds.pand.service.system.service.RongcloudTokenService; 
import com.weeds.pand.service.system.domain.RongcloudToken;
import com.weeds.pand.service.system.mapper.RongcloudTokenMapper;
import com.weeds.pand.service.system.mapper.RongcloudTokenJpaDao;


/**
 * service实现类
 * 由GenEntityMysql类自动生成
 * Tue Jan 22 10:29:32 CST 2019
 * @xuanxy
 */ 
@Service
public class RongcloudTokenServiceImpl implements RongcloudTokenService{


      @Resource
      private RongcloudTokenJpaDao rongcloudTokenJpaDao;
      @Resource
      private RongcloudTokenMapper rongcloudTokenMapper;


      @Override
      public List<RongcloudToken> getRongcloudTokenList(Map<String, Object> parameters) {
      		return rongcloudTokenMapper.getRongcloudTokenList(parameters);
      }

      
      @Override
      public RongcloudToken getRongcloudTokenByOrder(Map<String, Object> parameters) {
    	  List<RongcloudToken> list = rongcloudTokenMapper.getRongcloudTokenList(parameters);
    	  if(list!=null && !list.isEmpty()){
    		  return list.get(0);
    	  }
    	  return null;
      }

      @Override
      public RongcloudToken getRongcloudTokenById(String id) {
      		return rongcloudTokenJpaDao.findOne(id);
      }


      @Override
      public void saveRongcloudToken(RongcloudToken obj) {
      		obj.setUpdateTime(new Date());
      		rongcloudTokenJpaDao.save(obj);
      }


}

