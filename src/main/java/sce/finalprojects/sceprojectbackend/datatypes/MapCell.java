package sce.finalprojects.sceprojectbackend.datatypes;



public class MapCell {

	private String article_id;
	private String comment_id;
	private String mapping;  //the level is from the top to the bottom , the last level (leaves) is 0
	private int direct;

	public MapCell(String article_id, String comment_id, String mapping,int _direct) {
		super();
		this.article_id = article_id;
		this.mapping = mapping;
		this.comment_id = comment_id;
		this.direct = _direct;
	}
	
	@Override
	public String toString() {
		
		return this.article_id+" "+this.comment_id+" "+this.mapping+" "+this.direct;
		
	}
	
	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public String getArticle_id() {
		return article_id;
	}
	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}
	public String getMapping() {
		return mapping;
	}
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
	public String getComment_id() {
		return comment_id;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

}
