package sce.finalprojects.sceprojectbackend.managers;

import java.net.URL;
import java.util.ArrayList;
import sce.finalProject.dataBaseManager.DbHandler;
import sce.finalProject.dataStructures.CommentEntityDS;
import sce.finalProject.miscellaneousness.HelperFunctions;
import sce.finalProject.textProcessing.TextProcessingManager;
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
		DbHandler.addNewArticle(articleId, url.toString(), numOfComments);
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
		DbHandler.setArticleWords(TextProcessingManager.wordsArray, articleId);
		ArrayList<ArrayList<Double>> commentsVectors = cst.buildCommentsVector(wordCommentsMatrix, numOfComments);
		
		for(int i=0; i<numOfComments; i++)
			commentsArray[i].setVector(commentsVectors.get(i));
		
//		//TODO delete printing
//		for(int i=0; i<numOfComments;i++)
//			commentsArray[i].printCommentEntity();
		
		DbHandler.addComments(commentsArray, articleId);
	}
}
