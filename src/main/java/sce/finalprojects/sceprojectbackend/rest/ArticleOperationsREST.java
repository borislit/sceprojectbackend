package sce.finalprojects.sceprojectbackend.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.ArticleSetupRequestDO;


/*
 * This REST exposes operations that can be performed on given article.
 * Will mostly kick off the initial setup of the article within the system
 */
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
	
	/*
	 * Kicks off article lifecycle within the sytem. Receives an object, with all information required for the setup
	 * This is the article's entry point into the system
	 */
	@POST
	@Path("/setup")
	public Response setupArticle(ArticleSetupRequestDO request){
		
		if(DatabaseOperations.checkArticleExitanceByID(request.getArticleID())){
			DatabaseOperations.getClustersRepresentationByIDs(clusterIDs, 0, request.getArticleID())
		}else{
			
		}
		
		return Response.ok(request).build();
	}
	//http://www.sce.ac.il/article/setup
	
}
