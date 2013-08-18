package sce.finalprojects.sceprojectbackend.datatypes;

import java.util.ArrayList;

public class CommentEntityDS {
	
	private String id;	
	private String commentHTML;
	private ArrayList<Double> vector;

	public ArrayList<Double> getVector() {
		return vector;
	}

	public void setVector(ArrayList<Double> vector) {
		this.vector = vector;
	}

	public CommentEntityDS() {}
	
	public String getId()
	{
		return this.id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	
	public String getCommentHTML()
	{
		return this.commentHTML;
	}
	
	public void setCommentHTML(String html)
	{
		this.commentHTML = html;
	}
}
	