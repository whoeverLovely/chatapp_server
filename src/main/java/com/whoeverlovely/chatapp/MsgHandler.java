package com.whoeverlovely.chatapp;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MsgHandler {
	
	public static int saveMsg(String msgContent, int sender_id, int receiver_id) {
		String sqlInsert = "insert into messages(message_content,sender_id,receiver_id) values(?,?,?)";
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		map.put(1, msgContent);
		map.put(2, sender_id);
		map.put(3, receiver_id);
		
		int msgId = 0;
		try {
			msgId = DBUtil.executeUpdate(sqlInsert, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return msgId;
	}
	
	@Test
	public void test() {
		System.out.println(saveMsg("today is Jan 14th.", 1, 1));
	}

}
