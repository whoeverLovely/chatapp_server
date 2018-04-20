package com.whoeverlovely.chatapptest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.junit.Test;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.whoeverlovely.chatapp.DBUtil;

public class MsgTest {
	
	
	@Test
	public void sendMsg() {
		String userId = "1";
		String sqlQueryToken = "select token from users where id = ?";
		Map<Integer, Object> param = new HashMap<Integer, Object>();
		param.put(1, Integer.parseInt(userId));
		List<Map<String, Object>> result = null;;
		try {
			result = DBUtil.executeQuery(sqlQueryToken, param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String token = (String) result.get(0).get("token");
		/*String token = "dNmlkBc9sWs:APA91bHsDr9zOt98pJIMcGXfmBhjbPcZcY_j-DNy4TPbiaOQcDzZeYd794Un9vMs1whJlTJ21N_lk3fn3DnHxJ690d29tc3L3od63NIkUZdCEuMSLYvOcPj9Zh-VyB3rkaOOAKPG91Ch";*/
		/*		String token = "fJz834YOcUs:APA91bGigy26r2S2NMrba6M3e5FgYAIEdBSlIyPq2XpKOdXJH4mO40InPqD9zB7qYDh31RHKNQ4El39sCYgBMykLsMaCY1j-iCgNtHIlEM9Dagz4n33OtICH4D8zUfiRxnw95Dp2Na_P";
*/		/*String serverKey = "AAAAvljTIt0:APA91bGAW7QAUbtEQhB4EYCNNMstGsTwVJ1guqSeLgNmlvb6spEpq-IQDJ1eU5deBkV5MkBg1S-TMCiKQVDd3-om15T3pFDBO5705MrtU9JwFODP8JJ7oOLOXlyL5v0qEHclrwfu0OLA";*/
		String projectId = "messaging-16134";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send");
		httpPost.setHeader("Content-Type", "application/json; UTF-8");
		try {
			httpPost.setHeader("Authorization", "Bearer " + getAccessToken());

			JSONObject notification = new JSONObject();
			notification.put("title", "this is notification title test");
			notification.put("body", "this is notification body test");

			JSONObject data = new JSONObject();
			data.put("data key1", "this is data key 1 test");
			data.put("data key2", "this is data key 2 test");

			JSONObject message = new JSONObject();
			message.put("token", token);
			message.put("notification", notification);
			message.put("data", data);
			
			JSONObject fcm = new JSONObject();
			fcm.put("message", message);
			
			StringEntity entity = null;
			entity = new StringEntity(fcm.toString());
			httpPost.setEntity(entity);

			CloseableHttpResponse response = httpClient.execute(httpPost);
			System.out.println(response);
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
	
	public static void main(String[] args) {
		System.out.println("hehe");
	}
}
