package sce.finalprojects.sceprojectbackend.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.managers.CacheManager;
import sce.finalprojects.sceprojectbackend.managers.ConfigurationManager;

public class ApplicationStartupListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		if(ConfigurationManager.cacheConfig.isInvalidateCache()){
			CacheManager.clearCache();
		}else{
			System.out.println("STARTUP:Cache kept");
		}
		
		if(ConfigurationManager.dbConfig.isClearDB()){
			System.out.println("STARTUP: Database Cleared");
			DatabaseOperations.cleaArticleFromDB();
		}
		
		
	}

}
