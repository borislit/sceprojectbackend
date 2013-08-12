package sce.finalprojects.sceprojectbackend.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArticleSetupRequestDO {
	
	//{ url: composedUrl, count: ct.getCommnetsCount(), articleid:  content_id}
	
	String url;
	int commentsCount;
	String articleID;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getArticleID() {
		return articleID;
	}
	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}
	public int getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

}
