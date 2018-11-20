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
import com.weeds.pand.service.pandcore.domain.PandOrder;
import com.weeds.pand.service.pandcore.pagevo.PandOrderQueryParam;
import com.weeds.pand.service.pandcore.service.PandOrderService;

/**
 * @author user
 * @date 2017年9月21日 下午5:47:40	
 */
@Controller
@RequestMapping("/admin/order_manage")
public class PandManagerController {
	
//	private Logger logger = LoggerFactory.getLogger(PandManagerController.class);
	
	@Resource
	private PandOrderService pandOrderService;
	@Resource
	private PandUserService pandUserService;
	
	@RequestMapping("/pandorderlist")
	@OperateLog(desc="查询PAND订单", type=OperateType.QUERY,moudle="订单管理",menu="ordermanager")
	public ModelAndView userList (){
		ModelAndView view = new ModelAndView();
		
		Map<String, Object> parameters = Maps.newHashMap();
		List<PandUser> pandUserList = pandUserService.selectAll(parameters);
		view.addObject("pandUserList",pandUserList);
		Date startTime = DateTime.now().minusMonths(12).toDate();
		Date endTime = new Date();
		view.addObject("startTime", startTime);
		view.addObject("endTime", endTime);
		view.setViewName("order/order_list");
		return view;
	}
	
	
	@ResponseBody
	@RequestMapping("/pandOrderDatas")
	public DataTablesResult<PandOrder> dataList(PandOrderQueryParam params){
		PageInfo<PandOrder> page = pandOrderService.selectAllForPage(params);
		DataTablesResult<PandOrder> result = new DataTablesResult<PandOrder>();
		result.setData(page.getList());
		result.setRecordsTotal(page.getTotal());
		result.setRecordsFiltered(page.getTotal());
		return result;
	}
	
}
