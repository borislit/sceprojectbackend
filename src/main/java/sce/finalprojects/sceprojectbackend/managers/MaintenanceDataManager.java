package sce.finalprojects.sceprojectbackend.managers;

import java.net.MalformedURLException;
import java.net.URL;
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
	 */
	public static void gettingCommentsForMaintenance(String articleId, int newNumOfComments)
	{
		int lastComment = DatabaseOperations.getArticleNumOfComments(articleId);
		commentsArray = new CommentEntityDS[newNumOfComments - lastComment];
		DatabaseOperations.setArticleNumOfComments(articleId, newNumOfComments);
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
			Double[][] wordCommentsMatrix = cst.buildWordCommentMatrix(sd, newNumOfComments - lastComment);
			//DbHandler.insertWordsAndArticleId(TextProcessingManager.wordsArray, articleId);
			ArrayList<ArrayList<Double>> commentsVectors = cst.buildCommentsVector(wordCommentsMatrix, newNumOfComments - lastComment);
		
			//TODO check how to insert the words, need algo that build the new vector considering the old words that exist in the DB
			
			ArrayList<String> newWordsArray = TextProcessingManager.wordsArray;
			ArrayList<ArrayList<Double>> temp = cst.vectorsCompletionForMaintenance(newWordsArray, sd, newNumOfComments - lastComment);
			
			
			
			
			
//			for(int i=0; i<commentsArray.length; i++)
//				commentsArray[i].setVector(commentsVectors.get(i));

			
			DatabaseOperations.setComments(commentsArray, articleId);//TODO change to arrayList
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
