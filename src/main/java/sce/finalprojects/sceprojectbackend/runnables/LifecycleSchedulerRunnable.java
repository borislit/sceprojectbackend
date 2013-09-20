package sce.finalprojects.sceprojectbackend.runnables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import sce.finalprojects.sceprojectbackend.algorithms.EfficientHAC;
import sce.finalprojects.sceprojectbackend.algorithms.Maintenance;
import sce.finalprojects.sceprojectbackend.algorithms.xmlGenerator;
import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.ArrayOfCommentsDO;
import sce.finalprojects.sceprojectbackend.datatypes.ArticleSetupRequestDO;
import sce.finalprojects.sceprojectbackend.datatypes.ClusterRepresentationDO;
import sce.finalprojects.sceprojectbackend.datatypes.Comment;
import sce.finalprojects.sceprojectbackend.datatypes.CommentEntityDS;
import sce.finalprojects.sceprojectbackend.datatypes.LifecycleStageDO;
import sce.finalprojects.sceprojectbackend.factories.ArrayOfCommentsFactory;
import sce.finalprojects.sceprojectbackend.managers.LifecycleScheduleManager;
import sce.finalprojects.sceprojectbackend.managers.MaintenanceDataManager;

public class LifecycleSchedulerRunnable implements Callable<Set<ClusterRepresentationDO>>{
	private String articleID;
	private String articleUrl;
	private String commentsAmountURL;
	private int runsCounter;
	private int intialAmountOfComments;
	private long createTimestamp;
	

	public LifecycleSchedulerRunnable(ArticleSetupRequestDO request) {
		super();
		this.articleID = request.getArticleID();
		this.articleUrl = request.getUrl();
		this.intialAmountOfComments = request.getCommentsCount();
		this.commentsAmountURL = request.getCommentsAmountRetrievalURL();
		this.createTimestamp = System.currentTimeMillis();

	}
	
	private void setupNextRun(){
		
		long delay = calculateDelay();
		if(delay > 0){
			LifecycleScheduleManager.scheduleRun(this, delay);
		}
	}
	
	private long calculateDelay(){
		long age = System.currentTimeMillis() - this.createTimestamp;
		for(LifecycleStageDO lcs: LifecycleScheduleManager.stages){
			if(age >= lcs.getFrom() && age < lcs.getTo())
				return (long) lcs.getInterval();
		}
		
		return -1;
	}
	
	private void complete(){
		this.runsCounter++;
		this.intialAmountOfComments = 0;
	}

	@Override
	public Set<ClusterRepresentationDO> call() throws Exception {
		try{
		if(runsCounter == 0){
			
			DatabaseOperations.addNewArticle(this.articleID, this.articleUrl, this.intialAmountOfComments, this.commentsAmountURL);
			ArrayOfCommentsFactory commentFactory = new ArrayOfCommentsFactory();
			ArrayOfCommentsDO articleCommentsArray = commentFactory.get(this.articleID);
			commentFactory.save(articleCommentsArray);
			EfficientHAC effHAC = new EfficientHAC(articleCommentsArray.arrayOfComment, articleCommentsArray.vect);
			effHAC.runAlgorithm();
			xmlGenerator xmlGen = new xmlGenerator(this.articleID, effHAC.a, this.intialAmountOfComments);
			Maintenance maintenance = new Maintenance();
			maintenance.mapXmlHacToClusters(this.articleID);
			
			return DatabaseOperations.getHACRootID(this.articleID);

		}else{
			if(runsCounter%3 == 0){
				ArrayList<String> articleCommentsMarkup = DatabaseOperations.getAllArticleCommentsHtml(this.articleID);
				int latestCommentsCount = 0;
				ArrayList<CommentEntityDS> updatedArticleComments =  MaintenanceDataManager.gettingCommentsForMaintenance(this.articleUrl, this.articleID, latestCommentsCount, DatabaseOperations.getArticleNumOfComments(this.articleID), articleCommentsMarkup);
				ArrayOfCommentsDO commentsDO = new ArrayOfCommentsDO(this.articleID, Comment.convertCommentsDStoCommentsArrayList(updatedArticleComments));
				ArrayOfCommentsFactory commentFactory = new ArrayOfCommentsFactory();
				commentFactory.save(commentsDO);
				EfficientHAC effHAC = new EfficientHAC(commentsDO.arrayOfComment, commentsDO.vect);
				effHAC.runAlgorithm();
				xmlGenerator xmlGen = new xmlGenerator(this.articleID, effHAC.a, this.intialAmountOfComments);
				Maintenance maintenance = new Maintenance();
				maintenance.mapXmlHacToClusters(this.articleID);
				
			}else{
				
			}
		}
		

		setupNextRun();
		
		complete();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new HashSet<ClusterRepresentationDO>();
	}
	
	
}
