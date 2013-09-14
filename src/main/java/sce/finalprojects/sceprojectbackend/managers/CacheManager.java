package sce.finalprojects.sceprojectbackend.managers;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import sce.finalprojects.sceprojectbackend.config.CacheConfig;
import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.Cachable;
import sce.finalprojects.sceprojectbackend.datatypes.CacheToken;
import sce.finalprojects.sceprojectbackend.datatypes.LRUCache;

public class CacheManager {
	
	private static CacheManager instance;
	
	public enum CacheOrigin {
	    NEW, DB, MEMORY
	}
	
	public enum ObjectType {
	    ARTICLE, DOC, MAPPING, XML, ARRAYOFCOMMENTS
	}

	private HashMap<String, Cachable> cached = new HashMap<String, Cachable>(); //TODO Limit this in size
	private Map<String, CacheToken> cacheTokensLRUMap = Collections.synchronizedMap(new LRUCache<String, CacheToken>(2));

	private CacheManager(){

	}
	
	public static CacheManager getInstance(){
		if(instance == null)
			instance = new CacheManager();
		
		return instance;
	}
	
	public Cachable fetch(String contentID, CacheManager.ObjectType type){
		Cachable cachedObj = null;
		
		String cacheID = generateCacheID(contentID, type);
		
		CacheToken token = cacheTokensLRUMap.get(cacheID);
		
		if(token != null) 
			return fetch(token);
		
		cachedObj = cached.get(cacheID);
		
		if(cachedObj == null){
			cachedObj = DatabaseOperations.fetchFromCache(cacheID);
		}
		
		if(cachedObj != null){
			token = new CacheToken();
			token.setCacheID(cacheID);
			token.setOrigin(cachedObj.getOrigin());
			cacheTokensLRUMap.put(cacheID, token);
		}
		
		return cachedObj;
		
	}
	
	public Cachable fetch(CacheToken token){
		
		if(!cacheTokensLRUMap.containsKey(token.getCacheID())){
			cacheTokensLRUMap.put(token.getCacheID(), token);
		}
		
		switch(token.getOrigin()){
			case DB:
				return DatabaseOperations.fetchFromCache(token.getCacheID());
			case MEMORY:
				return cached.get(token.getCacheID());
			case NEW:
			default:
				return null;

		}
		
	}
	
	public CacheToken save(Cachable obj, CacheToken token){
		long doAge = obj.getAge();
		 String cacheID = null;
		 CacheConfig cacheConfig = ConfigurationManager.cacheConfig;
		 
		 if(token != null){
			 cacheID = token.getCacheID();
			 
		 }else{
			
			cacheID = generateCacheID(obj.getId(), obj.getType());
			
			token = new CacheToken();
			token.setCacheID(cacheID);
		}
		
		
		if(doAge <= cacheConfig.getCacheInMemoryUntil()){
			System.out.println("Item "+obj.getId()+" Moving to Memory, Age:"+doAge);
			token.setOrigin(CacheManager.CacheOrigin.MEMORY);
			cached.put(cacheID, obj);

		}else if(doAge <= cacheConfig.getCacheInDatabaseUntil()){
			System.out.println("Item "+obj.getId()+" Moving to DB, Age:"+doAge);
			removeFromMemoryCache(cacheID);
			
			token.setOrigin(CacheManager.CacheOrigin.DB);
			DatabaseOperations.saveToCache(obj, token); 
			
		}else{
			System.out.println("Item "+obj.getId()+" Removed, Age:"+doAge);
			removeFromMemoryCache(cacheID);
			removeFromDatabaseCache(cacheID);
		}
		
		cacheTokensLRUMap.put(token.getCacheID(), token);
		
		return token;
		
	}
	
	public static void clearCache(){

			DatabaseOperations.clearCache();
	}
	
	private void removeFromMemoryCache(String cacheID){
		if(cached.containsKey(cacheID)){
			cached.remove(cacheID);
		}
	}
	
	private void removeFromDatabaseCache(String cacheID){	
			DatabaseOperations.removeFromCache(cacheID);	
	}
	
	private String generateCacheID(String contentID, CacheManager.ObjectType type){
		String generatedCacheId = type.toString() + contentID;
		return generatedCacheId;
		//return new String(md.digest(generatedCacheId.getBytes()));
	}
	
}
