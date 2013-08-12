package sce.finalprojects.sceprojectbackend.datatypes;

import sce.finalprojects.sceprojectbackend.managers.CacheManager;

public class CacheToken {
	
	String cacheID;
	CacheManager.CacheOrigin origin = CacheManager.CacheOrigin.NEW;
	
	public String getCacheID() {
		return cacheID;
	}
	public void setCacheID(String cacheID) {
		this.cacheID = cacheID;
	}
	public CacheManager.CacheOrigin getOrigin() {
		return origin;
	}
	public void setOrigin(CacheManager.CacheOrigin origin) {
		this.origin = origin;
	}

}
