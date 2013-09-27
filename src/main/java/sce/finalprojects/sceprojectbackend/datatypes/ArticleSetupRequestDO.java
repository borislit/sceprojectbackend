package sce.finalprojects.sceprojectbackend.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArticleSetupRequestDO {
	
	//{ url: composedUrl, count: ct.getCommnetsCount(), articleid:  content_id}
	
	String buildingURL;
	String maintenanceURL;
	int commentsCount;
	String articleID;
	String commentsAmountRetrievalURL;
	
	public ArticleSetupRequestDO(String url, String maintenanceURL, String commentsAmountRetrievalURL, int commentsCount,
			String articleID) {
		super();
		this.buildingURL = url;
		this.commentsCount = commentsCount;
		this.articleID = articleID;
		this.commentsAmountRetrievalURL = commentsAmountRetrievalURL;
		this.maintenanceURL = maintenanceURL;
	}

	
	public String getUrl() {
		return buildingURL;
	}
	public void setUrl(String url) {
		this.buildingURL = url;
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
	public String getCommentsAmountRetrievalURL() {
		return commentsAmountRetrievalURL;
	}
	public void setCommentsAmountRetrievalURL(String commentsAmountRetrievalURL) {
		this.commentsAmountRetrievalURL = commentsAmountRetrievalURL;
	}


	public String getBuildingURL() {
		return buildingURL;
	}


	public void setBuildingURL(String buildingURL) {
		this.buildingURL = buildingURL;
	}


	public String getMaintenanceURL() {
		return maintenanceURL;
	}


	public void setMaintenanceURL(String maintenanceURL) {
		this.maintenanceURL = maintenanceURL;
	}

}
