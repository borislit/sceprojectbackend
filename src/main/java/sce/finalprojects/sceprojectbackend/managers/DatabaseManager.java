package sce.finalprojects.sceprojectbackend.managers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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
		
	    Connection conn = null;
	    Properties prop = new Properties();
	    
	    try {
	    	Class.forName("com.mysql.jdbc.Driver");
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(ConfigurationManager.DB_CONFIG_FILE));
	    
		    String serverName = prop.getProperty("server");
		    String user = prop.getProperty("username");
		    String password  = "";
		    String database  = prop.getProperty("database");
		    String connectionString = "jdbc:mysql://" +
	                	serverName +
	                   "/" + database;
	        conn = DriverManager.getConnection(
	        			connectionString,
	                   user, password);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	    
	    this.conn = conn;
	    
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
