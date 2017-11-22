package com.cetcme.springBootDemo.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class NotificationUtil {

	private static final String NOTIFICATION_URI = "/notification.json";

	public static void send(String deviceNo, String title, String content, String msgType) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", "admin"));
		params.add(new BasicNameValuePair("password", "xMpCOKC5I4INzFCab3WEmw=="));
		params.add(new BasicNameValuePair("deviceNo", deviceNo));
		params.add(new BasicNameValuePair("title", title));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("msgType", msgType));
		Properties proper = PropertiesUtil.getResources(Constants.CONFIG_FILE_NAME);
		String baseUri = proper.getProperty(Constants.RESTSERVICE_BASE_URI);
		RestServiceUtil.post(baseUri + NOTIFICATION_URI, params);
	}
}
