package com.cetcme.springBootDemo.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestServiceUtil {
	
	static Logger logger = LoggerFactory.getLogger(RestServiceUtil.class);

	public static String post(String uri, List<NameValuePair> params) {
		String result = null;
		
		// (1) Post请求
		HttpPost post = new HttpPost(uri);
		
		// (2) 设置超时
		RequestConfig requestConfig = RequestConfig.custom()    
				.setConnectTimeout(4000).setConnectionRequestTimeout(1000)    
				.setSocketTimeout(4000).build();
		
		// (3) 建立请求
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
			
			post.setConfig(requestConfig);
	
			// (4) 添加参数
			post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
		
			result = EntityUtils.toString(httpclient.execute(post).getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
            try {
            	httpclient.close();
            } catch (IOException e) {
                logger.error("http接口调用异常", e);
            }
        }
		
		return result;
	}
}
