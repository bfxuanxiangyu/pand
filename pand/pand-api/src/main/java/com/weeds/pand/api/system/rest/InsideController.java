package com.weeds.pand.api.system.rest;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weeds.pand.service.pandcore.domain.PandService;
import com.weeds.pand.service.pandcore.mapper.PandServiceMapper;
import com.weeds.pand.service.pandcore.service.PandServiceService;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandResponseUtil;
import com.weeds.pand.utils.PandStringUtils;
import com.weeds.pand.utils.weixin.GetWeixinInfoUtils;

@Controller
@RequestMapping("/api/system")
public class InsideController {
	
	private static final Logger logger = LoggerFactory.getLogger(InsideController.class);
	
	@Resource
	private PandServiceService pandServiceService;
	
	@Resource
	private PandServiceMapper pandServiceMapper;
	
	@Value("${img.savePath}")
	private String savePath;
	@Value("${img.imgUrl}")
	private String imgUrl;
	@Value("${weixin.app_id}") // spring配置文件配置了appID
    private String appId;
    
    @Value("${weixin.app_secret}") // spring配置文件配置了secret
    private String secret;
    
    /**
     * 获取分析二维码
     * @param serviceId
     * @return
     */
    @ResponseBody
    @RequestMapping("/generate_all")
    public String generateAll(){
        try {
        	List<PandService> listQrNull = pandServiceMapper.getPandServiceListQrNull();
        	if(listQrNull==null || listQrNull.isEmpty()){
        		return PandResponseUtil.printFailJson(PandResponseUtil.PARAMETERS,"无需生成", null);
        	}
        	String accessToken = GetWeixinInfoUtils.isVailToken(appId, secret);
        	String porder = "qrcode/";
        	String porderPath = porder+PandDateUtils.dateToStr(new Date(), "yyyyMMdd")+"/";
        	File imgFile = new File(savePath+porderPath);
			if(!imgFile.exists()){
				imgFile.mkdirs();
			}
			
        	String fileName = PandStringUtils.getUUID()+".png";
        	for (PandService obj : listQrNull) {
        		String path = "pages/service/servicedetails/servicedetails?id="+obj.getId();
        		boolean qrCodeUrl = GetWeixinInfoUtils.getQrCodeUrl(accessToken, path, 430, savePath+porderPath, fileName);
        		if(!qrCodeUrl){
        			//token失效，更换token再取一次
        			logger.info("token无效，继续下一个");
        			continue;
        		}
        		String qrUrl = imgUrl+porderPath+fileName;
        		obj.setQrUrl(qrUrl);
        		try {
        			pandServiceService.savePandService(obj);
        		} catch (Exception e) {
        			logger.error("二维码路径持久化异常"+e.getMessage(),e);
        		}
        		
			}
        	return PandResponseUtil.printJson("全部生成成", null);
        } catch (Exception e) {
        	logger.error("分析二维码获取异常"+e.getMessage(),e);
        	return PandResponseUtil.printFailJson(PandResponseUtil.SERVERUPLOAD,"服务器升级", null);
        }
    }
    
}
