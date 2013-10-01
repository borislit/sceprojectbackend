package sce.finalprojects.sceprojectbackend.datatypes;

import java.io.Serializable;
import java.util.Date;

import sce.finalprojects.sceprojectbackend.managers.CacheManager;

public class Cachable implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5264935015374659229L;
	
	private long creationDate;
	
	private CacheManager.CacheOrigin origin = CacheManager.CacheOrigin.NEW;
	private String id;
	private CacheManager.ObjectType type;


	public Cachable(String id,  CacheManager.ObjectType type) {
		super();
		this.id = id;
		this.type = type;
		this.creationDate = new Date().getTime();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public CacheManager.CacheOrigin getOrigin() {
		return origin;
	}

	public void setOrigin(CacheManager.CacheOrigin origin) {
		this.origin = origin;
	}


	public long getAge() {
		 long age = (System.currentTimeMillis() - this.creationDate);
		
		 return (age/1000);
	}

	public CacheManager.ObjectType getType() {
		return type;
	}

	public void setType(CacheManager.ObjectType type) {
		this.type = type;
	}

}
