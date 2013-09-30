/*package sce.finalprojects.sceprojectbackend.algorithms;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.Comment;


@RunWith(value = Parameterized.class)
public class Performance{
	
	private EfficientHAC efh;
	private ArrayList<Comment> ArrayOfComments;
	private double[] vectRep;
	private int numOfComments;
	
	
	 public Performance(int number) {
		    this.numOfComments = number;
		 }
	 
	 @Parameters
	 public static Collection<Object[]> data() {
	   Object[][] data = new Object[][] { { 10 }, { 100 }, {600}, { 800 } };
	   return Arrays.asList(data);
	 }
	
	@Before
	public void initTests(){
		
		ArrayOfComments = new ArrayList<Comment>();
		for(int i=0; i < (numOfComments -1 ) ; i+=2)
		{
			ArrayOfComments.add(new Comment("comment_number "+i, new double[] {1,0,0,0,1,1,0}));
			ArrayOfComments.add(new Comment("comment_number "+i, new double[] {1,0,0,0,1,1,0}));
		}

		vectRep = new double[numOfComments];

	}
	
	
	@Test(timeout = 150000)
	public void testPreformance() throws Exception {
		EfficientHAC.times = 0;
		this.efh = new EfficientHAC(ArrayOfComments, vectRep);
		this.efh.runAlgorithm();
		assertEquals(true,efh.calculated);
		
	}
	
}
*/