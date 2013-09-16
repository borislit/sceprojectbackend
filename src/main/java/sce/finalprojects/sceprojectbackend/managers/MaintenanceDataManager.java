package sce.finalprojects.sceprojectbackend.managers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
	 * @throws FileNotFoundException 
	 */
	public static ArrayList<CommentEntityDS> gettingCommentsForMaintenance(String urlString, String articleId, int newNumOfComments, int lastComment, ArrayList<String> htmlArr) throws SQLException{
		
		int amountOfComments = newNumOfComments - lastComment;
		commentsArray = new CommentEntityDS[amountOfComments];
		int numOfThreads = HelperFunctions.getNumOfThreads(newNumOfComments, lastComment);
		commentsString = new String[numOfThreads];
		ArrayList<ArrayList<Double>> commentsVectors = new ArrayList<ArrayList<Double>>();
		ArrayList<CommentEntityDS> arrayListOfComments = new ArrayList<CommentEntityDS>();
				
		DatabaseOperations.setArticleNumOfComments(articleId, newNumOfComments);

		try {
			URL url = new URL(urlString);
			HelperFunctions.buildThreads(url, newNumOfComments, numOfThreads, lastComment, null, new MaintenanceDataManager());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		StringBuilder finalString = new StringBuilder();
		if(htmlArr != null){
			CommentsDownloadManager cdm = new CommentsDownloadManager();
			int htmlArrSize = htmlArr.size();
			for(int i=0; i<htmlArrSize; i++)
				finalString.append(cdm.cleanTheCommentFromTheHtml(htmlArr.get(i)));
		}
		for(int i=0; i<numOfThreads; i++)
			finalString.append(commentsString[i]);

		if(htmlArr != null){
			TextProcessingManager cst = new TextProcessingManager();
			StatisticData[][] sd = cst.getTextResult(finalString.toString(),newNumOfComments);
			Double[][] wordCommentsMatrix = cst.buildWordCommentMatrix(sd, newNumOfComments);
			commentsVectors = cst.buildCommentsVector(wordCommentsMatrix, newNumOfComments);
			
			for(int i=0; i<amountOfComments; i++){
				commentsArray[i].setVector(commentsVectors.get(i + lastComment));
				arrayListOfComments.add(commentsArray[i]);
			}
			
			DatabaseOperations.setArticleWords(articleId, TextProcessingManager.wordsArray);
		}
		else{
			TextProcessingManager cst = new TextProcessingManager();
			StatisticData[][] sd = cst.getTextResult(finalString.toString(),newNumOfComments - lastComment);
			ArrayList<String> newWordsArray = TextProcessingManager.wordsArray;
			commentsVectors = cst.vectorsCompletionForMaintenance(newWordsArray, sd, (newNumOfComments - lastComment), articleId);
			
			for(int i=0; i<amountOfComments; i++){
				commentsArray[i].setVector(commentsVectors.get(i));
				arrayListOfComments.add(commentsArray[i]);
			}
			
			//the words saved in the data base in the method vectorsCompletionForMaintenance
		}
		
		return arrayListOfComments;
	}
}
