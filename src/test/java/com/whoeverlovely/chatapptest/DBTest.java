package com.whoeverlovely.chatapptest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.whoeverlovely.chatapp.DBUtil;

public class DBTest {

	public void testInsert() {
		
		String sql = "insert into messages(message_content,sender_id,receiver_id) values(?,?,?)";
		Map<Integer,Object> msgParam = new HashMap<Integer,Object>();
		msgParam.put(1, "hello world,test msg 2");
		msgParam.put(2, 1);
		msgParam.put(3, 2);

		int i = 0;
		try {
			i = DBUtil.executeUpdate(sql, msgParam);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(i);
	}
	
	@Test
	public void testSelect() {
		
		String sql = "select * from messages where id = ?";
		Map<Integer,Object> msgParam = new HashMap<Integer,Object>();
		msgParam.put(1, 2);

		List<Map<String, Object>> i = null;
		try {
			i = DBUtil.executeQuery(sql, msgParam);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Map<String, Object> map : i) {
			System.out.println(map.toString());
		}
	}
}
