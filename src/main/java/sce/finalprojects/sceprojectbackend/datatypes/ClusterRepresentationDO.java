package sce.finalprojects.sceprojectbackend.datatypes;

import java.util.Set;

public class ClusterRepresentationDO {
	String id;
	String label;
	Set<String> childrenIDs;
	
	public ClusterRepresentationDO(String id, String label,
			Set<String> childrenIDs) {
		super();
		this.id = id;
		this.label = label;
		this.childrenIDs = childrenIDs;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public Set<String> getChildrenIDs() {
		return childrenIDs;
	}
}
