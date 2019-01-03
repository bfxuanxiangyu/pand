package com.weeds.pand.api.wxpay;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Administrator on 2016/10/13.
 */
public class HttpService {

    public static String post(String xml, String url, boolean flag, int timeOut) {
        return doPost(url, xml, "UTF-8");
    }

    public static String doPost(String url, String content, String charset) {
        DefaultHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            HttpEntity httpEntity = new StringEntity(content, charset);
            httpPost.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

}
