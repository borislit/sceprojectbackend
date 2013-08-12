package sce.finalprojects.sceprojectbackend.runnables;

import sce.finalprojects.sceprojectbackend.datatypes.ArticleDO;
import sce.finalprojects.sceprojectbackend.datatypes.CacheToken;
import sce.finalprojects.sceprojectbackend.datatypes.LifecycleStageDO;
import sce.finalprojects.sceprojectbackend.factories.ArticlesFactory;
import sce.finalprojects.sceprojectbackend.managers.CacheManager;
import sce.finalprojects.sceprojectbackend.managers.LifecycleScheduleManager;

public class LifecycleSchedulerRunnable implements Runnable{
	private ArticleDO article;
	private String articleID;
	private CacheToken token;
	
	public LifecycleSchedulerRunnable(String articleID) {
		this.articleID = articleID;
	}

	@Override
	public void run() {
		
/*		if(this.token != null){
			this.article = (ArticleDO)CacheManager.getInstance().fetch(this.articleID, ObjectType.ARTICLE);
		}else{
			this.article = new ArticleDO(articleID); //TODO Stub
		}
*/
		
		ArticlesFactory factory = new ArticlesFactory();
		
		this.article = factory.get(this.articleID);
		
		System.out.println("Running "+this.articleID);
		
		this.token = CacheManager.getInstance().save(article, this.token);
		
		setupNextRun();
		
		complete();
		

		
	}
	
	private void setupNextRun(){
		
		long delay = calculateDelay();
		if(delay > 0){
			LifecycleScheduleManager.scheduleRun(this, delay);
		}
	}
	
	private long calculateDelay(){
		long age = article.getAge();
		for(LifecycleStageDO lcs: LifecycleScheduleManager.stages){
			if(age >= lcs.getFrom() && age < lcs.getTo())
				return (long) lcs.getInterval();
		}
		
		return -1;
	}
	
	private void complete(){
		this.article = null;
	}
	
	
}
