package sce.finalprojects.sceprojectbackend.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import sce.finalprojects.sceprojectbackend.managers.ConfigurationManager;

public class DatabaseConfig extends Config {
	private String  serverName;
	private String user;
	private String password;
	private String database;
	private String connectionString;
	private boolean clearDB;
	@Override
	public void init() {

	    Properties prop = new Properties();
	    
	    try {
	    	Class.forName("com.mysql.jdbc.Driver");
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(ConfigurationManager.DB_CONFIG_FILE));
	    
		    this.serverName = prop.getProperty("server");
		    this.user = prop.getProperty("username");
		    this.password  = "";
		    this.database  = prop.getProperty("database");
		    this.clearDB = Boolean.valueOf(prop.getProperty("cleardb"));
		    this.connectionString = "jdbc:mysql://" +
	                	serverName +
	                   "/" + database;

		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
	public boolean isClearDB() {
		return clearDB;
	}
	public void setClearDB(boolean clearDB) {
		this.clearDB = clearDB;
	}

}
