package sce.finalprojects.sceprojectbackend.datatypes;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetClusterCommentsResponse {
	
	private String markup;

	public String getMarkup() {
		return markup;
	}

	public void setMarkup(List<String> markupList) {
		
		StringBuilder sb = new StringBuilder();
		
		for(String tmpMarkup: markupList)
			sb.append(tmpMarkup);
		
		markup = sb.toString();
	}

}
