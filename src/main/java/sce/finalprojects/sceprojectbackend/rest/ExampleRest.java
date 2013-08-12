package sce.finalprojects.sceprojectbackend.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import sce.finalprojects.sceprojectbackend.managers.LifecycleScheduleManager;


@Service("exampleRest")
@Path("/example")
public class ExampleRest {
	
	private int i = 0;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getHandler(){
		try {
		LifecycleScheduleManager.createLifecycleForArticle("123");
		
			Thread.sleep(5000);
			
		LifecycleScheduleManager.createLifecycleForArticle("345");
		Thread.sleep(5000);
		LifecycleScheduleManager.createLifecycleForArticle("678");
		
		i++;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok(i).build();
	}
	
}
