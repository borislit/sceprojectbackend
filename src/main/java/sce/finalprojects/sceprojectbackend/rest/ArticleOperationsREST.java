package sce.finalprojects.sceprojectbackend.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import sce.finalprojects.sceprojectbackend.datatypes.ArticleSetupRequestDO;

@Service("articleSetupRest")
@Path("/article")
@Produces("application/json")
@Consumes("application/json")
public class ArticleOperationsREST {

	/*
	 * Input:
	 * {
		    "articleID": "ssdsd",
		    "url": "foourl",
		    "commentsCount": 111
		}
	 */
	
	@POST
	@Path("/setup")
	public Response setupArticle(ArticleSetupRequestDO request){
		return Response.ok(request).build();
	}
	
}
