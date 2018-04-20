package com.whoeverlovely.chatapp.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.whoeverlovely.chatapp.DBUtil;
import com.whoeverlovely.chatapp.FCMHandler;
import com.whoeverlovely.chatapp.MsgHandler;

/**
 * Servlet implementation class ReceiverServlet
 */
public class ReceiverServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReceiverServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// just for test
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.getWriter().append("this is receiverServlet get.");
		String msgContent = request.getParameter("msgContent");
		int sender_id = Integer.parseInt(request.getParameter("senderId"));
		int receiver_id = Integer.parseInt(request.getParameter("receiverId"));

		/*
		 * StringBuffer sb = new StringBuffer(); String line = null; try {
		 * BufferedReader reader = request.getReader(); while ((line =
		 * reader.readLine()) != null) sb.append(line); String req = sb.toString();
		 * JSONObject reqJSON = new JSONObject(req);
		 * 
		 * String msgContent = reqJSON.getString("msgContent"); int sender_id =
		 * reqJSON.getInt("senderId"); int receiver_id = reqJSON.getInt("receiverId");
		 */
		try {
			int msgId = MsgHandler.saveMsg(msgContent, sender_id, receiver_id);

			String sql = "select token from users where id=?";
			Map<Integer, Object> map = new HashMap<Integer, Object>();
			map.put(1, receiver_id);
			String token = (String) DBUtil.executeQuery(sql, map).get(0).get("token");

			JSONObject data = new JSONObject();
			data.put("msgId", String.valueOf(msgId));
			data.put("type", "notify");
			int code = FCMHandler.sendFCM(token, data);
			System.out.println(code);

			response.getWriter().append("msg_received");
		} catch (JSONException e) {
			// crash and burn
			throw new IOException("Error parsing JSON request string");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject msgStatus = new JSONObject();
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				sb.append(line);
			String req = sb.toString();
			JSONObject reqJSON = new JSONObject(req);

			String msgContent = reqJSON.getString("msgContent");
			int sender_id = reqJSON.getInt("senderId");
			int receiver_id = reqJSON.getInt("receiverId");
			int msgId = MsgHandler.saveMsg(msgContent, sender_id, receiver_id);

			String sql = "select Token from users where id=?";
			Map<Integer, Object> map = new HashMap<Integer, Object>();
			map.put(1, receiver_id);
			String token = (String) DBUtil.executeQuery(sql, map).get(0).get("token");

			//notify receiver there's a new msg via FCM
			JSONObject data = new JSONObject();
			data.put("msgId", String.valueOf(msgId));
			data.put("type", "notify");
			int code = FCMHandler.sendFCM(token, data);
			System.out.println("notify fcm status: " + code);
			
			//respond to sender that the msg received by server successfully
			msgStatus.put("status", "msg_received");
		} catch (JSONException e) {
			// crash and burn
			msgStatus.put("status", "Failed to parse the request.");
			throw new IOException("Error parsing JSON request string");
			
		} catch (Exception e) {
			msgStatus.put("status", "Failed to retrive receiver's FCM token.");
			e.printStackTrace();
		} finally {
			response.getWriter().print(msgStatus);
		}

	}

}
