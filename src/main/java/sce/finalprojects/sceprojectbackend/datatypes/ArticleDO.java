package sce.finalprojects.sceprojectbackend.datatypes;

import sce.finalprojects.sceprojectbackend.managers.CacheManager;

public class ArticleDO extends Cachable{

	public ArticleDO(String id) {
		super(id, CacheManager.ObjectType.ARTICLE);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4583735157048203313L;

}
