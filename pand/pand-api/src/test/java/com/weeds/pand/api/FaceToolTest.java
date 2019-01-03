package com.weeds.pand.api;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.weeds.pand.utils.HttpUtils;

public class FaceToolTest {
	
	
	public static void main(String[] args) {
		
		   /*String url = "http://localhost:8989/api/pand_image_upload/face_upload";//注册接口
		   String httpRes = null;
		   String localFileName = "d:opt/face/a.jpg";
				
		   Map<String,ContentBody> reqParam = new HashMap<String,ContentBody>();
		   reqParam.put("file", new FileBody(new File(localFileName)));
		   reqParam.put("userName", new StringBody("小泽", ContentType.APPLICATION_JSON));
		   reqParam.put("token", new StringBody("2pWBg4oFjxIGtSxVLjbI2auqvFSmY6qz", ContentType.MULTIPART_FORM_DATA));
		   reqParam.put("userId", new StringBody("085ba5ec70ce4d71a5b8d2494843d17f", ContentType.MULTIPART_FORM_DATA));
		   reqParam.put("imgModel", new StringBody("4", ContentType.MULTIPART_FORM_DATA));
				
		   httpRes = HttpUtils.postFileMultiPart(url,reqParam);
		   
		   System.out.println("人脸接口返回结果："+httpRes);*/
		   
		   
		   
		   String url = "http://localhost:8989/api/pand_image_upload/face_compare";//人脸比对接口
		   String httpRes = null;
		   String localFileName = "d:opt/face/1.jpg";
				
		   Map<String,ContentBody> reqParam = new HashMap<String,ContentBody>();
		   reqParam.put("file", new FileBody(new File(localFileName)));
		   reqParam.put("token", new StringBody("2pWBg4oFjxIGtSxVLjbI2auqvFSmY6qz", ContentType.MULTIPART_FORM_DATA));
		   reqParam.put("userId", new StringBody("085ba5ec70ce4d71a5b8d2494843d17f", ContentType.MULTIPART_FORM_DATA));
				
		   httpRes = HttpUtils.postFileMultiPart(url,reqParam);
		   
		   System.out.println("人脸比对接口返回结果："+httpRes);
	}

}
