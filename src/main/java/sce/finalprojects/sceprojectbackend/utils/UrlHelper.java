package sce.finalprojects.sceprojectbackend.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlHelper {

	/** this function build a format of URL
	 * @param url, getting from boris
	 * @return a format of url that contain 3 parameter that need to be replaced
	 */
	public synchronized URL buildUrl(URL url){
		String pathUrl = url.getPath();
		String qry  = url.getQuery();		
		String[] placeholders = {"{{COUNT_ARG}}", "{{OFFSET_ARG}}", "{{PAGE_NUM_ARG}}"};
	    
		qry = qry.replaceAll("count=[0-9]+&", placeholders[0]);
		qry = qry.replaceAll("offset=[0-9]+&", placeholders[1]);
		qry = qry.replaceAll("pageNumber=[0-9]+", placeholders[2]);

	    try {
			URL fix = new URL("http://news.yahoo.com" + pathUrl + "?" + qry);
			return fix;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	/**
	 * this function set a URL with count of comment we asking from yahoo = 100 by the offset that tell us from witch comment 
	 * we start to ask the comments
	 * @param url (the format url return from the buildUrl func)
	 * @param offset, the number that the comments we ask start from
	 * @return a fix URL that ready to send to yahoo
	 */
	public synchronized URL getFixUrl(URL url, int offset){
		String pathUrl = url.getPath();
		String qry = url.getQuery();
		qry = qry.replace("{{COUNT_ARG}}", "count=100&");
		qry = qry.replace("{{OFFSET_ARG}}", "offset=" + offset + "&");
		qry = qry.replace("{{PAGE_NUM_ARG}}", "pageNumber=1" );

		try {
			URL fixUrl = new URL("http://news.yahoo.com" + pathUrl + "?" + qry);
			return fixUrl;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}
}
