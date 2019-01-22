package com.weeds.pand.service.system.service;


import java.util.List;
import java.util.Map;

import com.weeds.pand.service.system.domain.RongcloudToken;

/**
 * service接口
 * 由GenEntityMysql类自动生成
 * Tue Jan 22 10:29:32 CST 2019
 * @xuanxy
 */ 
public interface RongcloudTokenService{


      List<RongcloudToken> getRongcloudTokenList(Map<String, Object> parameters);
      
      RongcloudToken getRongcloudTokenByOrder(Map<String, Object> parameters);

      RongcloudToken getRongcloudTokenById(String id);

      void saveRongcloudToken(RongcloudToken obj);

}

