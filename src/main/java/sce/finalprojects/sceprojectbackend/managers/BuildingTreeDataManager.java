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


public class BuildingTreeDataManager {
	
	public static String[] commentsString;
	public static CommentEntityDS[] commentsArray;

	/**
	 * this function called the first time we want to build the HAC tree, by the plug in
	 * @param url of the current article
	 * @param articleId
	 * @param numOfComments that the article have
	 * @throws SQLException 
	 */
	public static ArrayList<CommentEntityDS> gettingCommentsForTheFirstTime(String urlString, String articleId, int numOfComments){
		//DatabaseOperations.addNewArticle(articleId, url.toString(), numOfComments); //TODO delete when the server is ready
		commentsArray = new CommentEntityDS[numOfComments];
		int numOfThreads = HelperFunctions.getNumOfThreads(numOfComments, 0);
		commentsString = new String[numOfThreads];

		try {
			URL url = new URL(urlString);
			HelperFunctions.buildThreads(url, numOfComments, numOfThreads, 0, new BuildingTreeDataManager(), null);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		StringBuilder finalString = new StringBuilder();
		for(int i = 0; i < numOfThreads; i++)
			finalString.append(commentsString[i]);
		
		TextProcessingManager cst = new TextProcessingManager();
		StatisticData[][] sd = cst.getTextResult(finalString.toString(),numOfComments);
		Double[][] wordCommentsMatrix = cst.buildWordCommentMatrix(sd, numOfComments);
		ArrayList<ArrayList<Double>> commentsVectors = cst.buildCommentsVector(wordCommentsMatrix, numOfComments);
		
		//DbHandler.setArticleWords(articleId, TextProcessingManager.wordsArray);//TODO delete when the server is ready
		
		ArrayList<CommentEntityDS> arrayListOfComments = new ArrayList<CommentEntityDS>();
		for(int i = 0; i < numOfComments; i++){
			commentsArray[i].setVector(commentsVectors.get(i));
			arrayListOfComments.add(commentsArray[i]);
		}
		
		//DatabaseOperations.setComments(articleId, arrayListOfComments);//TODO delete when the server is ready
	
		return arrayListOfComments;
	}
}
