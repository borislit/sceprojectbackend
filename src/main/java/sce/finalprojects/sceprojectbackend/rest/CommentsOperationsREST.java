package sce.finalprojects.sceprojectbackend.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.GetClusterCommentsResponse;

/*
 * This REST exposes operations that can be performed on comments of the given cluster
 */
@Service("commentsRest")
@Path("/comments")
@Produces("application/json")
@Consumes("application/json")
public class CommentsOperationsREST {
	/*
	 * This method is used to retrieve batch of comment for specifc cluster. The From and To input are used
	 * to define boundaries for comments to be retrieved
	 * Input:
	 * comments/more?clusterid=foocluster&level=3&from=1&to=10
	 * 
	 * Output:
	 * 	 {"markup":"<div>comment</div>"}
	 */
	@GET
	@Path("/more")
	public Response getClusterComments(@QueryParam("articleid") String articleId, @QueryParam("clusterid") String clusterID, @QueryParam("level") int level, @QueryParam("from") int from, @QueryParam("to") int to){
		GetClusterCommentsResponse response = new GetClusterCommentsResponse();
		response.setMarkup(DatabaseOperations.getCommentsForGivenCluster(articleId, clusterID, level, from, to));
		return Response.ok(clusterID).build();
	}

}
