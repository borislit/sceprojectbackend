package sce.finalprojects.sceprojectbackend.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MarkupUtility {
	
	public static String  getCommentBodyFromMarkup(String markup){
		Document doc = Jsoup.parse(markup);
		Elements matchingElements  = doc.select(".comment-content");
		
		if(matchingElements.size() == 0) return null;
		
		Element elem =  matchingElements.get(0);
		return elem.text();
	}

}
