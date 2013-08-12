package sce.finalprojects.sceprojectbackend.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sce.finalprojects.sceprojectbackend.datatypes.Cachable;
import sce.finalprojects.sceprojectbackend.datatypes.CacheToken;
import sce.finalprojects.sceprojectbackend.managers.DatabaseManager;

public class DatabaseObjectCacheImpl {

	public static CacheToken save(Cachable obj, CacheToken token){
		Connection conn;
		String QUERY = "INSERT INTO POJOCache(cid, cachedContent) VALUES(?,?)";
		
		try {
			conn = DatabaseManager.getInstance().getConnection();

			PreparedStatement pstmt = conn.prepareStatement(QUERY);
	    
			byte[] serializedObject = getByteArrayObject(obj);
		
			pstmt.setString(1, token.getCacheID());
	    	pstmt.setBytes(2, serializedObject);
	    	
		     int affectedRows = pstmt.executeUpdate();
		     
		     if (affectedRows > 0) {
		    	 
		    	 return token;
		          //throw new SQLException("Insert failed, no rows affected.");
		     }
			
	    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	
	}
	
	public static Cachable fetch(String cacheID){

		Statement stmt = null;
		Connection conn = null;
		String QUERY = "SELECT * FROM POJOCache WHERE cid=\""+ cacheID + "\"";
		try {
			conn = DatabaseManager.getInstance().getConnection();
	        stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(QUERY);
	        
	        if(rs.next()) {
	      
	        	byte[] serializedObject =   rs.getBytes("cachedContent");
	        	
	        	return getJavaObject(serializedObject);
	        }
	        
	    } catch (SQLException e ) {
	    	
	    } finally {
	        if (stmt != null) { 
	        	try {
				stmt.close();
	        	} catch (SQLException e) {
				e.printStackTrace();
	        	} 
	        }
	    }
		return null;
		
		
	}
	
	public static void remove(String cacheID){
		 Statement stmt = null;
		 try {
			 Connection conn = DatabaseManager.getInstance().getConnection();
		 
		 	String query = "DELETE FROM POJOCache " +
                   "WHERE cid = '"+ cacheID + "'";
		 
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

    private static byte[] getByteArrayObject(Cachable obj){
        
        byte[] byteArrayObject = null;
        try {
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            byteArrayObject = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return byteArrayObject;
        }
        return byteArrayObject;
    } 
    
    private static Cachable getJavaObject(byte[] convertObject){
    	Cachable objObject = null;
        
        ByteArrayInputStream bais;
        ObjectInputStream ins;
        try {
        
        bais = new ByteArrayInputStream(convertObject);
        
        ins = new ObjectInputStream(bais);
         objObject =(Cachable)ins.readObject();
        
        ins.close();

        }
        catch (Exception e) {
        e.printStackTrace();
        }
        return objObject;
}
}
