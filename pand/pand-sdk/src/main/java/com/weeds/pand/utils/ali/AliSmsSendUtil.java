package com.weeds.pand.utils.ali;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.common.collect.Maps;
import com.weeds.pand.utils.PandStringUtils;

/**
 * Created on 17/6/7.
 * 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 * 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar
 * 2:aliyun-java-sdk-dysmsapi.jar
 *
 * 备注:Demo工程编码采用UTF-8
 * 国际短信发送请勿参照此DEMO
 */
public class AliSmsSendUtil {
	
	static final Logger logger = LoggerFactory.getLogger(AliSmsSendUtil.class);

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAIUt7dEpePajjt";
    static final String accessKeySecret = "IFZ4F0rYWj3F47bi590XssYkwinXJL";
    
    public static void main(String[] args) {
		String phone = "18616521390";
		String signName = "米米";
		String tempLateCode = "SMS_156465904";
		Map<String, Object> parameters = Maps.newConcurrentMap();
		parameters.put("code", "1233");
		String res = sendSms(phone, tempLateCode,signName, PandStringUtils.getJsonObj(parameters));
		System.out.println(res);
	}

    public static String sendSms(String phone,String tempLateCode,String signName,String jsonContent){
    	
    	try {
			
    		//可自助调整超时时间
    		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
    		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
    		
    		//初始化acsClient,暂不支持region化
    		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
    		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
    		IAcsClient acsClient = new DefaultAcsClient(profile);
    		
    		//组装请求对象-具体描述见控制台-文档部分内容
    		SendSmsRequest request = new SendSmsRequest();
    		//必填:待发送手机号
    		request.setPhoneNumbers(phone);
    		//必填:短信签名-可在短信控制台中找到
    		request.setSignName(signName);
    		//必填:短信模板-可在短信控制台中找到
    		request.setTemplateCode(tempLateCode);
    		//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
    		request.setTemplateParam(jsonContent);
    		
    		//选填-上行短信扩展码(无特殊需求用户请忽略此字段)
    		//request.setSmsUpExtendCode("90997");
    		
    		//可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//    		request.setOutId("yourOutId");
    		
    		//hint 此处可能会抛出异常，注意catch
    		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
    		String code = sendSmsResponse.getCode();
    		String message = sendSmsResponse.getMessage();
    		System.out.println("短信发送成功,返回结果="+message+",状态："+code);
    		logger.info("短信发送成功,返回结果="+message+",状态："+code);
    		if(code.equals("OK")){
    			return code;
    		}else{
    			sendSmsResponse.getMessage();
    			return null;
    		}
		} catch (Exception e) {
			logger.error("短信发送异常"+e.getMessage(),e);
			return null;
		}
    	
    }
    
}
