package com.weeds.pand.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class HttpUtils {
	
	private static Logger logger = Logger.getLogger(HttpUtils.class);
	
	public static void main(String[] args) {
		
	}
	 
	/**
	 * get请求
	 * @return
	 */
	public static String doGet(String url) {
        try {
        	HttpClient client = HttpClients.createDefault();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
 
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                
                return strResult;
            }
        } 
        catch (IOException e) {
        	e.printStackTrace();
        }
        
        return null;
	}
	
	/**
	 * post请求(用于key-value格式的参数)
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, Map<String, Object> params){
		
		BufferedReader in = null;  
        try {  
            // 定义HttpClient  
            HttpClient client = HttpClients.createDefault();
            // 实例化HTTP方法  
            HttpPost request = new HttpPost();  
            request.setURI(new URI(url));
            
            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
            for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
    			String name = (String) iter.next();
    			String value = String.valueOf(params.get(name));
    			nvps.add(new BasicNameValuePair(name, value));
    			
    			//System.out.println(name +"-"+value);
    		}
            request.setEntity(new UrlEncodedFormEntity(nvps,StandardCharsets.UTF_8));
            
            HttpResponse response = client.execute(request);  
            int code = response.getStatusLine().getStatusCode();
            if(code == 200){	//请求成功
            	in = new BufferedReader(new InputStreamReader(response.getEntity()  
                        .getContent(),"utf-8"));
                StringBuffer sb = new StringBuffer("");  
                String line = "";  
                String NL = System.getProperty("line.separator");  
                while ((line = in.readLine()) != null) {  
                    sb.append(line + NL);  
                }
                
                in.close();  
                
                return sb.toString();
            }
            else{	//
            	System.out.println("状态码：" + code);
            	return null;
            }
        }
        catch(Exception e){
        	e.printStackTrace();
        	
        	return null;
        }
	}
	
	/**
	 * post请求（用于请求json格式的参数）
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, String params) throws Exception {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);// 创建httpPost   
		httpPost.setHeader("Accept", "application/json"); 
		httpPost.setHeader("Content-Type", "application/json");
		String charSet = "UTF-8";
		StringEntity entity = new StringEntity(params, charSet);
		httpPost.setEntity(entity);        
		CloseableHttpResponse response = null;
		
		try {
			
			response = httpclient.execute(httpPost);
			StatusLine status = response.getStatusLine();
			int state = status.getStatusCode();
			if (state == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				String jsonString = EntityUtils.toString(responseEntity);
				return jsonString;
			}
			else{
				logger.error("请求返回:"+state+"("+url+")");
			}
		}
		finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * post请求（用于请求json格式的参数）
	 * @param url
	 * @param params
	 * @return
	 */
	public static boolean doPostForFormat(String url, String params,String savePath,String fileName) throws Exception {
		
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);// 创建httpPost   
    	httpPost.setHeader("Accept", "application/json"); 
    	httpPost.setHeader("Content-Type", "application/json");
    	String charSet = "UTF-8";
    	StringEntity entity = new StringEntity(params, charSet);
    	httpPost.setEntity(entity);        
    	CloseableHttpResponse response = null;
        boolean flag = false;
        try {
        	response = httpClient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
            	HttpEntity responseEntity = response.getEntity();
            	boolean ifJson = false;
            	if (responseEntity != null) {
            		byte[] responseBytes = getData(responseEntity);
            		try {
        			 	JSONObject.parseObject(new String(responseBytes));
        	            ifJson=true;
        	        } catch (Exception e) {
        	        }
            		 if(ifJson){
            			 logger.info(new String(responseBytes));
            			 return false;
            		 }
                    if(responseBytes != null){
                    	//流保存
                    	writeLocal(responseBytes, savePath, fileName);
                    	flag = true;
                    }
                }
            	return flag;
            }else{
				 logger.error("请求返回:"+state+"("+url+")");
			}
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return flag;
	}
	
	/**
     * 获取Entity中数据
     * @param httpEntity
     * @return
     * @throws Exception
     */
    public static byte[] getData(HttpEntity httpEntity) throws Exception{

        BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(httpEntity);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bufferedHttpEntity.writeTo(byteArrayOutputStream);
        byte[] responseBytes = byteArrayOutputStream.toByteArray();
        return responseBytes;
    }
    
    private static void writeLocal(byte[] data,String savePath,String fileName){
    	try {
    		OutputStream out = new FileOutputStream(new File(savePath+fileName));
    		out.write(data);
    		out.flush();
    		out.close();
    		logger.info("分享二维码写入本地成功fileName="+fileName);
		} catch (Exception e) {
			logger.error("获取字节流图片保存异常"+e.getMessage(),e);
		}
    }

}
