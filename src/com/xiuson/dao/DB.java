package com.xiuson.dao;

import java.sql.*;

public class DB {

	// 数据库URL
	private String url = "jdbc:mysql://localhost:3306/db_blog";//db_blog为你自己在MySQL用来保存博客数据表的数据库
	
	
	private String userName ="root";//数据库账号

	
	private String password ="？？？？？？";//？为数据库密码

	
	// 数据库驱动类路径
	private String className = "com.mysql.jdbc.Driver";
	private Connection con = null;
	private Statement stm = null;

	/**
	 * 通过构造方法加载数据库驱动
	 */
	public DB() {
		try {
			Class.forName(className).newInstance();
		} catch (Exception e) {
		//	e.printStackTrace();
			System.out.println("加载数据库驱动失败");
		}
	}

	/**
	 * 创建数据库连接
	 */
	public void createCon() {
		try {
			// 建立连接，连接到由属性url指定的数据库URL，并指定登陆数据库的账号密码。
			con = DriverManager.getConnection(url, userName, password);
		} catch (SQLException e) {
		//	e.printStackTrace();
			System.out.println("获取数据库连接失败");
		}
	}

	/**
	 * 执行StatMent对象
	 */
	public void getStm() {
		createCon();
		try {
			// 调用Connection类实例的createStatement()方法获取一个StateMent类对象
			stm = con.createStatement();
		} catch (Exception e) {
		//	e.printStackTrace();
			System.out.println("创建Statement对象失败");
		}
	}

	/**
	 * 功能：对数据库的增加、修改、和删除的操作 参数：sql为要执行的SQL语句 返回boolean值
	 */
	public boolean executeUpdate(String sql) {
		boolean mark = false;
		try {
			getStm();
			int iCount = stm.executeUpdate(sql);
			if (iCount > 0)
				mark = true;
			else
				mark = false;
		} catch (Exception e) {
		//	e.printStackTrace();
			System.out.println("更新失败");
			mark = false;
		}
		return mark;
	}

	/**
	 * 查询数据库
	 */
	public ResultSet executeQuery(String sql) {
		ResultSet rs = null;
		try {
			getStm();
			try {
				rs = stm.executeQuery(sql);
			} catch (Exception e) {
			//	e.printStackTrace();
			//	System.out.println();
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return rs;
	}
	
	public void close() {
		/*
		try {
			if(con != null )// || !con.isClosed()
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		*/
	}

}
