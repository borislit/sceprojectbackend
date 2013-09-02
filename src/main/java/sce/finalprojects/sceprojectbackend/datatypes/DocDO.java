package sce.finalprojects.sceprojectbackend.datatypes;

import org.w3c.dom.Document;

import sce.finalprojects.sceprojectbackend.managers.CacheManager;

public class DocDO extends Cachable{
	
	public Document doc;

	public DocDO(String DocArticleId,Document _doc) {
		
		super(DocArticleId, CacheManager.ObjectType.DOC);
		this.doc = _doc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4583735157048203313L;

}
