package com.weeds.pand.api.system.rest;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.weeds.pand.auth.user.domain.Area;
import com.weeds.pand.auth.user.service.AreaService;
import com.weeds.pand.service.system.domain.Banner;
import com.weeds.pand.service.system.domain.Skills;
import com.weeds.pand.service.system.domain.SystemVersionInfo;
import com.weeds.pand.service.system.mapper.BannerMapper;
import com.weeds.pand.service.system.mapper.SkillsMapper;
import com.weeds.pand.service.system.service.SystemVersionInfoService;
import com.weeds.pand.utils.PandResponseUtil;
import com.weeds.pand.utils.PandStringUtils;
import com.weeds.pand.utils.TencentMapUtils;

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
	
	@Resource
	private AreaService areaService;
	
	@Value("${tecent.key}")
	private String key;
	
	@ResponseBody
	@RequestMapping("/skills_list")
	public String skillsList() {
		
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("status", 1);
		List<Skills> list = skillsMapper.selectAll(parameters);
		Map<String, Object> map = Maps.newHashMap();
		map.put("fullList", list);
		List<LinkedHashMap<String, List<Skills>>> dataList = Lists.newArrayList();
		LinkedHashMap<String, List<Skills>> dataMap = Maps.newLinkedHashMap();
		int index = 0;
		String keyName = "data"+index;
		List<Skills> sList = Lists.newArrayList();
		for (int i = 0; i < list.size(); i++) {
			if(i>0 && i%8 == 0){
				dataMap.put(keyName, sList);
				dataList.add(dataMap);
				index++;
				keyName="data"+index;
				sList = Lists.newArrayList();
				sList.add(list.get(i));
			}else{
				sList.add(list.get(i));
			}
			
			if(i==list.size()-1){//循环至最后一条   一次性加入
				dataMap.put(keyName, sList);
				dataList.add(dataMap);
			}
		}
		map.put("dataArray", dataMap);
		return PandResponseUtil.printJson("技能列表获取成功", map);
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
	@RequestMapping("/getLocation")
	public String getLocation(String location) {
		if(PandStringUtils.isBlank(location)){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		String address = TencentMapUtils.getAddress(location,key);
		return PandResponseUtil.printJson("地址获取成功", address);
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
	
	@ResponseBody
	@RequestMapping("/area_list")
    public String areaList(String pAreaCode) {
		if(PandStringUtils.isBlank(pAreaCode)){
			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
		}
		try {
			List<Area> list = areaService.getAreaListByPAreaCode(pAreaCode);
			return PandResponseUtil.printJson("区域列表获取成功", list);
		} catch (Exception e) {
			logger.error("区域列表获取异常"+e.getMessage(),e);
		}
		
		
		return PandStringUtils.getJsonObj("success");
    }

}
