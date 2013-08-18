package sce.finalprojects.sceprojectbackend.datatypes;

public class CommentEntityDS {
	
	private String id;	
	private String comment; 
	private String commentHTML;
	//vector of comment will be double
	private String articleId;
	
	public CommentEntityDS()
	{
		super();
		this.id = "0";
		this.comment = "";
		this.commentHTML = "";
		this.articleId = "";
	}
	
	public void printCommentEntity()
	{
		System.out.println("id: " + this.id);
		System.out.println("comment: " + this.comment);
		System.out.println("comment HTML: " + this.commentHTML);
		System.out.println("articleId: " + this.articleId);
		System.out.println();
	}
	
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
	
	public String getComment()
	{
		return this.comment;
	}
	
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	public String getArticleId()
	{
		return this.articleId;
	}
	
	public void setArticleId(String articleId)
	{
		this.articleId = articleId;
	}
}
