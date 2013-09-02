package sce.finalprojects.sceprojectbackend.managers;

import java.net.URL;
import java.util.ArrayList;
import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.CommentEntityDS;
import sce.finalprojects.sceprojectbackend.utils.HelperFunctions;
import sce.finalprojects.sceprojectbackend.textProcessing.TextProcessingManager;
import DataTypes.StatisticData;


public class BuildingTreeDataManager {
	
	public static String[] commentsString;
	public static CommentEntityDS[] commentsArray;

	/**
	 * this function called the first time we want to build the HAC tree, by the plug in
	 * @param url of the current article
	 * @param articleId
	 * @param numOfComments that the article have
	 */
	public static void gettingCommentsForTheFirstTime(URL url, String articleId, int numOfComments)
	{
		DatabaseOperations.addNewArticle(articleId, url.toString(), numOfComments); //TODO delete when the server is ready
		commentsArray = new CommentEntityDS[numOfComments];
		int numOfThreads = HelperFunctions.getNumOfThreads(numOfComments, 0);
		commentsString = new String[numOfThreads];
		HelperFunctions.buildThreads(url, numOfComments, numOfThreads, 0, new BuildingTreeDataManager(), null);
		
		StringBuilder finalString = new StringBuilder();
		for(int i=0; i<numOfThreads; i++)
			finalString.append(commentsString[i]);
		
		TextProcessingManager cst = new TextProcessingManager();
		StatisticData[][] sd = cst.getTextResult(finalString.toString(),numOfComments);
		Double[][] wordCommentsMatrix = cst.buildWordCommentMatrix(sd, numOfComments);
		DatabaseOperations.setArticleWords(TextProcessingManager.wordsArray, articleId);//TODO delete when the server is ready
		ArrayList<ArrayList<Double>> commentsVectors = cst.buildCommentsVector(wordCommentsMatrix, numOfComments);
		
		for(int i=0; i<numOfComments; i++)
			commentsArray[i].setVector(commentsVectors.get(i));
		
		DatabaseOperations.setComments(commentsArray, articleId);//TODO delete when the server is ready
		//TODO change to array list and return array list of comments insteat of saving in DB
	}
}
