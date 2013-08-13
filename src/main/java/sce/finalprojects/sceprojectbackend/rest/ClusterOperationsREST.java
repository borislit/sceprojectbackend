package sce.finalprojects.sceprojectbackend.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;


@Service("clustersRest")
@Path("/clusters")
@Produces("application/json")
@Consumes("application/json")
public class ClusterOperationsREST {
	/*
	 * Input:
	 * clusters/?articleid=fooarticle&level=1&clusterid=1&clusterid=2&clusterid=3
	 * 
	 * Output:
	 * 			[
					 {
						id : 1,
						label:"Level "+i+" Child 1",
						children : [ 1, 2, 3 ]
					 },
					 {
						id : 2,
						label:"Level "+i+" Child 2",
						children : [ 1, 2, 3 ]
					 },
					 {
						id : 3,
						label:"Level "+i+" Child 3",
						children : [ 1, 2, 3 ]
					 }			 
				]
	 */
	@GET
	public Response getClusters(@QueryParam("articleid") String articleid, @QueryParam("level") int level,  @QueryParam("clusterid") String[] requestedClusters){
		return Response.ok(requestedClusters).build();
	}

}
