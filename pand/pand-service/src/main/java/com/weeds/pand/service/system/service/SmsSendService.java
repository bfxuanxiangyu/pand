package com.weeds.pand.service.system.service;


import java.util.List;
import java.util.Map;

import com.weeds.pand.service.system.domain.SmsSend;

/**
 * service接口
 * 由GenEntityMysql类自动生成
 * Mon Jan 21 18:46:33 CST 2019
 * @xuanxy
 */ 
public interface SmsSendService{


      List<SmsSend> getSmsSendList(Map<String, Object> parameters);

      SmsSend getSmsSendByPhone(String phone);
      
      SmsSend getSmsSendById(String id);

      void saveSmsSend(SmsSend obj);
      
      boolean verifySms(String authCode,SmsSend obj);

}

