package sce.finalprojects.sceprojectbackend.datatypes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
/**
 * Cluster with the required maintenance methods
 * @author Yuval Simhon
 *
 */
public class Cluster {
	
	public String cluster_id;
	
	public Set<Comment> innerComments;  ///including the representative (cluster id)
	
	public boolean availability;




/**
 * Constructor that make an new cluster with the data of the comment 
 * @param com
 */
public Cluster(Comment com){
	
	this.cluster_id = com.comment_id;
	this.availability = true;
	this.innerComments = new HashSet<Comment>();
	this.innerComments.add(com);
	
}

public Cluster(Cluster cl) {
	if(cl == null) {
		this.cluster_id = null;
		return;
	}
	this.cluster_id = cl.cluster_id;
	this.availability = cl.availability;
	this.innerComments = new HashSet<Comment>(cl.innerComments);
}

/**
 * generate a clusters array from comments array
 * @param arrayOfComments
 * @return array list of clusters , each cluster contains the comment in the innerComments array
 */
public static ArrayList<Cluster> makeClustersArray(ArrayList<Comment> arrayOfComments){
	
	ArrayList<Cluster> returnArray = new ArrayList<Cluster>();
	
	for(int i=0 ; i < arrayOfComments.size() ; i++)
	{
		returnArray.add(new Cluster(arrayOfComments.get(i)));
	}
	return returnArray;
}

/**
 * Merge between 2 clusters , merge c2 into this cluster instance
 * @param c2 - the merged into c1 cluster
 */
public void mergeWithCluster( Cluster c2) {
	
	c2.availability = false;
	
	for (Comment com : c2.innerComments) {
		this.innerComments.add(com);
	}
}


/**
 * This method implements GAAC to calculate the similarity between two given clusters
 * @param c1 - refer to Wi 
 * @param c2 - refer to Wj
 * @param maxVectorSize - size of the vector
 * @return The GAAC similarity between cluster i and cluster k1
 * @throws Exception : <br>
 * <li>vector representation should be set before invoking gaac
 * <li>c1 or c2 didn't initiate
 * <li>C1 OR C2 didn't contain any comments - empty clusters
 */
public double GAAC(Cluster c1, Cluster c2, double[] vector) throws Exception{
	
	double result = 0;

	result = (1f / ( (c1.innerComments.size()+c2.innerComments.size())*(c1.innerComments.size()+c2.innerComments.size() -1f) ) );
	
	vector = initialVector(vector);
	
	if(vector == null)
		throw new Exception("Vector representation didnt initiate");

	///sum all the vectors into one vector 
	for (Comment com : c1.innerComments) {
		
		for(int i = 0 ; i < com.vector.size() ; i++)

			vector[i] += com.vector.get(i);
	}

	for (Comment com : c2.innerComments) {
		
		for(int i = 0 ; i < com.vector.size() ; i++)

			vector[i] += com.vector.get(i);

	}
	
	
	double squarOfVector = squareOfVector(vector);
	
	result *= (squarOfVector - (c1.innerComments.size() + c2.innerComments.size() ) );

	return result;
}

/**
 * create a vector and initiate the values to zero
 */
private double[] initialVector(double[] vector) {

	for(int i=0;i<vector.length;i++)
		vector[i]=0;

	return vector;

}

/**
 * Calculate the square of a vector size |a|^2 its like scalar product of a vector by itself
 * @return the result
 */
public double squareOfVector(double[] vect){

	double sum = 0;
	for (double d : vect) {
		sum += d*d;
	}
	return sum;
}

/**
 * find a cluster with the given id in the array
 * @param clusterArray
 * @param id
 * @return null if the cluster doesnt exist
 * <br> Otherwise return the cluster
 */
public static Cluster findClusterByIdFromArray(ArrayList<Cluster> clusterArray , String id) {
	
	for (Cluster cluster : clusterArray) {
		if(cluster.cluster_id.equals(id) && cluster.availability)
			return cluster;
	}
	return null;
	
}

public static ArrayList<Cluster> removeUnavailableClustersFromArray(ArrayList<Cluster> clustersArray) {

	ArrayList<Cluster> retArray = new ArrayList<Cluster>();
	for (Cluster cluster : clustersArray) 
		if(cluster.availability) 
			retArray.add(cluster);
	return retArray;
		
}



}