package sce.finalprojects.sceprojectbackend.managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import sce.finalprojects.sceprojectbackend.datatypes.ArticleSetupRequestDO;
import sce.finalprojects.sceprojectbackend.datatypes.LifecycleStageDO;
import sce.finalprojects.sceprojectbackend.runnables.LifecycleSchedulerRunnable;

public class LifecycleScheduleManager {
	private static ScheduledExecutorService scheduler;
	
	
	public static final ArrayList<LifecycleStageDO> stages = new ArrayList<LifecycleStageDO>();
	
	private static final int THREAD_POOL_SIZE = 4;
	
	
	static {
		
		scheduler = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
		
		populateStages();

	}
	
	public static ScheduledFuture<?> createLifecycleForArticle(ArticleSetupRequestDO request){
		LifecycleSchedulerRunnable lsr = new LifecycleSchedulerRunnable(request);
		
		return scheduleRun(lsr, (long)stages.get(0).getInterval());
		
	}
	
	public static ScheduledFuture<?> scheduleRun(LifecycleSchedulerRunnable lsr, long delay){
		 return scheduler.schedule(lsr, delay , TimeUnit.SECONDS);
	}
	
	private static void populateStages(){
		BufferedReader br;
		String line;
		String[] stageElements = new String[3];
		
		try {

			 br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("lifecycle_stages.txt")));

			while ((line = br.readLine()) != null) {
				stageElements = line.split(",");
				stages.add(new LifecycleStageDO(Double.valueOf(stageElements[0]), Double.valueOf(stageElements[1]), Double.valueOf(stageElements[2])));
			}
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
