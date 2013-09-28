package sce.finalprojects.sceprojectbackend.managers;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.CommentEntityDS;
import sce.finalprojects.sceprojectbackend.utils.HelperFunctions;
import sce.finalprojects.sceprojectbackend.utils.MarkupUtility;
import sce.finalprojects.sceprojectbackend.textProcessing.TextProcessingManager;
import DataTypes.StatisticData;

public class MaintenanceDataManager {
	
	public static String[] commentsString;
	public static CommentEntityDS[] commentsArray;
	private static CommentsDownloadManager cdm;
	public static String[] arrayOfKeys;
	/**
	 * this function called when we want to update the HAC tree to get more comments of the article
	 * @param articleId- by this article id we will get data from the DB about all we done with this article
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 */
	public static ArrayList<CommentEntityDS> gettingCommentsForMaintenance(String urlString, String articleId, int newNumOfComments, int lastComment) throws SQLException, FileNotFoundException{

		int amountOfComments = newNumOfComments - lastComment;
		commentsArray = new CommentEntityDS[amountOfComments];
		int numOfThreads = HelperFunctions.getNumOfThreads(newNumOfComments, lastComment);
		commentsString = new String[numOfThreads];
		ArrayList<ArrayList<Double>> commentsVectors = new ArrayList<ArrayList<Double>>();
		ArrayList<CommentEntityDS> arrayListOfComments = new ArrayList<CommentEntityDS>();
		cdm = new CommentsDownloadManager();
		int numOfKeys = newNumOfComments/100 + 1;
		arrayOfKeys = new String[numOfKeys]; 
		
		if(lastComment <= 110)		
		arrayOfKeys[0] = getKeyFromURL(urlString);
		
		else{
			String tempUrl = urlString;
			for(int i=0; i<lastComment; i++)
			{
				if(i == 0){
					arrayOfKeys[i] = cdm.getJsonObjectFromYahoo(tempUrl);
				}
				
			}
		}
		
		//TODO get all the keys if the last comment is larger then 100
		

		try {
			URL url = new URL(urlString);
			int num;
			if((newNumOfComments / 100) == (lastComment / 100)){
				cdm.getCommentsByUrlForMaintenance(url, newNumOfComments, 1, lastComment, arrayOfKeys[0]);
				commentsString[0] = cdm.getCommentString();
			}
			else{
				num = newNumOfComments - lastComment;
				for(int i = 0; i < numOfThreads; i++){
					if(i == 0){
						int temp = ((lastComment/100) + 1) * 100;
						cdm.getCommentsByUrlForMaintenance(url, (temp - lastComment), i+1, lastComment, arrayOfKeys[i]);
						commentsString[i] = cdm.getCommentString();
						num -= (temp - lastComment);
					}
					else{
						if(num > 100){
							num -= 100;
							cdm.getCommentsByUrlForMaintenance(url, 100, i+1, lastComment, arrayOfKeys[i]);
							commentsString[i] = cdm.getCommentString();
						}
						else{
							cdm.getCommentsByUrlForMaintenance(url, num, i+1, lastComment, arrayOfKeys[i]);
							commentsString[i] = cdm.getCommentString();
						}
					}
				}
			}
				
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		StringBuilder finalString = new StringBuilder();
		for(int i=0; i<numOfThreads; i++)
			finalString.append(commentsString[i]);

		TextProcessingManager cst = new TextProcessingManager();
		StatisticData[][] sd = cst.getTextResult(finalString.toString(),newNumOfComments - lastComment);
		ArrayList<String> newWordsArray = TextProcessingManager.wordsArray;
		commentsVectors = cst.vectorsCompletionForMaintenance(newWordsArray, sd, (newNumOfComments - lastComment), articleId);
		
		System.out.println("array: " + commentsArray.length + "numOfComments: " + amountOfComments + "  vectors: " + commentsVectors.size());

		for(int i=0; i<commentsArray.length; i++){
			if(commentsArray[i] != null){
				commentsArray[i].setVector(commentsVectors.get(i));
				arrayListOfComments.add(commentsArray[i]);
			}
			else
				System.out.println(i + ": null");
		}
		//the words saved in the data base in the method vectorsCompletionForMaintenance

		DatabaseOperations.setArticleNumOfComments(articleId, (DatabaseOperations.getArticleNumOfComments(articleId) + arrayListOfComments.size()));

		return arrayListOfComments;
		
	}
	
	
	/**
	 * this function called when we want to update the HAC tree to get more comments of the article
	 * @param articleId- by this article id we will get data from the DB about all we done with this article
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 */
	public static ArrayList<CommentEntityDS> gettingCommentsForReBuilding(String urlString, String articleId, int newNumOfComments, int lastComment, ArrayList<String> htmlArr) throws SQLException, FileNotFoundException{

		int amountOfComments = newNumOfComments - lastComment;
		commentsArray = new CommentEntityDS[amountOfComments];
		int numOfThreads = HelperFunctions.getNumOfThreads(newNumOfComments, lastComment);
		commentsString = new String[numOfThreads];
		ArrayList<ArrayList<Double>> commentsVectors = new ArrayList<ArrayList<Double>>();
		ArrayList<CommentEntityDS> arrayListOfComments = new ArrayList<CommentEntityDS>();
		cdm = new CommentsDownloadManager();
		int numOfKeys = newNumOfComments/100 + 1;
		arrayOfKeys = new String[numOfKeys]; 
						
		arrayOfKeys[0] = getKeyFromURL(urlString);
		//TODO get all the keys if the last comment is larger then 100
		
		try {
			URL url = new URL(urlString);
			int num;
			if((newNumOfComments / 100) == (lastComment / 100)){
				cdm.getCommentsByUrlForMaintenance(url, newNumOfComments, 1, lastComment, arrayOfKeys[0]);
				commentsString[0] = cdm.getCommentString();
			}
			else{
				num = newNumOfComments - lastComment;
				for(int i = 0; i < numOfThreads; i++){
					if(i == 0){
						int temp = ((lastComment/100) + 1) * 100;
						cdm.getCommentsByUrlForMaintenance(url, (temp - lastComment), i+1, lastComment, arrayOfKeys[i]);
						commentsString[i] = cdm.getCommentString();
						num -= (temp - lastComment);
					}
					else{
						if(num > 100){
							num -= 100;
							cdm.getCommentsByUrlForMaintenance(url, 100, i+1, lastComment, arrayOfKeys[i]);
							commentsString[i] = cdm.getCommentString();
						}
						else{
							cdm.getCommentsByUrlForMaintenance(url, num, i+1, lastComment, arrayOfKeys[i]);
							commentsString[i] = cdm.getCommentString();
						}
					}
				}
			}
				
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		StringBuilder finalString = new StringBuilder();
		CommentsDownloadManager cdm = new CommentsDownloadManager();
		int htmlArrSize = htmlArr.size();
		for(int i=0; i<htmlArrSize; i++)
			finalString.append(cdm.prepareCommentToTextProcessing(MarkupUtility.getCommentBodyFromMarkup(htmlArr.get(i))) + " ");

		for(int i=0; i<numOfThreads; i++)
			finalString.append(commentsString[i]);
		TextProcessingManager cst = new TextProcessingManager();
		StatisticData[][] sd = cst.getTextResult(finalString.toString(),newNumOfComments);
		Double[][] wordCommentsMatrix = cst.buildWordCommentMatrix(sd, newNumOfComments);
		commentsVectors = cst.buildCommentsVector(wordCommentsMatrix, newNumOfComments);
		
		ArrayList<ArrayList<Double>> oldCommentsVectors = new ArrayList<ArrayList<Double>>();
		for(int i=0; i<lastComment;i++)
			oldCommentsVectors.add(commentsVectors.get(i));
		DatabaseOperations.replaceVectorsForComments(articleId, oldCommentsVectors);
		
		System.out.println("array: " + commentsArray.length + "numOfComments: " + amountOfComments + "  vectors: " + commentsVectors.size());
		for(int i=0; i<amountOfComments; i++){
			if(commentsArray[i] != null){
				commentsArray[i].setVector(commentsVectors.get(i + lastComment));
				arrayListOfComments.add(commentsArray[i]);
			}
			else 
				System.out.println("null");
		}
		
		DatabaseOperations.setArticleWords(articleId, TextProcessingManager.wordsArray);

		DatabaseOperations.setArticleNumOfComments(articleId, (DatabaseOperations.getArticleNumOfComments(articleId) + arrayListOfComments.size()));

		return arrayListOfComments;
	}
	
	public static String getKeyFromURL(String url)
	{
		String[] temp = url.split("exprKey=Ascending");
		temp = temp[1].split("isNext=true");
		
		return temp[0];
	}
	
	
	
	
	
	
	
//	/**
//	 * this function called when we want to update the HAC tree to get more comments of the article
//	 * @param articleId- by this article id we will get data from the DB about all we done with this article
//	 * @throws SQLException 
//	 * @throws FileNotFoundException 
//	 */
//	public static ArrayList<CommentEntityDS> gettingCommentsForMaintenance(String urlString, String articleId, int newNumOfComments, int lastComment, ArrayList<String> htmlArr) throws SQLException{
//	
//	int amountOfComments = newNumOfComments - lastComment;
//	commentsArray = new CommentEntityDS[amountOfComments];
//	int numOfThreads = HelperFunctions.getNumOfThreads(newNumOfComments, lastComment);
//	commentsString = new String[numOfThreads];
//	ArrayList<ArrayList<Double>> commentsVectors = new ArrayList<ArrayList<Double>>();
//	ArrayList<CommentEntityDS> arrayListOfComments = new ArrayList<CommentEntityDS>();
//			
//	DatabaseOperations.setArticleNumOfComments(articleId, newNumOfComments);
//
//	try {
//		URL url = new URL(urlString);
//		HelperFunctions.buildThreads(url, newNumOfComments, numOfThreads, lastComment, null, new MaintenanceDataManager());
//	} catch (MalformedURLException e) {
//		e.printStackTrace();
//	}
//	StringBuilder finalString = new StringBuilder();
//	if(htmlArr != null){
//		CommentsDownloadManager cdm = new CommentsDownloadManager();
//		int htmlArrSize = htmlArr.size();
//		for(int i=0; i<htmlArrSize; i++)
//			finalString.append(cdm.prepareCommentToTextProcessing(MarkupUtility.getCommentBodyFromMarkup(htmlArr.get(i))) + " ");
//
//	}
//	for(int i=0; i<numOfThreads; i++)
//		finalString.append(commentsString[i]);
//
//	if(htmlArr != null){
//		TextProcessingManager cst = new TextProcessingManager();
//		StatisticData[][] sd = cst.getTextResult(finalString.toString(),newNumOfComments);
//		Double[][] wordCommentsMatrix = cst.buildWordCommentMatrix(sd, newNumOfComments);
//		commentsVectors = cst.buildCommentsVector(wordCommentsMatrix, newNumOfComments);
//		
//		ArrayList<ArrayList<Double>> oldCommentsVectors = new ArrayList<ArrayList<Double>>();
//		for(int i=0; i<lastComment;i++)
//			oldCommentsVectors.add(commentsVectors.get(i));
//		DatabaseOperations.replaceVectorsForComments(articleId, oldCommentsVectors);
//		
//		System.out.println("array: " + commentsArray.length + "numOfComments: " + amountOfComments + "  vectors: " + commentsVectors.size());
//		for(int i=0; i<amountOfComments; i++){
//			if(commentsArray[i] != null){
//				commentsArray[i].setVector(commentsVectors.get(i + lastComment));
//				arrayListOfComments.add(commentsArray[i]);
//			}
//			else 
//				System.out.println("null");
//		}
//		//ArrayList<String> a = DatabaseOperations.getArticleWords(articleId);
//		
//		DatabaseOperations.setArticleWords(articleId, TextProcessingManager.wordsArray);
////		System.out.println(a);
////		System.out.println(TextProcessingManager.wordsArray);
//		
////		for(int i=0; i<a.size();i++)
////			if(!(a.get(i).equals(TextProcessingManager.wordsArray.get(i))))
////				System.out.println("not equals");
//				
//	}
//	else{
//		TextProcessingManager cst = new TextProcessingManager();
//		StatisticData[][] sd = cst.getTextResult(finalString.toString(),newNumOfComments - lastComment);
//		ArrayList<String> newWordsArray = TextProcessingManager.wordsArray;
//		commentsVectors = cst.vectorsCompletionForMaintenance(newWordsArray, sd, (newNumOfComments - lastComment), articleId);
//		
//		System.out.println("array: " + commentsArray.length + "numOfComments: " + amountOfComments + "  vectors: " + commentsVectors.size());
//		for(int i=0; i<amountOfComments; i++){
//			if(commentsArray[i] != null){
//				commentsArray[i].setVector(commentsVectors.get(i));
//				arrayListOfComments.add(commentsArray[i]);
//			}
//			else 
//				System.out.println("null");
//		}
//		
//		//the words saved in the data base in the method vectorsCompletionForMaintenance
//	}
//	
//	//TODO check hoe to save the new num of comments
//	return arrayListOfComments;
//}
	
}
