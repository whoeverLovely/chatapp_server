package com.whoeverlovely.chatapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.whoeverlovely.chatapp.DBUtil;

/**
 * Servlet implementation class ForwardServlet
 */
public class ForwardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForwardServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msgId = request.getParameter("msgId");
		String sql = "select * from messages where id = ?";
		Map<Integer,Object> map = new HashMap<Integer,Object>();
		map.put(1, msgId);
		List<Map<String, Object>> resultList = null;
		try {
			resultList = DBUtil.executeQuery(sql, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String,Object> requestedMsg = resultList.get(0);
		JSONObject msgJSON = new JSONObject(requestedMsg);
		String senderId = String.valueOf(msgJSON.getInt("sender_id"));
		String messageContent = (String) requestedMsg.get("message_content");
		String sqlGetSenderName = "select user_name from users where id = ?";
		Map<Integer,Object> mapSenderName = new HashMap<Integer,Object>();
		mapSenderName.put(1, senderId);
		List<Map<String, Object>> senderNameResultList = null;
		try {
			senderNameResultList = DBUtil.executeQuery(sqlGetSenderName, mapSenderName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String senderName = (String) senderNameResultList.get(0).get("user_name");
		msgJSON.put("senderName", senderName);
		msgJSON.put("senderId", senderId);
		msgJSON.put("message_content", messageContent);
		
		PrintWriter out = response.getWriter();
		out.print(msgJSON.toString());
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
