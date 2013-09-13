package sce.finalprojects.sceprojectbackend.algorithms;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.hssf.record.DBCellRecord;
import org.junit.Before;
import org.junit.Test;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.Acell;
import sce.finalprojects.sceprojectbackend.datatypes.ArrayOfCommentsDO;
import sce.finalprojects.sceprojectbackend.datatypes.Cluster;
import sce.finalprojects.sceprojectbackend.datatypes.Comment;
import sce.finalprojects.sceprojectbackend.factories.ArrayOfCommentsFactory;





public class EfficientHACRegularTsts {

	private EfficientHAC efh;
	private ArrayList<Comment> ArrayOfComments;
	private double[] vectRep;
	
	@Before
	public void initGaacTests() throws SQLException{
		
		ArrayOfComments = DatabaseOperations.getAllComentsWithoutHTML("1");
		vectRep = new double[9];


	}
	
	
	@Test
	public void testTheAlgorithmIsRunning() throws Exception {
		
		this.efh = new EfficientHAC(ArrayOfComments, vectRep);
		this.efh.runAlgorithm();
		
		assertEquals(true,efh.calculated);
	}
	
	@Test 
	public void testRunningAgainTheAlgorithmWithoutReset(){
		
		try {
			this.efh = new EfficientHAC(ArrayOfComments, vectRep);
			this.efh.runAlgorithm();
			assertEquals(efh.calculated, true);
			this.efh.runAlgorithm();
			
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals("Please reset the fields", e.getMessage());
		}

	}
	
	@Test
	public void testThePriorityQueueIsStilSorting() throws Exception {
		
		this.efh = new EfficientHAC(ArrayOfComments, vectRep);
		this.efh.runAlgorithm();
		assertEquals(true,efh.calculated);
		
		String log = "";
		for(int i = 0 ; i < efh.clustersArray.size(); i++)
		{
			double last = Double.MAX_VALUE;
			while(!efh.p.get(i).isEmpty())
			{
				if((last < efh.p.get(i).peek().sim) )
				{
					log += "The Priority queue of Cluste : "+i+" wasn't sorted because "+last+" < ";  
					last = efh.p.get(i).poll().sim;
					log += last+"\n";		
				}
				else
					last = efh.p.get(i).poll().sim;
			}
		}
		
		System.out.println(log);
		assertEquals("", log);
	}
	
	
	@Test
	public void testTheMergesArrayIsSortedBySim() throws Exception {
		
		this.efh = new EfficientHAC(ArrayOfComments, vectRep);
		this.efh.runAlgorithm();
		assertEquals(true,efh.calculated);
		
		String log = "SUCCESS";
		double last = Double.MAX_VALUE;
		for (Acell merge : efh.a) {
			if(merge.mergeSim > last)
			{
				log = "FAIL";
				break;
			}
			last = merge.mergeSim;	
		}
		assertEquals("SUCCESS", log);
	}

	@Test
	public void testNormalization() throws Exception {
		
		Comment com1 = new Comment("1", new double[] {0.37,0.25,0.47});
		Comment.nomalizeCommentVector(com1);
		Comment com2 = new Comment("2", new double[] {0.25,0.17,0.12});
		Comment.nomalizeCommentVector(com2);
		Cluster cl1 = new Cluster(com1);
		Cluster cl2 = new Cluster(com2);
		System.out.println(cl1.GAAC(cl1, cl2, new double[com1.vector.size()]));
		
	}
	
	
	@Test
	public void testDBsomeTest() throws Exception {
		
		int numOfCom = 160;
		
		DatabaseOperations.addNewArticle("123", "http://news.yahoo.com/_xhr/contentcomments/get_comments/?content_id=5dfccac3-8873-3941-845c-c9e1de3d20cc&_device=full&count=10&sortBy=highestRated&isNext=true&offset=10&pageNumber=1&_media.modules.content_comments.switches._enable_view_others=1&_media.modules.content_comments.switches._enable_mutecommenter=1&enable_collapsed_comment=1", numOfCom );
		
		ArrayOfCommentsFactory commentsFactory = new ArrayOfCommentsFactory();
		ArrayOfCommentsDO arrayOfComments = commentsFactory.get("123");
		commentsFactory.save(arrayOfComments);
		
		EfficientHAC efh = new EfficientHAC(arrayOfComments.arrayOfComment, arrayOfComments.vect);
		efh.runAlgorithm();
		xmlGenerator xxx = new xmlGenerator("123", efh.a, numOfCom);
		Maintenance maint = new Maintenance();
		maint.mapXmlHacToClusters("123");
		
		System.out.println(DatabaseOperations.getXMLRepresentation("123"));
		DatabaseOperations.getArticleMapping("123");
		
		
	}

}
