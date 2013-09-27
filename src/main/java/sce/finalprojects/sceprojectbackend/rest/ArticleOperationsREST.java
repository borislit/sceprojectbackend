package sce.finalprojects.sceprojectbackend.rest;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.ArticleSetupRequestDO;
import sce.finalprojects.sceprojectbackend.datatypes.ClusterRepresentationDO;
import sce.finalprojects.sceprojectbackend.managers.LifecycleScheduleManager;


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
	@SuppressWarnings("unchecked")
	@GET
	@Path("/setup")
	public Response setupArticle(@QueryParam("building_url") String buildingUrl, @QueryParam("maintenance_url") String maintenanceUrl, @QueryParam("comments_amount_url") String commentAmountURL, @QueryParam("count") int count, @QueryParam("articleid")String articleID){
		ArticleSetupRequestDO setupRequest = new ArticleSetupRequestDO(buildingUrl, maintenanceUrl, commentAmountURL, count, articleID );
		Set<ClusterRepresentationDO> response = null;
		
		if(DatabaseOperations.checkArticleExitanceByID(setupRequest.getArticleID())){
			
			response = DatabaseOperations.getHACRootID(setupRequest.getArticleID());
			
		}else{
			
			ScheduledFuture<?> task = LifecycleScheduleManager.createLifecycleForArticle(setupRequest);
			
			try {
				
				response = (Set<ClusterRepresentationDO>) task.get();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return Response.ok(response).build();
	}
	//http://www.sce.ac.il/article/setup
	
}
