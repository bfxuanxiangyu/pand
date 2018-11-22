package com.weeds.pand.service.pandcore.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.weeds.pand.service.pandcore.domain.PandUserComment;
import com.weeds.pand.service.pandcore.mapper.PandUserCommentJpaDao;
import com.weeds.pand.service.pandcore.mapper.PandUserCommentMapper;
import com.weeds.pand.service.pandcore.pagevo.PandCommentQueryParam;
import com.weeds.pand.service.pandcore.service.PandUserCommentService;


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
    	  	Map<String, Object> parameters = Maps.newHashMap();
    	  	parameters.put("id", id);
    	  	List<PandUserComment> list = pandUserCommentMapper.getPandUserCommentList(parameters);
    	  	if(list == null || list.isEmpty()){
    	  		return null;
    	  	}
      		return list.get(0);
      }


      @Override
      public void savePandUserComment(PandUserComment obj) {
      		obj.setUpdateTime(new Date());
      		pandUserCommentJpaDao.save(obj);
      }


	@Override
	public PageInfo<PandUserComment> selectAllForPage(PandCommentQueryParam params) {
		PageHelper.offsetPage(params.getStart(), params.getLength());
		List<PandUserComment> list = pandUserCommentMapper.getPandCommentListPage(params);
		PageInfo<PandUserComment> page = new PageInfo<PandUserComment>(list);
		return page;
	}


}

