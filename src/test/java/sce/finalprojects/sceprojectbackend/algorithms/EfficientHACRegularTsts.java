package sce.finalprojects.sceprojectbackend.algorithms;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.Acell;
import sce.finalprojects.sceprojectbackend.datatypes.Comment;
import sce.finalprojects.sceprojectbackend.utils.CommentsDownloadTest;
import commentsTreatment.Driver;



public class EfficientHACRegularTsts {

	private EfficientHAC efh;
	private ArrayList<Comment> ArrayOfComments;
	private double[] vectRep;
	
	@Before
	public void initGaacTests() throws SQLException{
		
		ArrayOfComments = DatabaseOperations.getAllComentsWithoutHTML("1");
	
//		ArrayOfComments = new Comment[10];
//		ArrayOfComments[0] =new Comment("comment_number 0", new double[] {1,0,0,0,1,1,0,1});
//		ArrayOfComments[1] =new Comment("comment_number 1", new double[] {1,0,1,1,0,1,0,1});
//		ArrayOfComments[2] =new Comment("comment_number 2", new double[] {0,0,1,0,1,1,0,1});
//		ArrayOfComments[3] =new Comment("comment_number 3", new double[] {1,0,0,1,0,1,0,0});
//		ArrayOfComments[4] =new Comment("comment_number 4", new double[] {1,1,0,1,0,1,1,1});
//		ArrayOfComments[5] =new Comment("comment_number 5", new double[] {1,0,1,1,0,1,0,1});
//		ArrayOfComments[6] =new Comment("comment_number 6", new double[] {0,0,1,1,1,1,0,1});
//		ArrayOfComments[7] =new Comment("comment_number 7", new double[] {1,1,1,1,0,1,1,1});
//		ArrayOfComments[8] =new Comment("comment_number 8", new double[] {0,0,1,0,0,1,0,0});
//		ArrayOfComments[9] =new Comment("comment_number 9", new double[] {1,1,1,0,1,1,0,0});
		
		
		vectRep = new double[9];
//		for (int i = 0   ; i <  9 ; i ++) {
//			
//			vectRep.add(true);
			
//		}

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
	public void testWithCommentsFromArtice() throws Exception {
		
		CommentsDownloadTest.numOfComments = 8 ;
		
		
		ArrayList<ArrayList<Double>> vectArray = CommentsDownloadTest.returnCommentsArray();
		
		this.ArrayOfComments = new ArrayList<Comment>();
		
		int sizeOfVector = vectArray.get(0).size();
		this.vectRep = new double[sizeOfVector];
//		for (int i = 0   ; i < sizeOfVector ; i ++) 
//			vectRep.add(true);
//		
		for(int i = 0 ; i < CommentsDownloadTest.numOfComments ; i++ )
			ArrayOfComments.add(new Comment("Comment "+(i+1), vectArray.get(i)));
		
		this.efh = new EfficientHAC(ArrayOfComments, vectRep);
		this.efh.runAlgorithm();
		
		//XML
		
		xmlGenerator x = new xmlGenerator("1",efh.a, CommentsDownloadTest.numOfComments);
		
	}

}
