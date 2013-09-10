package sce.finalprojects.sceprojectbackend.algorithms;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import sce.finalprojects.sceprojectbackend.managers.BuildingTreeDataManager;
import sce.finalprojects.sceprojectbackend.managers.MaintenanceDataManager;


public class Driver {
	
	public static int numOfComments = 100;
	public static String articleId = "aaa";
	
	public static void main(String[] args) throws FileNotFoundException
	{
			String url = new String("http://news.yahoo.com/_xhr/contentcomments/get_comments/?content_id=7f838e1d-b04c-3c98-8117-33b9d6aa0996&_device=full&count=10&sortBy=highestRated&isNext=true&offset=10&pageNumber=1");	

			//first build
			//			try {
//				BuildingTreeDataManager.gettingCommentsForTheFirstTime(url, articleId, numOfComments);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
		
			//maintenance
		try {
			MaintenanceDataManager.gettingCommentsForMaintenance(url,articleId, numOfComments,90);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
