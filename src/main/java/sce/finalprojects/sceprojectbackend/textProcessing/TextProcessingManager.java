package sce.finalprojects.sceprojectbackend.textProcessing;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.utils.HelperFunctions;
import Configurations.Configurations;
import DataTypes.Document;
import DataTypes.StatisticData;
import FileLoader.TextLoader;
import Output.FinalResults;
import Output.TextFileWriter;
import SentenceSplitter.RegularExpressionAlgorithm;
import SentenceSplitter.SentenceSplitter;
import StatisticCalculations.StatisticCalculations;
import Stemmer.Stemmer;
import Tokenizer.Tokenizer;

public class TextProcessingManager {
	
	public int numOfWords = 0;
	public static ArrayList<String> wordsArray = new ArrayList<String>();
	public static ArrayList<String> newWordsForTheArticle = new ArrayList<String>();
	
	/**
	 * this function using the text processing tool (Igor) and do all the text processing
	 * @param finalString- the string that sent to the text processing
	 * @param numOfComments - the number of comments that going to go throw the process
	 * @return StatisticData sd that contain the result of the text processing
	 */
	public StatisticData[][] getTextResult(String finalString, int numOfComments) 
	{	
		StatisticData[][] sd = null;
		try {
			Configurations c = Configurations.createConfigurations();
			c.addProperty(Configurations.SAVING_PATH_KEY, "C:\\Users\\saritProj");//Here must be path for saving result files.
			c.addProperty(Configurations.WORDNET_DIC_PATH, "C:\\Users\\saritProj\\WordNet\\2.1\\dict");
			c.addProperty(Configurations.POS_TAGGER_PATH, "C:\\Users\\saritProj\\stanford-postagger-full-2012-11-11\\models\\english-caseless-left3words-distsim.tagger");
			TextLoader tl = new TextLoader(new Document(0, finalString));
			SentenceSplitter<RegularExpressionAlgorithm> sentenceSplitter = new SentenceSplitter<RegularExpressionAlgorithm>(new RegularExpressionAlgorithm());//Creating sentence splitter with RegularExpressionAlgorithm
			Tokenizer tokenizer = new Tokenizer(true);//Creating Tokenizer with options deleting stopwords
			//tokenizer.setUseWordNet(true);
			Stemmer stemmer = new Stemmer();//Creating porter stemmer
			StatisticCalculations statisticCalculator = new StatisticCalculations(StatisticCalculations.TF);//Creating statistic calculator with option of calculating TF.
			//TODO check
			TextFileWriter textFileWriter = new TextFileWriter("C:\\Users\\saritProj");//Creating text file writer for saving results into text files
			FinalResults finalResults = new FinalResults();//Creating container for saving final results of operations.
			tl.connectProcessToDocumentOut(sentenceSplitter, textFileWriter);//Connecting output of FileLoader to SentenceSplitter and FinalResults
			sentenceSplitter.connectProcessToSplittedDocumentOut(tokenizer, textFileWriter);//Connecting output of SentenceSplitter to Tokenizer
			tokenizer.connectProcessToTokenizeOut(stemmer);//Connecting output of Tokenizer to Stemmer
			stemmer.connectProcessToStemmerOut(statisticCalculator);//Connecting output of Stemmer to TextFileWriter,FinalResults and StatisticCalculator 
			statisticCalculator.connectProcessToStatisticsOut(finalResults, textFileWriter);//Connecting output of StatisticCalculator to TextFileWriter and FinalResult.
			tl.proceedDocument();
			sd = FinalResults.getStatisticsResultData().getResultMatrix();
		} catch (Exception e) {
			e.printStackTrace();
			}
		return sd;
	}

	/**
	 * this function build the matrix for words and comments 
	 * and also set the array of the words 
	 * matrix: row = word, column = comment
	 * @param sd - statisticDada that includes the results of the text processing
	 * @param numOfComments
	 * @return a matrix that present for each comment which words are in the comment
	 */	
	public Double[][] buildWordCommentMatrix(StatisticData[][] sd, int numOfComments)
	{
		numOfWords = sd[0].length;
		Double[][] commentsMatrix = new Double[numOfWords][numOfComments];
		Vector<Integer> commentVector;
		
		for(int i=0; i<numOfWords; i++)
		{
			wordsArray.add(sd[0][i].getTerm());//insert the words
			commentVector = sd[0][i].getListOfSentenceIndeces();			
			for(int j=0; j<commentVector.size();j++)
				commentsMatrix[i][commentVector.get(j)] = (double)1;
		}
		
		for(int i=0; i<numOfWords; i++)//fill the matrix with values 
		{
			for(int j=0; j<numOfComments; j++)
				if(commentsMatrix[i][j] == null)
					commentsMatrix[i][j] = (double)0;
		}

		return commentsMatrix;
	}
	
	/**
	 * @param matrix that we got from the buildWordCommentMatrix function that includes 
	 * a presentation  for each comment and the words that in the comment
	 * @param numOfComments
	 * @return an array that includes all the comments vectors
	 */
	public ArrayList<ArrayList<Double>> buildCommentsVector(Double[][] matrix, int numOfComments)
	{
		ArrayList<ArrayList<Double>> commentsVectors = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> vector;
		
		for(int i=0; i<numOfComments; i++)
		{
			vector = new ArrayList<Double>();
			for(int j=0; j<numOfWords; j++)
			{
				vector.add((double)matrix[j][i]);
			}
			commentsVectors.add(vector);
		}
		return commentsVectors;		
	}
	
	public ArrayList<ArrayList<Double>> vectorsCompletionForMaintenance(ArrayList<String> newWordsArray, StatisticData[][] sd, int numOfComments) throws SQLException
	{
		ArrayList<String> wordArray = HelperFunctions.addNewWordsToOldWords(newWordsArray);
		ArrayList<ArrayList<Double>> commentsVectors = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> vector;
		boolean flag = false;
		
		for(int i=0; i<numOfComments; i++)
		{
			vector = new ArrayList<Double>();
			for(int j=0 ;j<wordArray.size(); j++)
			{
				flag=false;
				Vector<Integer> vectorOfTheComment;
				for(int t=0; t<sd[0].length; t++)
				{
					if(sd[0][t].getTerm().equals(wordArray.get(j)))
					{
						vectorOfTheComment = sd[0][t].getListOfSentenceIndeces();
						for(int k=0; k<vectorOfTheComment.size(); k++)
							if(vectorOfTheComment.get(k) == i)
							{
								flag =true;
								break;
							}
					}
				}
				if(flag == true)
					vector.add((double)1);
				else
					vector.add((double)0);
			}
			commentsVectors.add(vector);
		}
		return commentsVectors;
	}
}
