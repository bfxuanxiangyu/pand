/*
 * Copyright (C) 2017 Shanghai sinnren soft Co., Ltd
 *
 * All copyrights reserved by Shanghai sinnren.
 * Any copying, transferring or any other usage is prohibited.
 * Or else, Shanghai sinnren possesses the right to require legal 
 * responsibilities from the violator.
 * All third-party contributions are distributed under license by
 * Shanghai sinnren soft Co., Ltd.
 */
package com.weeds.pand.auth.user.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.weeds.pand.auth.log.OperateLog;
import com.weeds.pand.auth.log.OperateType;
import com.weeds.pand.core.web.DataTablesResult;
import com.weeds.pand.service.mechanic.domain.PandUser;
import com.weeds.pand.service.mechanic.service.PandUserService;
import com.weeds.pand.service.pandcore.domain.PandUserComment;
import com.weeds.pand.service.pandcore.pagevo.PandCommentQueryParam;
import com.weeds.pand.service.pandcore.service.PandUserCommentService;

/**
 * @author user
 * @date 2017年9月21日 下午5:47:40	
 */
@Controller
@RequestMapping("/admin/comment_manage")
public class CommentManagerController {
	
	private Logger logger = LoggerFactory.getLogger(PandManagerController.class);
	
	@Resource
	private PandUserCommentService pandUserCommentService;
	@Resource
	private PandUserService pandUserService;
	
	@RequestMapping("/pandcommentlist")
	@OperateLog(desc="查询评论", type=OperateType.QUERY,moudle="评论管理",menu="commentmanager")
	public ModelAndView pandcommentlist (){
		ModelAndView view = new ModelAndView();
		
		Map<String, Object> parameters = Maps.newHashMap();
		List<PandUser> pandUserList = pandUserService.selectAll(parameters);
		view.addObject("pandUserList",pandUserList);
		
		Date startTime = DateTime.now().minusMonths(12).toDate();
		Date endTime = new Date();
		view.addObject("startTime", startTime);
		view.addObject("endTime", endTime);
		view.setViewName("comment/comment_list");
		return view;
	}
	
	
	@ResponseBody
	@RequestMapping("/pandCommentDatas")
	public DataTablesResult<PandUserComment> dataList(PandCommentQueryParam params){
		PageInfo<PandUserComment> page = pandUserCommentService.selectAllForPage(params);
		DataTablesResult<PandUserComment> result = new DataTablesResult<PandUserComment>();
		result.setData(page.getList());
		result.setRecordsTotal(page.getTotal());
		result.setRecordsFiltered(page.getTotal());
		return result;
	}
	
	@RequestMapping("/dealPandComment")
	@OperateLog(desc="删除评论", type=OperateType.DELETE,moudle="删除评论",menu="deletecomment")
    public ModelAndView dealPandComment(String id) {
		ModelAndView view = new ModelAndView();
    	int isSuc = 0;
    	String message = "删除成功";
    	try {
    		
    		Map<String, Object> parameters = Maps.newHashMap();
    		parameters.put("id", id);
    		PandUserComment comment = pandUserCommentService.getPandUserCommentById(id);
    		if(comment== null){
    			view.addObject("isSuc", false);		
    			view.addObject("message", "评论不存在");
    			return view;
    		}
    		comment.setStatus(1);
    		pandUserCommentService.savePandUserComment(comment);
    		isSuc = 1;
		} catch (Exception e) {
	    	message = "删除异常";
			logger.error("普通用户设置异常"+e.getMessage(),e);
		}
		view.addObject("isSuc", isSuc > 0);		
		view.addObject("message", message);		
		view = pandcommentlist();
        return view;
    }
	
}
