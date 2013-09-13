package sce.finalprojects.sceprojectbackend.datatypes;
import sce.finalprojects.sceprojectbackend.datatypes.CommentEntityDS;
import java.util.ArrayList;

/**
 * Comment with the required methods
 * @author Yuval Simhon
 *
 */
public class Comment {

	public String comment_id;  ///the inner serial number

	public ArrayList<Double> vector;


	/**
	 * constructor that get id of comment and vector arry and create a comment from it
	 * @param id
	 * @param vec
	 */
	public Comment(String id, ArrayList<Double> vec){
		this.comment_id = id;
		this.vector = new ArrayList<Double>(vec);
	}
	
	/**
	 * make a copy from the comment
	 * @param com
	 */
	public Comment(Comment com){
		this.comment_id = com.comment_id;
		this.vector = new ArrayList<Double>(com.vector);
	}

	public Comment(String id, double[] ds) {
		this.comment_id = id;
		this.vector = new ArrayList<Double>();
		for(int i = 0 ; i < ds.length ; i ++)
			this.vector.add(ds[i]);
	}

	/**
	 * duplicate the comment array
	 * @param comarr
	 * @return
	 */
	public static Comment[] duplicateArray (Comment[] comarr){

		Comment[] returnArray = new Comment[comarr.length];
		for (int i=0 ; i < comarr.length ; i++) {
			returnArray[i] = new Comment(comarr[i]);
		}
		return returnArray;


	}
	
	/**
	 * make the vector of the comment normalized
	 * change the vector components to be normalized
	 * @param com
	 */
	public static void nomalizeCommentVector(Comment com){
		double lenghtOfVector = 0;
		int arraySize = com.vector.size();
		double element = 0;
		for(int i=0 ; i < arraySize ; i++)
		{
			element = com.vector.get(i);
			lenghtOfVector += Math.pow(element, 2);
		}
		lenghtOfVector = Math.sqrt(lenghtOfVector);
		for(int i=0 ; i < arraySize ; i++)
		{
			com.vector.set(i, (com.vector.get(i)/lenghtOfVector) );
		}
	}

	/**
	 * convert a string to an vector array
	 * @param stringVector
	 * @return
	 */
	public static ArrayList<Double> replaceStringWithVector(String stringVector) {
		
		ArrayList<Double> vect = new ArrayList<Double>();
		
		String[] temp = stringVector.split("\\,");
		
		for (String string : temp) {
			vect.add(Double.parseDouble(string));
		}
		
		return vect;
		
	}
	
	/**
	 * convert the DS comments to Comments array list
	 * @param commentsDS
	 * @return
	 */
		
	public static ArrayList<Comment> convertCommentsDStoCommentsArrayList(ArrayList<CommentEntityDS> commentsDS) {
		ArrayList<Comment> returnArray = new ArrayList<Comment>();
		
		for (CommentEntityDS comment : commentsDS) {
			returnArray.add(new Comment(comment.getId(), comment.getVector()));
		}
		
		return returnArray;
		
	}

}