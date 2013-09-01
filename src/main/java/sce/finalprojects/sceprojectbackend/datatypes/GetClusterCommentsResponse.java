package sce.finalprojects.sceprojectbackend.datatypes;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetClusterCommentsResponse {
	
	private List<String> markup;

	public List<String> getMarkup() {
		return markup;
	}

	public void setMarkup(List<String> markup) {
		this.markup = markup;
	}

}
