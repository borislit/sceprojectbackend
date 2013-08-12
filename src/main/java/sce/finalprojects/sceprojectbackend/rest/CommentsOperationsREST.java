package sce.finalprojects.sceprojectbackend.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

@Service("commentsRest")
@Path("/comments")
@Produces("application/json")
@Consumes("application/json")
public class CommentsOperationsREST {
	/*
	 * Input:
	 * comments/more?clusterid=foocluster&level=3&from=1&to=10
	 */
	@GET
	@Path("/more")
	public Response setupArticle(@QueryParam("articleid") String articleid, @QueryParam("clusterid") String clusterID, @QueryParam("level") int level, @QueryParam("from") int from, @QueryParam("to") int to){
		return Response.ok(clusterID).build();
	}

}
