package sce.finalprojects.sceprojectbackend.managers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import sce.finalprojects.sceprojectbackend.config.DatabaseConfig;

public class DatabaseManager {
	
	
	private static DatabaseManager dbManager;
	
	
	private Connection conn;
	
	public static DatabaseManager getInstance(){
		if(dbManager == null)
			dbManager = new DatabaseManager();
		
		try {
			dbManager.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dbManager;
	}
	
	private DatabaseManager(){
		
	}
	

	public Connection getConnection() throws SQLException {

		if(this.conn != null) return this.conn;
		
	    	DatabaseConfig dbc = ConfigurationManager.dbConfig;
	    	 this.conn = DriverManager.getConnection(
	        		dbc.getConnectionString(),
	        		dbc.getUser(), dbc.getPassword());

	    return this.conn;
	}
	
	public void close(){
		
		if(this.conn == null) return;
		
		try {
			this.conn.close();
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
	}
	
	
	
}
