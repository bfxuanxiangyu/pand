package com.weeds.pand.api.system.rest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.weeds.pand.service.system.domain.Banner;
import com.weeds.pand.service.system.domain.Skills;
import com.weeds.pand.service.system.domain.SystemVersionInfo;
import com.weeds.pand.service.system.mapper.BannerMapper;
import com.weeds.pand.service.system.mapper.SkillsMapper;
import com.weeds.pand.service.system.service.SystemVersionInfoService;
import com.weeds.pand.utils.PandResponseUtil;
import com.weeds.pand.utils.PandStringUtils;

@Controller
@RequestMapping("/api/system")
public class SystemController {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemController.class);
	
	@Resource
	private SystemVersionInfoService systemVersionInfoService;
	
	@Resource
	private BannerMapper bannerMapper;
	@Resource
	private SkillsMapper skillsMapper;
	
	@ResponseBody
	@RequestMapping("/skills_list")
	public String skillsList() {
		
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("status", 1);
		List<Skills> list = skillsMapper.selectAll(parameters);
		
		return PandResponseUtil.printJson("技能列表获取成功", list);
	}
	@ResponseBody
	@RequestMapping("/banner_list")
	public String bannerList() {
		
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("status", 1);
		List<Banner> list = bannerMapper.selectAll(parameters);
		
		return PandResponseUtil.printJson("轮播图获取成功", list);
	}
	@ResponseBody
	@RequestMapping("/version")
	public String index() {
		
		SystemVersionInfo systemVersionInfo = systemVersionInfoService.getSystemVersionInfoObj("402889ea6055620f0160556252f40000");
		
		return PandResponseUtil.printJson("版本号获取成功", systemVersionInfo);
	}
	
	@ResponseBody
	@RequestMapping("/save-version")
    public String saveVersion() {
		
		SystemVersionInfo systemVersionInfo = new SystemVersionInfo();
		systemVersionInfo.setCityCode("10010");
		systemVersionInfo.setCompel("1");
		systemVersionInfo.setCreateTime(new Date());
		systemVersionInfo.setDevicetype(1);
		systemVersionInfo.setPlatCode("1001");
		systemVersionInfo.setSystemVersion("1.1.1");
		systemVersionInfo.setVersionCode(12);
		systemVersionInfo.setVersionDesc("正式上线");
		
		systemVersionInfoService.saveSystemVersionInfo(systemVersionInfo);
		
		return PandStringUtils.getJsonObj("success");
    }

}
