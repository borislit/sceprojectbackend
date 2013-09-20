package sce.finalprojects.sceprojectbackend.datatypes;

import java.util.ArrayList;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;

import sce.finalprojects.sceprojectbackend.managers.CacheManager;

public class ArrayOfCommentsDO extends Cachable{
	
	public ArrayList<Comment> arrayOfComment;
	
	public double[] vect;

	public ArrayOfCommentsDO(String articleId,ArrayList<Comment> _arrayOfComment) {
		
		super(articleId, CacheManager.ObjectType.ARRAYOFCOMMENTS);
		this.arrayOfComment = _arrayOfComment;
		this.vect = setVector(articleId);
	}
	
	 private double[] setVector(String articleId) {
		return new double[DatabaseOperations.getWordsCountForArticle(articleId) + 1];  //plus 1 -  the artificial number 
	}
	 
	 //TODO: check if it does the job
	 private void addNewCommentsToOldCommentsArray(ArrayList<Comment> newComments) {
		 this.arrayOfComment.addAll(newComments);
	 }

	/**
	 * 
	 */
	private static final long serialVersionUID = -4583735157048203313L;

}
