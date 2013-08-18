package sce.finalprojects.sceprojectbackend.algorithms;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;





import sce.finalprojects.sceprojectbackend.database.DbHandler;
import sce.finalprojects.sceprojectbackend.datatypes.Comment;
import commentsTreatment.Driver;


public class maintenanceTest {

	private Maintenance maint;
	
	@Before
	public void initTest() {
		
		this.maint = new Maintenance();
	}
	
	@Test
	public void testXmlMapping() throws Exception {
		
		maint.mapXmlHacToClusters("1");
		
		System.out.println(DbHandler.getArticleMapping("1"));
		
	}
	
	@Test
	public void testAddNewElement() throws Exception {
		
		double[] ne = {1,0,1,0,0,1,0,0};
		double[] vec = {0,0,0,0,0,0,0,0};
		maint.addNewElementToHAC(new Comment("11",ne), "1",vec);
		
	}
	
	@Test
	public void testAddNewElementIntoAnArticle() throws Exception {
		
		Driver.numOfComments = 10 ;
		
		
		ArrayList<ArrayList<Double>> vectArray = Driver.returnCommentsArray();
		
		ArrayList<Comment> ArrayOfComments = new ArrayList<Comment>();
		
		int sizeOfVector = vectArray.get(0).size();
		double[] vectRep = new double[sizeOfVector];
		
//		for (int i = 0   ; i < sizeOfVector ; i ++) 
//			vectRep.add(true);
		
		for(int i = 0 ; i < Driver.numOfComments ; i++ )
			ArrayOfComments.add(new Comment("Comment "+(i+1), vectArray.get(i)));
		
		EfficientHAC efh = new EfficientHAC(ArrayOfComments, vectRep);
		efh.runAlgorithm();
		//XML
		xmlGenerator x = new xmlGenerator("1",efh.a, Driver.numOfComments);
		
		Driver.numOfComments = 1;
		
		vectArray = Driver.returnCommentsArray();
		ArrayOfComments.set(0, new Comment("comment 11", vectArray.get(0)));
		vectRep = new double[sizeOfVector+2];
		
		
		maint.addNewElementToHAC(ArrayOfComments.get(0), Driver.articleId, vectRep);
		
	}
	

}
