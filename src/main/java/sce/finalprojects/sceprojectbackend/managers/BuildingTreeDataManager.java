package sce.finalprojects.sceprojectbackend.managers;

import java.io.FileNotFoundException;
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
	 * @param buildingURL of the current article
	 * @param articleId
	 * @param numOfComments that the article have
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 */
	public static ArrayList<CommentEntityDS> gettingCommentsForTheFirstTime(String urlString, String articleId, int numOfComments){

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
		
		DatabaseOperations.setArticleWords(articleId, TextProcessingManager.wordsArray);
		ArrayList<CommentEntityDS> arrayListOfComments = new ArrayList<CommentEntityDS>();

		System.out.println("array: " + commentsArray.length + "numOfComments: " + numOfComments + "  vectors: " + commentsVectors.size());
		for(int i = 0; i < numOfComments; i++){
			if(commentsArray[i] != null){
				commentsArray[i].setVector(commentsVectors.get(i));
				arrayListOfComments.add(commentsArray[i]);
			}
			else 
				System.out.println("null");
		}
		//TODO set the num of comments in the DB by the sizze of the arrayListOfComments
		try {
			DatabaseOperations.setArticleNumOfComments(articleId, arrayListOfComments.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arrayListOfComments;
	}
}