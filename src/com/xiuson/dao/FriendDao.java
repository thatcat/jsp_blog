package com.xiuson.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.xiuson.valuebean.*;

public class FriendDao {
	private String sql = "";
	private DB connection;
	private FriendBean friendBean;

	public FriendDao() {
		connection = new DB();
	}
	
	public DB getDB() {
		
		return connection;
	}
	
	public int queryFriendSum(){
		String sql = "select count(*) from tb_friend";
		int sum = 0;
		ResultSet rs = connection.executeQuery(sql);
		if (rs != null) {	
			try {
				rs.next();
				sum = rs.getInt(1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return sum;
	}

	/*
	 * 参数begin為返回好友記錄的起始位置，count返回起始位置后面的记录数，若begin==0 && end==0则返回全部
	 * 返回公式为(begin-1)*10+count
	 */
	public List<FriendBean> queryFriend(int begin,int count) {
		List<FriendBean> friendList = new ArrayList<FriendBean>();
		
		if (begin == 0 && count == 0) {
			sql = "select * from tb_friend order by id desc";
		} else
			sql = "select * from tb_friend order by id desc limit "+(begin-1)*10+","+count+"";
		
		System.out.println("test----------FriendDao.sql = "+sql);

		ResultSet rs = connection.executeQuery(sql);
		if (rs != null) {			
			try {
				while (rs.next()) {
					friendBean = new FriendBean();
					friendBean.setId(rs.getInt(1));
					friendBean.setBlog(rs.getString(2));
					friendBean.setLink(rs.getString(3));
					friendList.add(friendBean);
					System.out.println("test----------FriendDao.rs is not null");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return friendList;
	}
}
