package sce.finalprojects.sceprojectbackend.factories;

import java.io.FileNotFoundException;

import sce.finalprojects.sceprojectbackend.datatypes.Cachable;
import sce.finalprojects.sceprojectbackend.datatypes.CacheToken;
import sce.finalprojects.sceprojectbackend.managers.CacheManager;

public abstract class BaseFactory<T extends Cachable> {
	
	private CacheManager cacheManager = CacheManager.getInstance();
	
	@SuppressWarnings("unchecked")
	public final T get(String id) throws FileNotFoundException{
		Cachable cachedObject = cacheManager.fetch(id,  getType());
		
		//in case the object found
		if(cachedObject != null)
			return (T)cachedObject;
		
		//in case the object didn't exist in the cache
		return handle(id);
	}
	
	@SuppressWarnings("unchecked")
	public final T get(CacheToken token){
		Cachable cachedObject = cacheManager.fetch(token);
		
		return (T)cachedObject;
		
		//TODO Try to come up with fallback - BORIS
	}
	
	public final CacheToken save(Cachable obj){
		return cacheManager.save(obj, null);
	}
	
	abstract protected T handle(String id) throws FileNotFoundException; 
	
	abstract protected CacheManager.ObjectType getType();

}
