package com.weeds.pand.api.system.rest;

import java.io.File;
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

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.weeds.pand.auth.user.domain.Area;
import com.weeds.pand.auth.user.service.AreaService;
import com.weeds.pand.service.pandcore.domain.PandService;
import com.weeds.pand.service.pandcore.service.PandServiceService;
import com.weeds.pand.service.system.domain.Banner;
import com.weeds.pand.service.system.domain.Skills;
import com.weeds.pand.service.system.domain.SystemVersionInfo;
import com.weeds.pand.service.system.mapper.BannerMapper;
import com.weeds.pand.service.system.mapper.SkillsMapper;
import com.weeds.pand.service.system.service.SystemVersionInfoService;
import com.weeds.pand.utils.HttpUtils;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandResponseUtil;
import com.weeds.pand.utils.PandStringUtils;
import com.weeds.pand.utils.TencentMapUtils;
import com.weeds.pand.utils.weixin.GetWeixinInfoUtils;

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
	@Resource
	private PandServiceService pandServiceService;
	
	@Value("${tecent.key}")
	private String key;
	
	@Value("${img.savePath}")
	private String savePath;
	@Value("${img.imgUrl}")
	private String imgUrl;
	
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
		//删除list最后一条数据
		list.remove(list.size()-1);
		map.put("fullList", list);
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
		Map<String, Object> address = TencentMapUtils.getAddress(location,key);
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

	
	@Value("${weixin.app_id}") // spring配置文件配置了appID
    private String appId;
    
    @Value("${weixin.app_secret}") // spring配置文件配置了secret
    private String secret;
    
    @ResponseBody
    @RequestMapping("/openId")
    public String openId(String code){ // 小程序端获取的CODE
        Map<String, Object> params = Maps.newHashMap();
        try {
        	if(PandStringUtils.isBlank(code)){
    			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
    		}
            StringBuilder urlPath = new StringBuilder("https://api.weixin.qq.com/sns/jscode2session"); // 微信提供的API，这里最好也放在配置文件
            params.put("appid", appId);
            params.put("secret", secret);
            params.put("js_code", code);
            params.put("grant_type", "authorization_code");
//            urlPath.append(String.format("?appid=%s", appId));
//            urlPath.append(String.format("&secret=%s", secret));
//            urlPath.append(String.format("&js_code=%s", code));
//            urlPath.append(String.format("&grant_type=%s", "authorization_code")); // 固定值
            String data = HttpUtils.doPost(urlPath.toString(), params);// java的网络请求，这里是我自己封装的一个工具包，返回字符串
            logger.info("微信授权获取数据：data="+data);
            if(PandStringUtils.isBlank(data)){
            	return PandResponseUtil.printFailJson(PandResponseUtil.failed,"无法获取openId", null);
            }
            JSONObject object = JSONObject.parseObject(data);
            String openid = object.getString("openid");
            String sessionKey = object.getString("openid");
            Map<String, Object> returnMaps = Maps.newHashMap();
            returnMaps.put("openid", openid);
            returnMaps.put("sessionKey", sessionKey);
            
            return PandResponseUtil.printJson("获取微信登录信息成功", returnMaps);
            
        } catch (Exception e) {
        	logger.error("openid获取异常"+e.getMessage(),e);
        	return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
        }
    }
    
    /**
     * 获取分析二维码
     * @param serviceId
     * @return
     */
    @ResponseBody
    @RequestMapping("/share_service_qr")
    public String shareService(String serviceId){
    	logger.info("获取分析二维码serviceId="+serviceId);
        try {
        	if(PandStringUtils.isBlank(serviceId)){
    			return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"缺少参数", null);
    		}
        	PandService obj = pandServiceService.getPandServiceById(serviceId);
        	if(obj==null){
        		return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"服务不存在", null);
        	}
        	if(PandStringUtils.isNotBlank(obj.getQrUrl())){
        		return PandResponseUtil.printJson("获取分析二维码成功", obj.getQrUrl());
        	}
        	
        	String accessToken = GetWeixinInfoUtils.isVailToken(appId, secret);
        	logger.info("---获取微信token="+accessToken);
        	if(PandStringUtils.isBlank(accessToken)){
        		return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"微信账号不匹配", null);
        	}
        	String porder = "qrcode/";
        	String porderPath = porder+PandDateUtils.dateToStr(new Date(), "yyyyMMdd")+"/";
        	File imgFile = new File(savePath+porderPath);
			if(!imgFile.exists()){
				imgFile.mkdirs();
			}
			String path = "pages/service/servicedetails/servicedetails?id="+serviceId;
			
        	String fileName = PandStringUtils.getUUID()+".png";
            boolean qrCodeUrl = GetWeixinInfoUtils.getQrCodeUrl(accessToken, path, 430, savePath+porderPath, fileName);
            if(!qrCodeUrl){
            	//token失效，更换token再取一次
            	return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"微信账号异常", null);
            }
            String qrUrl = imgUrl+porderPath+fileName;
            obj.setQrUrl(qrUrl);
            try {
				pandServiceService.savePandService(obj);
			} catch (Exception e) {
				logger.error("二维码路径持久化异常"+e.getMessage(),e);
			}
            logger.info("二维码路径="+qrUrl);
            return PandResponseUtil.printJson("获取分析二维码成功", qrUrl);
        } catch (Exception e) {
        	logger.error("分析二维码获取异常"+e.getMessage(),e);
        	return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
        }
    }
    
}
