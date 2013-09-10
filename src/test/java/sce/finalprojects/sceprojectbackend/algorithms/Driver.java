package sce.finalprojects.sceprojectbackend.algorithms;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import sce.finalprojects.sceprojectbackend.managers.MaintenanceDataManager;


public class Driver {
	
	public static int numOfComments = 10;
	public static String articleId = "aaa";
	
	public static void main(String[] args) throws FileNotFoundException
	{
//		try {
//			//859 comments
//			URL url = new URL("http://news.yahoo.com/_xhr/contentcomments/get_comments/?content_id=7f838e1d-b04c-3c98-8117-33b9d6aa0996&_device=full&count=10&sortBy=highestRated&isNext=true&offset=10&pageNumber=1");
//			
//			//98 comments
//			//URL url = new URL("http://news.yahoo.com/_xhr/contentcomments/get_comments/?content_id=75015988-79e9-36ff-ba6b-c49de15c28cd&_device=full&count=100&sortBy=highestRated&isNext=true&offset=0&pageNumber=1");
//			
//			BuildingTreeDataManager.gettingCommentsForTheFirstTime(url, articleId, numOfComments);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
		try {
			MaintenanceDataManager.gettingCommentsForMaintenance(articleId, numOfComments);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
