package sce.finalprojects.sceprojectbackend.managers;

import sce.finalprojects.sceprojectbackend.config.CacheConfig;

public class ConfigurationManager {
	public static final String CACHE_CONFIG_FILE="caching_config.properties";
	public static final String DB_CONFIG_FILE = "db_config.properties";
	
	
	public static final CacheConfig cacheConfig;
	
	static{
		cacheConfig = new CacheConfig();
	}



}
