package sce.finalprojects.sceprojectbackend.factories;

import sce.finalprojects.sceprojectbackend.datatypes.Cachable;
import sce.finalprojects.sceprojectbackend.datatypes.CacheToken;
import sce.finalprojects.sceprojectbackend.managers.CacheManager;

public abstract class BaseFactory<T extends Cachable> {
	
	private CacheManager cacheManager = CacheManager.getInstance();
	
	@SuppressWarnings("unchecked")
	public final T get(String id){
		Cachable cachedObject = cacheManager.fetch(id,  getType());
		
		if(cachedObject != null)
			return (T)cachedObject;
		
		return handle(id);
	}
	
	@SuppressWarnings("unchecked")
	public final T get(CacheToken token){
		Cachable cachedObject = cacheManager.fetch(token);
		
		return (T)cachedObject;
		
		//TODO Try to come up with fallback - BORIS
	}
	
	public final void save(Cachable obj){
		cacheManager.save(obj, null);
	}
	
	abstract protected T handle(String id); 
	
	abstract protected CacheManager.ObjectType getType();

}
