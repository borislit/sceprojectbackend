package sce.finalprojects.sceprojectbackend.factories;
import java.util.ArrayList;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.ArrayOfCommentsDO;
import sce.finalprojects.sceprojectbackend.datatypes.Comment;
import sce.finalprojects.sceprojectbackend.datatypes.CommentEntityDS;
import sce.finalprojects.sceprojectbackend.managers.BuildingTreeDataManager;
import sce.finalprojects.sceprojectbackend.managers.CacheManager;
import sce.finalprojects.sceprojectbackend.managers.CacheManager.ObjectType;

public class ArrayOfCommentsFactory extends BaseFactory<ArrayOfCommentsDO> {

	@Override
	/**
	 * This method will get the array of all comments from the DB if the article exist
	 * if the article doesn't exist will call to the getter and save the comments
	 * ASSUMTION: setNewArticle called before! calling to this Factory
	 */
	protected ArrayOfCommentsDO handle(String articleId){
		
		ArrayList<Comment> arrayOfComments = DatabaseOperations.getAllComentsWithoutHTML(articleId);
		ArrayOfCommentsDO commentsDO;

		//in case the article comments doesn't exist 	
		if(arrayOfComments == null || arrayOfComments.size() == 0)
		{
			ArrayList<CommentEntityDS> commentsDSArray = BuildingTreeDataManager.gettingCommentsForTheFirstTime(DatabaseOperations.getUrl(articleId),articleId,DatabaseOperations.getArticleNumOfComments(articleId));
			//this.checker(commentsDSArray);
			DatabaseOperations.setComments(articleId, commentsDSArray); //save the comments in the DB
			//save the comments array in the cache
			commentsDO = new ArrayOfCommentsDO(articleId, Comment.convertCommentsDStoCommentsArrayList(commentsDSArray));
			return commentsDO;
		}

		commentsDO = new ArrayOfCommentsDO(articleId, arrayOfComments);
		return commentsDO; // new ArrayOfCommentsDO(articleId);
	}
	
	@Override
	protected ObjectType getType() {
		return CacheManager.ObjectType.ARRAYOFCOMMENTS;
	}
	
//	public void checker(ArrayList<CommentEntityDS> ca){
//		boolean flag = false;
//		for (CommentEntityDS commentEntityDS : ca) {
//			flag = true;
//			ArrayList<Double> vec = commentEntityDS.getVector();
//			for (Double dub : vec) {
//				if(dub != 0)
//				{
//					flag = false;
//					break;
//				}	
//			}
//			if (flag)
//			{
//				commentEntityDS.getVector().set(0, (double) 1);
//				System.out.println(commentEntityDS.getId());
//			}
//		}
//		
//	}


}
