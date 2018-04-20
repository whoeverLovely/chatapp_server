package com.whoeverlovely.chatapptest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.junit.Test;

public class HttpClientTest {
	
	
	
	@Test
	public void httpClient() {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/chatapptest/testServlet");
		httpPost.setHeader("Content-Type","application/json; UTF-8");
		
		JSONObject message = new JSONObject();
		message.put("msgContent", "air is bad today.");
		message.put("senderId", 1);
		message.put("receiverId", 3);
		
		StringEntity entity = null;
		try {
			entity = new StringEntity(message.toString());
			httpPost.setEntity(entity);
			
			CloseableHttpResponse response = client.execute(httpPost);
			System.out.println(response);
			client.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static String getProp(String propName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("config.properties");
		
		Properties properties = new Properties();
		String value = null;
		try {
			properties.load(input);
			value = properties.getProperty(propName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return value;
	}
	
	
	public void test() {
		System.out.println(getProp("database.host"));
	}

}
