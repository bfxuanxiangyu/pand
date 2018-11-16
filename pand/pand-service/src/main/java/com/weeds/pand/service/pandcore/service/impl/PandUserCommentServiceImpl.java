package com.weeds.pand.service.pandcore.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;


import javax.annotation.Resource;


import org.springframework.stereotype.Service;

import com.weeds.pand.service.pandcore.service.PandUserCommentService; 
import com.weeds.pand.service.pandcore.domain.PandUserComment;
import com.weeds.pand.service.pandcore.mapper.PandUserCommentMapper;
import com.weeds.pand.service.pandcore.mapper.PandUserCommentJpaDao;


/**
 * service实现类
 * 由GenEntityMysql类自动生成
 * Thu Nov 15 12:31:50 CST 2018
 * @xuanxy
 */ 
@Service
public class PandUserCommentServiceImpl implements PandUserCommentService{

      @Resource
      private PandUserCommentJpaDao pandUserCommentJpaDao;
      @Resource
      private PandUserCommentMapper pandUserCommentMapper;


      @Override
      public List<PandUserComment> getPandUserCommentList(Map<String, Object> parameters) {
      		return pandUserCommentMapper.getPandUserCommentList(parameters);
      }


      @Override
      public PandUserComment getPandUserCommentById(String id) {
      		return pandUserCommentJpaDao.findOne(id);
      }


      @Override
      public void savePandUserComment(PandUserComment obj) {
      		obj.setUpdateTime(new Date());
      		pandUserCommentJpaDao.save(obj);
      }


}

