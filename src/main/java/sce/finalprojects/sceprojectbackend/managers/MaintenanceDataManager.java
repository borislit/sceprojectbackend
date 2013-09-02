package sce.finalprojects.sceprojectbackend.managers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import sce.finalProject.dataBaseManager.DbHandler;
import sce.finalProject.dataStructures.CommentEntityDS;
import sce.finalProject.miscellaneousness.HelperFunctions;
import sce.finalProject.textProcessing.TextProcessingManager;
import DataTypes.StatisticData;


public class MaintenanceDataManager {
	
	public static String[] commentsString;
	public static CommentEntityDS[] commentsArray;
	
	/**
	 * this function called when we want to update the HAC tree to get more comments of the article
	 * @param articleId- by this article id we will get data from the DB about all we done with this article
	 */
	public static void gettingCommentsForMaintenance(String articleId, int newNumOfComments)
	{
		int lastComment = DbHandler.getArticleNumOfComments(articleId);
		commentsArray = new CommentEntityDS[newNumOfComments - lastComment];
		DbHandler.setArticleNumOfComments(articleId, newNumOfComments);
		try {
			URL url = new URL(DbHandler.getUrl(articleId));
			int numOfThreads = HelperFunctions.getNumOfThreads(newNumOfComments, lastComment);
			commentsString = new String[numOfThreads];
			HelperFunctions.buildThreads(url, newNumOfComments, numOfThreads, lastComment, null, new MaintenanceDataManager());
		
			StringBuilder finalString = new StringBuilder();
			for(int i=0; i<numOfThreads; i++)
				finalString.append(commentsString[i]);
						
			TextProcessingManager cst = new TextProcessingManager();
			StatisticData[][] sd = cst.getTextResult(finalString.toString(),newNumOfComments - lastComment);
			Double[][] wordCommentsMatrix = cst.buildWordCommentMatrix(sd, newNumOfComments - lastComment);
			//DbHandler.insertWordsAndArticleId(TextProcessingManager.wordsArray, articleId);
			ArrayList<ArrayList<Double>> commentsVectors = cst.buildCommentsVector(wordCommentsMatrix, newNumOfComments - lastComment);
		
			//TODO check how to insert the words, need algo that build the new vector considering the old words that exist in the DB
			
			ArrayList<String> newWordsArray = TextProcessingManager.wordsArray;
			ArrayList<ArrayList<Double>> temp = cst.vectorsCompletionForMaintenance(newWordsArray, sd, newNumOfComments - lastComment);
			
			
			
			
			
//			for(int i=0; i<commentsArray.length; i++)
//				commentsArray[i].setVector(commentsVectors.get(i));
//			
//			//TODO delete printing
//			for(int i=0;i<commentsArray.length;i++)
//				commentsArray[i].printCommentEntity();
			
			DbHandler.addComments(commentsArray, articleId);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
