package sce.finalprojects.sceprojectbackend.datatypes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetClustersResponse {
	Set<ClusterRepresentationDO> clusterRepresentation = new HashSet<ClusterRepresentationDO>();

	public Set<ClusterRepresentationDO> getClusterRepresentation() {
		return clusterRepresentation;
	}

	public void setClusterRepresentation(
			Set<ClusterRepresentationDO> clusterRepresentation) {
		this.clusterRepresentation = clusterRepresentation;
	}
}
