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
import com.weeds.pand.auth.user.domain.Log;
import com.weeds.pand.core.web.DataTablesResult;
import com.weeds.pand.service.mechanic.domain.PandUser;
import com.weeds.pand.service.mechanic.service.PandUserService;
import com.weeds.pand.service.pandcore.pagevo.PandUserQueryParam;

/**
 * @author user
 * @date 2017年9月21日 下午5:47:40	
 */
@Controller
@RequestMapping("/admin/user")
public class PandUserManagerController {
	
	private Logger logger = LoggerFactory.getLogger(PandUserManagerController.class);
	
	@Resource
	private PandUserService pandUserService;
	
	@RequestMapping("/panduserlist")
	@OperateLog(desc="查询PAND用户", type=OperateType.QUERY,moudle="普通用户管理",menu="usermanager")
	public ModelAndView userList (){
		ModelAndView view = new ModelAndView();
		
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("userType", 1);
		List<PandUser> pandUserList = pandUserService.selectAll(parameters);
		view.addObject("pandUserList",pandUserList);
		view.setViewName("user/panduserlist");
		return view;
	}
	
	@RequestMapping("/dealPandUser")
	@OperateLog(desc="设置普通用户", type=OperateType.DELETE,moudle="设置普通用户",menu="deleteuser")
    public ModelAndView dealPandUser(Integer userStatus,String id) {
    	int isSuc = 0;
    	String message = "编辑成功";
    	if(userStatus==4){
    		message = "封号成功";
    	}else{
    		message = "激活成功";
    	}
    	try {
    		Map<String, Object> parameters = Maps.newHashMap();
    		parameters.put("id", id);
    		PandUser pandUser = pandUserService.getPandUserObj(parameters);
    		pandUser.setUserStatus(userStatus);
    		
    		pandUserService.savePandUser(pandUser);
			
    		isSuc = 1;
		} catch (Exception e) {
			if(userStatus==4){
	    		message = "封号失败";
	    	}else{
	    		message = "激活失败";
	    	}
			logger.error("普通用户设置异常"+e.getMessage(),e);
		}
		ModelAndView view = userList();
		view.addObject("isSuc", isSuc > 0);		
		view.addObject("message", message);		
        return view;
    }
	
	@RequestMapping("/pandArtisanUserlist")
	@OperateLog(desc="查询PAND商户", type=OperateType.QUERY,moudle="PAND商户管理",menu="usermanager")
	public ModelAndView pandArtisanUserlist (){
		ModelAndView view = new ModelAndView();
		Date startTime = DateTime.now().minusMonths(12).toDate();
		Date endTime = new Date();
		view.addObject("startTime", startTime);
		view.addObject("endTime", endTime);
		view.setViewName("user/pandArtisanlist");
		return view;
	}
	
	@ResponseBody
	@RequestMapping("/pandUserDatas")
	public DataTablesResult<PandUser> dataList(PandUserQueryParam params){
		PageInfo<PandUser> page = pandUserService.selectAllForPage(params);
		DataTablesResult<PandUser> result = new DataTablesResult<PandUser>();
		result.setData(page.getList());
		result.setRecordsTotal(page.getTotal());
		result.setRecordsFiltered(page.getTotal());
		return result;
	}
	
}
