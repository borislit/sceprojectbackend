package sce.finalprojects.sceprojectbackend.utils;

import java.io.FileNotFoundException;
import java.net.URL;
import sce.finalprojects.sceprojectbackend.managers.*;



public class CommentsWorkerThread extends Thread{
	
	int threadId;
	URL url;
	int numOfComments;
	int requestingNum;
	String articleId;
	CommentsDownloadManager cdm = new CommentsDownloadManager();
	BuildingTreeDataManager btdm = null;
	MaintenanceDataManager mdm = null;
	int lastComment;
	
	/**
	 * constructor
	 * @param id of the thread
	 * @param url
	 * @param numOfComments we want to get from yahoo in the specific thread
	 * @param btdm - BuildingTreeDataManager object
	 * @param mdm - MaintenanceDataManager object
	 * @param lastComment- the comment that we want to start from 
	 */
	public CommentsWorkerThread(int id, URL url,int numOfComments, BuildingTreeDataManager btdm, MaintenanceDataManager mdm, int lastComment)
	{
		this.threadId = id;
		this.url = url;
		this.numOfComments = numOfComments;
		if(btdm != null)
			this.btdm = btdm;
		else
			this.mdm = mdm;
		this.lastComment = lastComment;
	}
	
	public void run() 
	{
		try {
			if(this.btdm != null)
			{
				this.cdm.getCommentsByUrl(url, numOfComments, threadId, lastComment, btdm, null);
				BuildingTreeDataManager.commentsString[this.threadId-1] = cdm.getCommentString();
			}
			else
			{
				this.cdm.getCommentsByUrl(url, numOfComments, threadId, lastComment, null, mdm);
				MaintenanceDataManager.commentsString[this.threadId-1] = cdm.getCommentString();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}

}
