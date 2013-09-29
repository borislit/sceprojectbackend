package sce.finalprojects.sceprojectbackend.utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.textProcessing.TextProcessingManager;

public class HelperFunctions {
	
	/**
	 * this function calculate how many threads we need to run
	 * @param numOfComments we want to get from yahoo
	 * @param lastComment - the number of comments that we already got and have in the DB
	 * @return the number of threads to run
	 */
	public static int getNumOfThreads(int numOfComments, int lastComment){
		int numOfThreads;
		int num = numOfComments - ((lastComment/100)*100);
		
		if(num % 100 == 0)
			numOfThreads = num / 100;
		else
			numOfThreads = num / 100 + 1;
		return numOfThreads;
	}
	
	/**
	 * this function runs all the threads we neet to get all the comments we asking, each threads asks 100 comments max
	 * @param url
	 * @param numOfComments we asking from yahoo
	 * @param numOfThreads
	 */
	public static void buildThreads(URL url, int numOfComments, int numOfThreads){
		CommentsWorkerThread[] threadsArray = new CommentsWorkerThread[numOfThreads];
		int num = numOfComments;
		
		for(int i = 0; i < numOfThreads; i++){
			if(num > 100){
				num -= 100;
				threadsArray[i] = new CommentsWorkerThread(i+1, url, 100);
			}
			else
				threadsArray[i] = new CommentsWorkerThread(i+1, url, num);
			threadsArray[i].start();
		}

		for(int i = 0; i < threadsArray.length; i++)
			try {
				threadsArray[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * the function add the new words that returned from the text processing
	 * in the maintenance stage to the old words of the article that saved in the DB
	 * @param newWordsArray ncludes the new words from the new comments
	 * @param articleId
	 * @return array list that contain all the words of the article
	 * @throws SQLException
	 */
	public static ArrayList<String> addNewWordsToOldWords(ArrayList<String> newWordsArray, String articleId) throws SQLException{
		ArrayList<String> result = new ArrayList<String>();
		
		ArrayList<String> oldWords = DatabaseOperations.getArticleWords(articleId);
		boolean isWordExist;
		int sizeOfOldWords = oldWords.size();
		int sizeOfNewWords = newWordsArray.size();
		
		for(int i = 0; i < sizeOfOldWords; i++)
			result.add(oldWords.get(i));
		
		for(int i = 0; i < sizeOfNewWords; i++){
			isWordExist = false;
			int resultSize = result.size();
			for(int j = 0; j < resultSize; j++)
				if(result.get(j).equals(newWordsArray.get(i))){
					isWordExist = true;
					break;
				}
			if(isWordExist == false){
				result.add(newWordsArray.get(i));
				TextProcessingManager.newWordsForTheArticle.add(newWordsArray.get(i));
			}
		}
		return result; //the array with all the words of the article
	}
}
