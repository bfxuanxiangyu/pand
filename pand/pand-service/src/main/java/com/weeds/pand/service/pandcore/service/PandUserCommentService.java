package com.weeds.pand.service.pandcore.service;


import java.util.List;
import java.util.Map;

import com.weeds.pand.service.pandcore.domain.PandUserComment;

/**
 * service接口
 * 由GenEntityMysql类自动生成
 * Thu Nov 15 12:31:50 CST 2018
 * @xuanxy
 */ 
public interface PandUserCommentService{


      List<PandUserComment> getPandUserCommentList(Map<String, Object> parameters);

      PandUserComment getPandUserCommentById(String id);

      void savePandUserComment(PandUserComment obj);

}

