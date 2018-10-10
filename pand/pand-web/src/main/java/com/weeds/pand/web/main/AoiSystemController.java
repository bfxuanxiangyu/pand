package com.weeds.pand.web.main;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weeds.pand.service.sinnren.domain.SinnrenLog;
import com.weeds.pand.service.sinnren.mapper.SinnrenLogJpaDao;
import com.weeds.pand.service.system.domain.AskFor;
import com.weeds.pand.service.system.service.SystemVersionInfoService;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandStringUtils;
import com.weeds.pand.utils.NetUtils;

@Controller
@RequestMapping("/api")
public class AoiSystemController {

	private Logger logger = LoggerFactory.getLogger(AoiSystemController.class);
	
	@Resource
	private SinnrenLogJpaDao sinnrenLogJpaDao;
	
	@Resource
	private SystemVersionInfoService systemVersionInfoService;
	
	@ResponseBody
	@RequestMapping("/sinnrenlog")
	public String extractMoney(String ip,String username,String version){
		if(PandStringUtils.isBlank(ip)){
			return "illegal url";
		}
		try {
			SinnrenLog sl = new SinnrenLog();
			sl.setIp(ip);
			sl.setUsername(username);
			sl.setCreateTime(new Date());
			Map<String,String> map =  NetUtils.getIpInfoParse(ip);
			String address = map.get("address");
			String country = map.get("country");
			String region = map.get("region");
			String city = map.get("city");
			String area = map.get("area");
			String isp = map.get("isp");
			sl.setAddress(address);
			sl.setCountry(country);
			sl.setRegion(region);
			sl.setCity(city);
			sl.setArea(area);
			sl.setIsp(isp);
			sl.setVersionS(version);
			sinnrenLogJpaDao.save(sl);
			logger.info("访问ip："+ip+",用户名："+username+",地址："+address+",登录时间："+PandDateUtils.dateToStr(new Date(), PandDateUtils.format_senconde));
		} catch (Exception e) {
			logger.error("访问日志异常"+e.getMessage(),e);
		}
		return "success";
	}
	
	@ResponseBody
	@RequestMapping("/askfor_save")
	public String askforSave(AskFor askfor){
		if(PandStringUtils.isBlank(askfor.getContent())){
			return "illegal url";
		}
		try {
			askfor.setCreateTime(new  Date());
			systemVersionInfoService.saveAskForInfo(askfor);
		} catch (Exception e) {
			logger.error("索要服务异常"+e.getMessage(),e);
		}
		return "success";
	}
	
	
}
