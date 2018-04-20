package com.whoeverlovely.chatapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.junit.Test;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

public class FCMHandler {

	public static int sendFCM(String token, JSONObject data) {

		int responseCode = 0;
		String projectId = "messaging-16134";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send");
		httpPost.setHeader("Content-Type", "application/json; UTF-8");
		try {
			httpPost.setHeader("Authorization", "Bearer " + getAccessToken());

			JSONObject message = new JSONObject();
			message.put("token", token);
			message.put("data", data);

			JSONObject fcm = new JSONObject();
			fcm.put("message", message);

			StringEntity entity = null;
			entity = new StringEntity(fcm.toString());
			httpPost.setEntity(entity);
			
			System.out.println(fcm.toString());
			CloseableHttpResponse response = httpClient.execute(httpPost);
			responseCode = response.getStatusLine().getStatusCode();
			System.out.println(response);
			httpClient.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return responseCode;
	}

	public static void sendFCM(String token, JSONObject data, JSONObject notification) {
		String projectId = "messaging-16134";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send");
		httpPost.setHeader("Content-Type", "application/json; UTF-8");
		try {
			httpPost.setHeader("Authorization", "Bearer " + getAccessToken());

			JSONObject message = new JSONObject();
			message.put("token", token);
			message.put("notification", notification);
			message.put("data", data);

			JSONObject fcm = new JSONObject();
			fcm.put("message", message);

			StringEntity entity = null;
			entity = new StringEntity(fcm.toString());
			httpPost.setEntity(entity);

			System.out.println(fcm.toString());
			CloseableHttpResponse response = httpClient.execute(httpPost);
			System.out.println(response);
			httpClient.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private static String getAccessToken() throws IOException {
		String SCOPES = "https://www.googleapis.com/auth/firebase.messaging";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("messaging-16134-firebase-adminsdk-aoffb-f795aca492.json");
		GoogleCredential googleCredential = GoogleCredential
				.fromStream(input)
				.createScoped(Arrays.asList(SCOPES));
		googleCredential.refreshToken();
		System.out.println(googleCredential.getAccessToken());
		return googleCredential.getAccessToken();
	}
	
	@Test
	public void test() {
		String token = "c_GOu3LuBZ4:APA91bG-SA96PL9lKlD8VYXtKe-bS1s-QIRMzVRbJciU9CRqKmtuxCV8BOxfOzERm-M81gbmAq_mhDYO6Z7Tsc4HuMGW0rR4J1TEg8mW2icQ-qlwAMi8zWYfydwypXVyW-iV9nRavzNY";
		JSONObject data = new JSONObject();
		data.put("k1", "v1");
		sendFCM(token,data);
	}

}
