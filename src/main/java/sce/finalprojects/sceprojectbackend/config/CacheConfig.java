package sce.finalprojects.sceprojectbackend.config;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import sce.finalprojects.sceprojectbackend.managers.ConfigurationManager;


public class CacheConfig extends Config {
	private  double  cacheInMemoryUntil;
	private  double cacheInDatabaseUntil;
	private  double stopCachingAfter;
	private  boolean invalidateCache;

	public CacheConfig(){
		super();
	}


	public boolean isInvalidateCache() {
		return invalidateCache;
	}
	
	public double getCacheInMemoryUntil() {
		return cacheInMemoryUntil;
	}


	public double getCacheInDatabaseUntil() {
		return cacheInDatabaseUntil;
	}


	public double getStopCachingAfter() {
		return stopCachingAfter;
	}

	@Override
	public void init() {
		
		 Properties prop = new Properties();
		 
		 try {
			 
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(ConfigurationManager.CACHE_CONFIG_FILE));
			
			this.cacheInMemoryUntil = Double.valueOf(prop.getProperty("cache_in_memory_until"));
			this.cacheInDatabaseUntil = Double.valueOf(prop.getProperty("cache_in_db_until"));
			this.stopCachingAfter = Double.valueOf(prop.getProperty("stop_caching_after"));
			this.invalidateCache = Boolean.valueOf(prop.getProperty("invalidate_cache"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
