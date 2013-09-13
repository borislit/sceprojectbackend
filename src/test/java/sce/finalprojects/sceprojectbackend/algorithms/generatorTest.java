package sce.finalprojects.sceprojectbackend.algorithms;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.Acell;
import sce.finalprojects.sceprojectbackend.datatypes.Comment;


public class generatorTest {
	
	private xmlGenerator gen;
	//efficient HAc 
	private EfficientHAC efh;
	private ArrayList<Comment> ArrayOfComments;
	private double[] vectRep;
	private int numOfComments = 8;
	private int sizeOfComment = 15;
	
	@Before
	public void init() throws Exception {
		
		ArrayOfComments = new ArrayList<Comment>();
		ArrayOfComments = DatabaseOperations.getAllComentsWithoutHTML("1");

		vectRep = new double[sizeOfComment];
//		for (int i = 0   ; i <  sizeOfComment ; i ++) {	
//			vectRep.add(true);	
//		}
		efh = new EfficientHAC(ArrayOfComments, vectRep);
		efh.runAlgorithm();
		
		//gen = new Generator(efh.a);
		
	}
	
	
	@Test(timeout = 50000)
	public void testXLMBuilderWithoutEfficientHACAlgo() {
		
		ArrayList<Acell> a = new ArrayList<Acell>();
		a.add(new Acell("1", "2", 0.2));
		a.add(new Acell("4", "5", 0.3));
		a.add(new Acell("6", "7", 0.4));
		a.add(new Acell("1", "3", 0.5));
		a.add(new Acell("9", "8", 0.6));
		a.add(new Acell("1", "4", 0.7));
		a.add(new Acell("6", "9", 0.8));
		a.add(new Acell("6", "10", 0.9));
		a.add(new Acell("1", "6", 1));
		
		gen = new xmlGenerator("1",a,9);
		
	}
	
	@Test
	public void testXLMBuilderWithEfficientHACAlgo() {
		
		//TODO: check that test results (comment 0 - in th EOF) , something wrong with the self insertion of cluster, also with the merge sim
		
		gen = new xmlGenerator("1",efh.a,ArrayOfComments.size());
		
	}
	

}
