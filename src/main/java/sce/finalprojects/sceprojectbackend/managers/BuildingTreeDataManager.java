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
	 * @param numOfComments that the article have, the number of comments we are going to get
	 * @throws SQLException 
	 */
	public static ArrayList<CommentEntityDS> gettingCommentsForTheFirstTime(String urlString, String articleId, int numOfComments){

		commentsArray = new CommentEntityDS[numOfComments];
		int numOfThreads = HelperFunctions.getNumOfThreads(numOfComments, 0);
		commentsString = new String[numOfThreads];

		try { //get the data from yahoo site by threads
			URL url = new URL(urlString);
			HelperFunctions.buildThreads(url, numOfComments, numOfThreads);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		StringBuilder finalString = new StringBuilder();
		for(int i = 0; i < numOfThreads; i++)
			finalString.append(commentsString[i]);
		
		//text processing
		TextProcessingManager cst = new TextProcessingManager();
		StatisticData[][] sd = cst.getTextResult(finalString.toString(),numOfComments);
		Double[][] wordCommentsMatrix = cst.buildWordCommentMatrix(sd, numOfComments);
		ArrayList<ArrayList<Double>> commentsVectors = cst.buildCommentsVector(wordCommentsMatrix, numOfComments);
		
		DatabaseOperations.setArticleWords(articleId, TextProcessingManager.wordsArray);//save the word of the article in the DB
		
		ArrayList<CommentEntityDS> arrayListOfComments = new ArrayList<CommentEntityDS>();
		for(int i = 0; i < numOfComments; i++){
			if(commentsArray[i] != null){
				commentsArray[i].setVector(commentsVectors.get(i));
				arrayListOfComments.add(commentsArray[i]);
			}
			else{
				System.out.println(i + ": null");//TODO delete after testing
				continue;
			}
		}
		try {//set the num of comments in the DB by the size of the arrayListOfComments - TODO check
			DatabaseOperations.setArticleNumOfComments(articleId, arrayListOfComments.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arrayListOfComments;
	}
}