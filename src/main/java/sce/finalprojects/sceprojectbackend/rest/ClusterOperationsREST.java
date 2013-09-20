package sce.finalprojects.sceprojectbackend.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import sce.finalprojects.sceprojectbackend.datatypes.ClusterRepresentationDO;



/*
 * This REST exposes operations that can be performed on clusters
 */
@Service("clustersRest")
@Path("/clusters")
@Produces("application/json")
@Consumes("application/json")
@Controller
public class ClusterOperationsREST {
	
	/*
	 * This method is used to retrieve basic information on multiple/single clusters
	 * Input:
	 * clusters/?articleid=fooarticle&level=1&clusterid=1&clusterid=2&clusterid=3
	 * 
	 * Output:
	 * 			[
					 {
						id : 1,
						label:"Label 1",
						children : [ 1, 2, 3 ]
					 },
					 {
						id : 2,
						label:"Label 2",
						children : [ 1, 2, 3 ]
					 },
					 {
						id : 3,
						label:"Label 3",
						children : [ 1, 2, 3 ]
					 }			 
				]
				
				The output is an array of objects. Each objects represents a cluster. For each object, we returns its ID, its label
				and the ID's of its sub-clusters
	 */
	@GET
	public Response getClusters(@QueryParam("articleid") String articleID, @QueryParam("level") int level,  @QueryParam("clusterid") List<String> requestedClusters){

		Set<ClusterRepresentationDO> clusterRepresentation = new HashSet<ClusterRepresentationDO>();

			//TODO STUB - REMOVE - START
			Set<String> children = new HashSet<String>();
			children.add("1234");
			children.add("2223");
			clusterRepresentation.add(new ClusterRepresentationDO("222"+(new Random()).nextInt(), "Test", children));
			//TODO STUB - REMOVE - END
			//return Response.ok(DatabaseOperations.getClustersRepresentationByIDs(requestedClusters, level, articleID)).build();
		
		return Response.ok(clusterRepresentation).build();
	}

}
