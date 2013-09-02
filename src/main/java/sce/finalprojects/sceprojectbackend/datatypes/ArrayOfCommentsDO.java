package sce.finalprojects.sceprojectbackend.datatypes;

import java.util.ArrayList;

import sce.finalprojects.sceprojectbackend.managers.CacheManager;

public class ArrayOfCommentsDO extends Cachable{
	
	public ArrayList<Comment> arrayOfComment;

	public ArrayOfCommentsDO(String ArticleId,ArrayList<Comment> _arrayOfComment) {
		
		super(ArticleId, CacheManager.ObjectType.ARRAYOFCOMMENTS);
		this.arrayOfComment = _arrayOfComment;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4583735157048203313L;

}
