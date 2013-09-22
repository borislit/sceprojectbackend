package sce.finalprojects.sceprojectbackend.datatypes;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetClusterCommentsResponse {
	
	private String markup;

	public String getMarkup() {
		return markup;
	}

	public void setMarkup(String markup) {
		
		this.markup = markup;
	}

}
