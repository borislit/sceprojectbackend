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
	public static ArrayList<CommentEntityDS> gettingCommentsForMaintenance(String urlString, String articleId, int newNumOfComments, int lastComment, ArrayList<String> htmlArr) throws SQLException, FileNotFoundException{
		commentsArray = new CommentEntityDS[newNumOfComments - lastComment];
		int numOfThreads = HelperFunctions.getNumOfThreads(newNumOfComments, lastComment);
		commentsString = new String[numOfThreads];
		ArrayList<ArrayList<Double>> commentsVectors = new ArrayList<ArrayList<Double>>();
		ArrayList<CommentEntityDS> arrayListOfComments = new ArrayList<CommentEntityDS>();
				
		//DatabaseOperations.setArticleNumOfComments(articleId, newNumOfComments);//TODO delete when the server is ready

		PrintWriter out = new PrintWriter("C:\\\\vectors.txt"); ////TODO delete after testing

		
		try {
			URL url = new URL(urlString);
			HelperFunctions.buildThreads(url, newNumOfComments, numOfThreads, lastComment, null, new MaintenanceDataManager());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		StringBuilder finalString = new StringBuilder();
		if(htmlArr != null){
			CommentsDownloadManager cdm = new CommentsDownloadManager();
			for(int i=0; i<htmlArr.size(); i++)
				finalString.append(cdm.cleanTheCommentFromTheHtml(htmlArr.get(i)));
		}
		for(int i=0; i<numOfThreads; i++)
			finalString.append(commentsString[i]);

		if(htmlArr != null){
			TextProcessingManager cst = new TextProcessingManager();
			StatisticData[][] sd = cst.getTextResult(finalString.toString(),newNumOfComments);
			Double[][] wordCommentsMatrix = cst.buildWordCommentMatrix(sd, newNumOfComments);
			commentsVectors = cst.buildCommentsVector(wordCommentsMatrix, newNumOfComments);
			
			for(int i=0; i<commentsArray.length; i++){
				commentsArray[i].setVector(commentsVectors.get(i + lastComment));
				out.println(i+1 + ": " + commentsVectors.get(i));//TODO delete after testing
				out.println();		
				out.println();
				
				
				arrayListOfComments.add(commentsArray[i]);
			}
		}
		else{
			TextProcessingManager cst = new TextProcessingManager();
			StatisticData[][] sd = cst.getTextResult(finalString.toString(),newNumOfComments - lastComment);
			ArrayList<String> newWordsArray = TextProcessingManager.wordsArray;
			commentsVectors = cst.vectorsCompletionForMaintenance(newWordsArray, sd, newNumOfComments - lastComment);
			
			for(int i=0; i<commentsArray.length; i++){
				commentsArray[i].setVector(commentsVectors.get(i));
				out.println(commentsArray[i].getId() + ": " + commentsVectors.get(i));//TODO delete after testing
				out.println();		
				out.println();
				
			
				arrayListOfComments.add(commentsArray[i]);
			}
		}
	
		//DatabaseOperations.setArticleWords(articleId, TextProcessingManager.newWordsForTheArticle);//TODO delete when the server is ready
		//DatabaseOperations.setComments(articleId, arrayListOfComments);//TODO delete when the server is ready
		out.close();
		return arrayListOfComments;
	}
}
