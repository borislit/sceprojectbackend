package sce.finalprojects.sceprojectbackend.managers;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.CommentEntityDS;
import sce.finalprojects.sceprojectbackend.utils.HelperFunctions;
import sce.finalprojects.sceprojectbackend.textProcessing.TextProcessingManager;
import DataTypes.StatisticData;


public class MaintenanceDataManager {
	
	public static String[] commentsString;
	public static CommentEntityDS[] commentsArray;
	
	/**
	 * this function called when we want to update the HAC tree to get more comments of the article
	 * @param articleId- by this article id we will get data from the DB about all we done with this article
	 * @throws SQLException 
	 */
	public static ArrayList<CommentEntityDS> gettingCommentsForMaintenance(String articleId, int newNumOfComments) throws SQLException
	{
		int lastComment = DatabaseOperations.getArticleNumOfComments(articleId); //TODO get as a parameter
		commentsArray = new CommentEntityDS[newNumOfComments - lastComment];
		DatabaseOperations.setArticleNumOfComments(articleId, newNumOfComments);
		ArrayList<CommentEntityDS> arrayListOfComments = new ArrayList<CommentEntityDS>();

		try {
			URL url = new URL(DatabaseOperations.getUrl(articleId));
			int numOfThreads = HelperFunctions.getNumOfThreads(newNumOfComments, lastComment);
			commentsString = new String[numOfThreads];
			HelperFunctions.buildThreads(url, newNumOfComments, numOfThreads, lastComment, null, new MaintenanceDataManager());
		
			StringBuilder finalString = new StringBuilder();
			for(int i=0; i<numOfThreads; i++)
				finalString.append(commentsString[i]);
						
			TextProcessingManager cst = new TextProcessingManager();
			StatisticData[][] sd = cst.getTextResult(finalString.toString(),newNumOfComments - lastComment);
			ArrayList<String> newWordsArray = TextProcessingManager.wordsArray;
			ArrayList<ArrayList<Double>> commentsVectors = cst.vectorsCompletionForMaintenance(newWordsArray, sd, newNumOfComments - lastComment);
			
			DatabaseOperations.setArticleWords(articleId, TextProcessingManager.newWordsForTheArticle);//TODO delete when the server is ready
			//the DB function get array, need to change it so the function will get array list
			
			for(int i=0; i<commentsArray.length; i++)
			{
				commentsArray[i].setVector(commentsVectors.get(i));
				arrayListOfComments.add(commentsArray[i]);
			}
			
			DatabaseOperations.setComments(articleId, arrayListOfComments);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return arrayListOfComments;
	}
}
