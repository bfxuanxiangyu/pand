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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.weeds.pand.auth.user.service.PoliceService;

/**
 * @author sunxiaopeng
 * @date 2017年10月31日 下午5:24:10
 */
@Controller
@RequestMapping("/admin/police")
public class PoliceController {
	@Resource
	private PoliceService policeService;

	@RequestMapping(value = "/gettree")
	public @ResponseBody List<Map<String, String>> getRoleTree(String areaCode) {
		return policeService.getPoliceTree(areaCode);
	}

}
