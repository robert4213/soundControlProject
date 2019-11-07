package com.project.model;

import java.sql.Connection;
import java.sql.DriverManager;


public class SqlDB {
	private Connection c = null;
	
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
	
	public Connection getC() {
		return c;
	}
	
	public void quit() {
		try{c.close();}
		catch(Exception e){
			
		}
	}
}
