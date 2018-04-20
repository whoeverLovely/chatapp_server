package com.whoeverlovely.chatapp.servlet;

import java.io.BufferedReader;
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
 * Servlet implementation class tokenHandlerServlet
 */
public class TokenHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TokenHandlerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		String line = null;
		String resp = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				sb.append(line);
			String req = sb.toString();
			
			System.out.println("request is: " + req);
			JSONObject reqJSON = new JSONObject(req);

			String userId = reqJSON.getString("userId");
			String token = reqJSON.getString("token");

			if (userId != null && token != null) {
				String sqlQueryToken = "select token from users where id = ?";
				Map<Integer, Object> param = new HashMap<Integer, Object>();
				param.put(1, Integer.parseInt(userId));
				List<Map<String, Object>> result = DBUtil.executeQuery(sqlQueryToken, param);
				String tokenSaved = (String) result.get(0).get("token");

				JSONObject respJson = new JSONObject();
				if (!token.equals(tokenSaved)) {
					String sqlUpdateToken = "update users set token = ? where id = ?";
					Map<Integer, Object> paramUpdate = new HashMap<Integer, Object>();
					paramUpdate.put(1, token);
					paramUpdate.put(2, Integer.parseInt(userId));
					int updateResult = DBUtil.executeUpdate(sqlUpdateToken, paramUpdate);
					
					//if update successfully, respond "update success"
					if(updateResult != 0) {
						respJson.put("update_status", "update_success");
					}
						
					
 				} else {
 					//if token received is same as the existing one, respond "no update"
 					respJson.put("update_status", "no_update");
 				}
				resp = respJson.toString();
			}
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(resp);
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
