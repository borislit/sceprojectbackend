package sce.finalprojects.sceprojectbackend.algorithms;

import java.util.ArrayList;
import java.util.PriorityQueue;

import sce.finalprojects.sceprojectbackend.datatypes.Acell;
import sce.finalprojects.sceprojectbackend.datatypes.Ccell;
import sce.finalprojects.sceprojectbackend.datatypes.Cluster;
import sce.finalprojects.sceprojectbackend.datatypes.Comment;
import sce.finalprojects.sceprojectbackend.utils.simComperator;


public class EfficientHAC {
	
	public static int times = 0;
	
	public boolean initiated;
	
	public boolean calculated;
	
	public Ccell[][] c;

	//public Comment[] commentsArray;

	public ArrayList<Acell> a;    ///merges array

	public boolean[] i;  ///i array of availability

	public ArrayList<PriorityQueue<Ccell>> p;   ///a queue contains a sorted (by sim) line from C matrix

	public ArrayList<Cluster> clustersArray;
	
	private int numberOfElements; ///the number of clusters/comments etc

	private double[] vect; ///how a vector should looks like
	
	public static void main(String[] args) {

	}

	public EfficientHAC(ArrayList<Comment> commentsArr , double[] vectRep) throws Exception{
		reset(commentsArr, vectRep);
	}
	
	/**
	 * reseting all the fields and prepare the fields to run algorithm
	 * @param commentsArr
	 * @param vectRep
	 * @throws Exception
	 */
	public void reset(ArrayList<Comment> commentsArr , double[] vectRep) throws Exception{
		
		initiated = false;
		calculated = false;
		
		for (Comment com : commentsArr) {
			Comment.nomalizeCommentVector(com);
		}
		vect = vectRep;
		clustersArray = Cluster.makeClustersArray(commentsArr);
		
		initiateEfficientHACForUse();
	}
	

	/**
	 * Initiate all the fields and arrays to use efficient HAC
	 * @throws Exception 
	 *  
	 */
	private void initiateEfficientHACForUse() throws Exception {
		
		numberOfElements = clustersArray.size();
		
		this.p = new ArrayList<PriorityQueue<Ccell>>();
		
		this.c = new Ccell[numberOfElements][numberOfElements];
		
		this.i = new boolean[numberOfElements];
				
		for(int n = 0 ; n < numberOfElements ; n++)
		{
			for(int i = 0 ; i < numberOfElements ; i++)
			{
				if(i == n)
				{
					c[n][i] = new Ccell(i,1.0);
				}
				
				if(c[n][i] == null) {
					c[n][i] = new Ccell(i,clustersArray.get(n).GAAC(clustersArray.get(n), clustersArray.get(i),vect));
					c[i][n] = new Ccell(n, c[n][i].sim);
				}
			}
		
			this.i[n] = true;	
			initiatePriorityQueue(n);
		}
		a = new ArrayList<Acell>();
		initiated = true;
	}
	
	/**
	 * run the EFFICIENT HAC algorithm
	 * @throws Exception
	 */
	public void runAlgorithm () throws Exception{
		
		if(!initiated)
		{
			throw new Exception("The algorithm is not initiated, please initiate the algorithem or make another instance");
		}
		if(calculated)
		{
			throw new Exception("Please reset the fields");
		}
		
		int k1;
		int k2;
		
		for(int f = 0 ; f < (numberOfElements-1) ; f++ )
		{
			k1 = getArgMax();
			
			System.out.print(f+" "+k1+" ");
			
			k2 = p.get(k1).peek().index;
			
			System.out.println(k2);
			
			a.add(new Acell(clustersArray.get(k1).cluster_id,clustersArray.get(k2).cluster_id,p.get(k1).peek().sim));
			
			clustersArray.get(k1).mergeWithCluster(clustersArray.get(k2));
			
			EfficientHAC.times++;
			
			i[k2] = false;
			
			p.get(k1).clear();
			
			reCalculatePandC(k1,k2);
			
		}
			
		calculated = true;
	}
	
	/**
	 * recalculating the priority queue and the sim matrix c
	 * @throws Exception 
	 */
	private void reCalculatePandC(int k1, int k2) throws Exception {
		
		for(int i = 0 ; i < numberOfElements ; i++)
			if(i != k1 && this.i[i])
			{
				p.get(i).remove(c[i][k1]);
				p.get(i).remove(c[i][k2]);
				c[i][k1].sim = clustersArray.get(i).GAAC(clustersArray.get(i),clustersArray.get(k1),vect);
				c[k1][i].sim = c[i][k1].sim;
				p.get(i).add(c[i][k1]);
				p.get(k1).add(c[k1][i]);
			}
	}

	/**
	 * ArgMax = argMax{k:i[k] = true}p[k].max().sim
	 * @return the position in clusters array of the cluster with the maximum similarity for one of the clusters<br>
	 * it's depends of the availability of the cluster. 
	 */
	private int getArgMax(){
		
		double tempSim;
		double maxSim = 0;
		int position = -1;
		for(int k = 0 ; k < numberOfElements; k++)
		{
			if(i[k])
			{
				tempSim = p.get(k).peek().sim;
			    if (tempSim > maxSim)
			    {
			    	maxSim = tempSim;
					position = k;
			    }
				
			}
		}
		return position;
	}




	/**
	 * Initiate the priority queue P for each comment
	 */
	private void initiatePriorityQueue(int index){
		
		p.add(index,new PriorityQueue<Ccell>(numberOfElements, simComperator.getInstance()));
		for(int i = 0 ; i < numberOfElements ; i++)
		{
			if(i != index) /// avoiding self similarities
				p.get(index).add(c[index][i]);
		}

	}
}
