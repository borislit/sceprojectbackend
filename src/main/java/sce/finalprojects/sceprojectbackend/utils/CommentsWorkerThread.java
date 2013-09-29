package sce.finalprojects.sceprojectbackend.utils;

import java.io.FileNotFoundException;
import java.net.URL;
import sce.finalprojects.sceprojectbackend.managers.*;

public class CommentsWorkerThread extends Thread{
	
	int threadId;
	URL url;
	int numOfComments;
	CommentsDownloadManager cdm = new CommentsDownloadManager();
	
	/**
	 * constructor
	 * @param id of the thread
	 * @param url
	 * @param numOfComments we want to get from yahoo in the specific thread
	 */
	public CommentsWorkerThread(int id, URL url, int numOfComments){
		this.threadId = id;
		this.url = url;
		this.numOfComments = numOfComments;
		
	}
	
	public void run() {
		try {
				this.cdm.getCommentsByUrlForBuilding(url, threadId, numOfComments);
				BuildingTreeDataManager.commentsString[this.threadId-1] = cdm.getCommentString();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
}
