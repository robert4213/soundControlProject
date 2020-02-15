package com.project.model;

import java.sql.*;
import java.text.SimpleDateFormat;  
import java.util.Date;  


public class SqlDB {
	private static Connection c = null;
	private static SqlDB sql = null;
	
	public SqlDB(){
		try {
			Class.forName("org.sqlite.JDBC");
			System.out.println("Start to Connect to DB");
			c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/db.sqlite");
			c.setAutoCommit(false);
			System.out.println("Connect to DB");
		}catch(Exception e){
			System.out.println("Create DB: ");
			e.printStackTrace();
		}
	}

	public static SqlDB getInstance(){
		if(sql == null){
			sql = new SqlDB();
		}
		return sql;
	}
	
	public Connection getC() {
		return c;
	}
	
	public void quit() {
		try{c.close();}
		catch(Exception e){
			
		}
	}

	public void ececuteRecord(String command){
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
			Date date = new Date();  
			String time = formatter.format(date);
			Statement stm = c.createStatement();
			String sql = "INSERT INTO ExeRecord (Time,Command) " + "VALUES('" + time + "','" + command
					+ "');";
			// System.out.println(sql);
			stm.executeUpdate(sql);
			// System.out.println(rs);
			c.commit();
		}catch (Exception e){
			e.printStackTrace();
		}

	}
}
