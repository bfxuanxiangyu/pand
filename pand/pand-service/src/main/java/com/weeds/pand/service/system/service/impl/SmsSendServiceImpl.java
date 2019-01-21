package com.weeds.pand.service.system.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.weeds.pand.service.system.domain.SmsSend;
import com.weeds.pand.service.system.mapper.SmsSendJpaDao;
import com.weeds.pand.service.system.mapper.SmsSendMapper;
import com.weeds.pand.service.system.service.SmsSendService;


/**
 * service实现类
 * 由GenEntityMysql类自动生成
 * Mon Jan 21 18:46:33 CST 2019
 * @xuanxy
 */ 
@Service
public class SmsSendServiceImpl implements SmsSendService{

      private static final Logger logger = LoggerFactory.getLogger(SmsSendServiceImpl.class);


      @Resource
      private SmsSendJpaDao smsSendJpaDao;
      @Resource
      private SmsSendMapper smsSendMapper;


      @Override
      public List<SmsSend> getSmsSendList(Map<String, Object> parameters) {
      		return smsSendMapper.getSmsSendList(parameters);
      }


      @Override
      public SmsSend getSmsSendById(String id) {
      		return smsSendJpaDao.findOne(id);
      }


      @Override
      public void saveSmsSend(SmsSend obj) {
      		smsSendJpaDao.save(obj);
      }


	  @Override
	  public SmsSend getSmsSendByPhone(String phone) {
		    return smsSendMapper.getSmsSendByPhone(phone);
	  }


	@Override
	public boolean verifySms(String authCode,SmsSend obj) {
		if(!authCode.equals(obj.getPhone())){
			logger.info("验证码不匹配");
			return false;
		}
		if((new Date().getTime() - obj.getCreateTime().getTime())>120000){
			logger.info("验证码已过期");
			return false;
		}
		obj.getPhone();
		return false;
	}


}

