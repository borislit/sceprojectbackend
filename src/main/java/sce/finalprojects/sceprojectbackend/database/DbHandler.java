package sce.finalprojects.sceprojectbackend.database;
import sce.finalprojects.sceprojectbackend.datatypes.Comment;
import sce.finalprojects.sceprojectbackend.datatypes.MapCell;
import  sce.finalprojects.sceprojectbackend.managers.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DbHandler {
	
	

	
	/**
	 * return all the comments vectors that belongs to the article
	 * @param articleID 
	 * @return an arrayList of comments 
	 * @throws SQLException 
	 */
	public static ArrayList<Comment> getAllComentsWithoutHTML(String articleID) throws SQLException {
		
		//TODO: check what is the impact if the order is changed when returning the entries from the DB (when building the Mapping or XML )
		//SELECT vector FROM comments WHERE article_id = 'articleID'
		Connection conn = DatabaseManager.getInstance().getConnection();
		
		PreparedStatement sqlQuerry = conn.prepareStatement("SELECT comment_id,vector FROM comments WHERE article_id = ? ORDER BY comment_id ASC;");
		
		sqlQuerry.setString(1, articleID);
		
		ResultSet rs = sqlQuerry.executeQuery();
		
		ArrayList<Comment> ArrayOfComments = new ArrayList<Comment>();
		
		while(rs.next())
		{
			ArrayOfComments.add(new Comment(rs.getString("comment_id"),Comment.replaceStringWithVector(rs.getString("vector"))));
		}
		
		return ArrayOfComments;
	}
	
	/**
	 * save the article mapping
	 * @param articleId
	 * @param mapping
	 * @throws SQLException
	 */
	public static void setArticleMapping(String articleId, ArrayList<MapCell> mapping) throws SQLException {
		
		Connection conn = DatabaseManager.getInstance().getConnection();
		conn.setAutoCommit(false);
		//INSERT INTO `mydb`.`hacnodesmapping` (`articleid`, `commentid`, `node_mapping`) VALUES ('1', '12', 'dsa'), ('2', '12', 'dsa');
		
		PreparedStatement sqlQuerry = conn.prepareStatement("DELETE FROM HACNodesMapping WHERE  article_id = ? ;");
		sqlQuerry.setString(1, articleId);
		sqlQuerry.addBatch();
		
		String insertQuerry = "";
		for (MapCell mapCell : mapping) {
			insertQuerry += "('"+mapCell.getArticle_id()+"','"+mapCell.getComment_id()+"','"+mapCell.getMapping()+"'),";
		}
		insertQuerry = insertQuerry.substring(0, insertQuerry.length() - 1) + ";";
		
		sqlQuerry = conn.prepareStatement("INSERT INTO HACNodesMapping (articleid, commentid, node_mapping) VALUES "+insertQuerry);
		sqlQuerry.addBatch();
		
		sqlQuerry.executeBatch();
		conn.commit();
		conn.setAutoCommit(true);
		
	}
	
	/**
	 * return from DB the XML representation of the HAC for a given article
	 * @param articleID
	 * @return
	 * @throws SQLException 
	 */
	public static String getXMLRepresentation(String articleID) throws SQLException {
		
		Connection conn = DatabaseManager.getInstance().getConnection();
		
		PreparedStatement sqlQuerry = conn.prepareStatement("SELECT xmlRepresentation FROM articles WHERE article_id = ?;");
		
		sqlQuerry.setString(1, articleID);

		ResultSet rs = sqlQuerry.executeQuery();
		
		rs.next();
		String xmlrep= rs.getString("xmlRepresentation");
		
		if(xmlrep.length() == 0)
			return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
		
		//return xmlrep;
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Cluster id=\"1\" level=\"0\" mergeSim=\"1.0\"><Cluster id=\"6\" level=\"1\" mergeSim=\"0.9\"><Cluster id=\"10\" level=\"2\" mergeSim=\"1.0\"><Cluster id=\"10\" level=\"3\" mergeSim=\"1\"><Cluster id=\"10\" level=\"4\" mergeSim=\"1\"/></Cluster></Cluster><Cluster id=\"6\" level=\"2\" mergeSim=\"0.8\"><Cluster id=\"9\" level=\"3\" mergeSim=\"0.6\"><Cluster id=\"8\" level=\"4\" mergeSim=\"1.0\"/><Cluster id=\"9\" level=\"4\" mergeSim=\"0.6\"/></Cluster><Cluster id=\"6\" level=\"3\" mergeSim=\"0.4\"><Cluster id=\"7\" level=\"4\" mergeSim=\"1.0\"/><Cluster id=\"6\" level=\"4\" mergeSim=\"0.4\"/></Cluster></Cluster></Cluster><Cluster id=\"1\" level=\"1\" mergeSim=\"0.7\"><Cluster id=\"4\" level=\"2\" mergeSim=\"0.3\"><Cluster id=\"5\" level=\"3\" mergeSim=\"1.0\"><Cluster id=\"5\" level=\"4\" mergeSim=\"1\"/></Cluster><Cluster id=\"4\" level=\"3\" mergeSim=\"0.3\"><Cluster id=\"4\" level=\"4\" mergeSim=\"1\"/></Cluster></Cluster><Cluster id=\"1\" level=\"2\" mergeSim=\"0.5\"><Cluster id=\"3\" level=\"3\" mergeSim=\"1.0\"><Cluster id=\"3\" level=\"4\" mergeSim=\"1\"/></Cluster><Cluster id=\"1\" level=\"3\" mergeSim=\"0.2\"><Cluster id=\"2\" level=\"4\" mergeSim=\"1.0\"/><Cluster id=\"1\" level=\"4\" mergeSim=\"0.2\"/></Cluster></Cluster></Cluster></Cluster>";
		
	}
	
	/**
	 * save the xml representation
	 * @param articleId
	 * @param xmlHacRepresentation
	 * @throws SQLException
	 */
	public static void setXmlRepresentation(String articleId, String xmlHacRepresentation) throws SQLException {
		
		Connection conn = DatabaseManager.getInstance().getConnection();
		
		PreparedStatement sqlQuerry = conn.prepareStatement("UPDATE  articles SET xmlRepresentation = ?  WHERE  article_id = ? ;");
		
		sqlQuerry.setString(1, xmlHacRepresentation);
		sqlQuerry.setString(2, articleId);

		sqlQuerry.execute();

		//System.out.println(xmlHacRepresentation);
		
	}
	
	public static ArrayList<MapCell> getArticleMapping(String article_id) {
		
		//TODO: replace by real DB statement
		
		String temp = "1 10 10_3, 1 8 9_3, 1 9 9_3, 1 7 6_3, 1 6 6_3, 1 5 5_3, 1 4 4_3, 1 3 3_3, 1 2 1_3, 1 1 1_3, 1 10 10_2, 1 8 6_2, 1 9 6_2, 1 7 6_2, 1 6 6_2, 1 5 4_2, 1 4 4_2, 1 3 1_2, 1 2 1_2, 1 1 1_2, 1 10 6_1, 1 8 6_1, 1 7 6_1, 1 6 6_1, 1 9 6_1, 1 5 1_1, 1 4 1_1, 1 3 1_1, 1 2 1_1, 1 1 1_1, 1 8 1_0, 1 7 1_0, 1 6 1_0, 1 9 1_0, 1 10 1_0, 1 3 1_0, 1 5 1_0, 1 2 1_0, 1 4 1_0, 1 1 1_0";

		ArrayList<MapCell> returnarray = new ArrayList<MapCell>();
		String subs[] = temp.split("\\, ");
		
		for (String string : subs) {
			returnarray.add(new MapCell(string.split("\\ ")[0].trim(),string.split("\\ ")[1].trim(),string.split("\\ ")[2].trim()));
		}
		
		System.out.println(returnarray.toString());
		return returnarray;
		
	}
	
	
}
