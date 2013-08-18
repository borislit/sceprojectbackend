package sce.finalprojects.sceprojectbackend.datatypes;
/**
 * Cell for the merges array A array of the EfficientHac algorithm
 * @author Yuval Simhon
 *
 */
public class Acell {

	public String a1;
	
	public String a2;
	
	public double mergeSim;



public Acell(String k1 , String k2, double sim){
	
	this.a1 = k1;
	this.a2 = k2;
	this.mergeSim = sim ;
}


}