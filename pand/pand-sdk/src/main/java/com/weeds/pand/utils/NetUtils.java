package com.weeds.pand.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

public class NetUtils {
	
	private static Logger logger = LoggerFactory.getLogger(NetUtils.class);
	
	public static boolean isConnect() {
		boolean connect = false;
		Runtime runtime = Runtime.getRuntime();
		Process process;
		try {
			process = runtime.exec("ping " + "www.baidu.com");
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			is.close();
			isr.close();
			br.close();

			if (null != sb && !sb.toString().equals("")) {
				if (sb.toString().indexOf("TTL") > 0) {
					// 网络畅通
					connect = true;
				} else {
					// 网络不畅通
					connect = false;
				}
			}
		} catch (IOException e) {
			logger.error(""+e.getMessage(),e);
		}
		return connect;
	}
	
	
	 /**
     * 获得内网IP
     * @return 内网IP
     */
    private static String getIntranetIp(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得外网IP
     * @return 外网IP
     */
    private static String getInternetIp(){
        try{
            String wip = getV4IP();
            if(PandStringUtils.isNotBlank(wip)){
            	return wip;
            }
            // 如果没有外网IP，就返回内网IP
            return INTRANET_IP;
        } catch(Exception e){
        	logger.error(""+e.getMessage(),e);
        }
        return "127.0.0.1";
    }
    
    public static String INTRANET_IP = getIntranetIp(); // 内网IP
    public static String INTERNET_IP = getInternetIp(); // 外网IP
    
    
    public static String getV4IP(){
    	String ip = "";
    	String chinaz = "http://ip.chinaz.com";
    	
    	StringBuilder inputLine = new StringBuilder();
    	String read = "";
    	URL url = null;
    	HttpURLConnection urlConnection = null;
    	BufferedReader in = null;
    	try {
    		url = new URL(chinaz);
    		urlConnection = (HttpURLConnection) url.openConnection();
    	    in = new BufferedReader( new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
    		while((read=in.readLine())!=null){
    			inputLine.append(read+"\r\n");
    		}
    		//System.out.println(inputLine.toString());
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}finally{
    		if(in!=null){
    			try {
    				in.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    	}
    	
    	
    	Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
    	Matcher m = p.matcher(inputLine.toString());
    	if(m.find()){
    		String ipstr = m.group(1);
    		ip = ipstr;
    		//System.out.println(ipstr);
    	}
    	return ip;
    }	
    
    public static String getIpInfo(String ip){
    	
    	String address = ip;
    	try {
    		URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip="+ip);
    		URLConnection con = url.openConnection();
    		InputStream is = con.getInputStream();
    		InputStreamReader isr = new InputStreamReader(is);
    		BufferedReader br = new BufferedReader(isr);
    		StringBuffer buffer = new StringBuffer();
    		String line =null;
    		while(null != (line = br.readLine()))
    		{
    			buffer.append(line);
    		}
    		br.close();
    		isr.close();
    		is.close();
    		
    		address = buffer.toString();
    	} catch (Exception e) {
    	}
    	return address;
    }
    
    public static Map<String,String> getIpInfoParse(String ip){
    	Map<String,String> map = Maps.newHashMap();
    	String address = ip;
    	try {
    		URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip="+ip);
    		URLConnection con = url.openConnection();
    		InputStream is = con.getInputStream();
    		InputStreamReader isr = new InputStreamReader(is);
    		BufferedReader br = new BufferedReader(isr);
    		StringBuffer buffer = new StringBuffer();
    		String line =null;
    		while(null != (line = br.readLine()))
    		{
    			buffer.append(line);
    		}
    		br.close();
    		isr.close();
    		is.close();
    		
    		StringBuffer addbuffer = new StringBuffer(); 
    		JSONObject ipobj = JSON.parseObject(buffer.toString());
    		if(ipobj != null){
    			JSONObject dataObj = ipobj.getJSONObject("data");
    			if(dataObj != null){
    				addbuffer.append(dataObj.getString("country"));//国家
    				map.put("country", dataObj.getString("country"));
    				addbuffer.append(dataObj.getString("region"));//省会
    				map.put("region", dataObj.getString("region"));
    				addbuffer.append(dataObj.getString("city"));//城市
    				map.put("city", dataObj.getString("city"));
    				addbuffer.append(dataObj.getString("area"));//区域
    				map.put("area", dataObj.getString("area"));
    				addbuffer.append("_"+dataObj.getString("isp"));//运营商
    				map.put("isp", dataObj.getString("isp"));
    			}
    		}
    		address = addbuffer.toString();
    	} catch (Exception e) {
    	}
    	map.put("address", address);
    	return map;
    }
    
    /**
     * 获取http://2018.ip138.com/ic.asp下本机外网ip
     * @param ip
     * @return
     */
    public static String getWanIp(){
    	String wanIp = null;
    	try {
    		URL url = new URL("http://2018.ip138.com/ic.asp");
    		URLConnection con = url.openConnection();
    		InputStream is = con.getInputStream();
    		InputStreamReader isr = new InputStreamReader(is,"gbk");
    		BufferedReader br = new BufferedReader(isr);
    		StringBuffer buffer = new StringBuffer();
    		String line =null;
    		while(null != (line = br.readLine()))
    		{
    			buffer.append(line);
    		}
    		br.close();
    		isr.close();
    		is.close();
    		String wStr = buffer.toString();
    		if(wStr.contains("[") && wStr.contains("]")){
    			wanIp = wStr.substring(wStr.indexOf("[")+1,wStr.indexOf("]"));
    		}
		} catch (Exception e) {
		}
    	return wanIp;
    }
    
    public static void main(String[] args) {
    	//http://2018.ip138.com/ic.asp
    	System.out.println(getWanIp());
	}

}
