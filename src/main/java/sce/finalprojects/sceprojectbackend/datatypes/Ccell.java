package sce.finalprojects.sceprojectbackend.datatypes;
/**
 * Ccell is a cell in the sim matrix c[][] in the EfficientHAC algorithm
 * @author Yuval Simhon
 *
 */
public class Ccell {

	public int index;
	
	public double sim;
	

	public Ccell(int _index, double _sim){
		this.index = _index;
		this.sim = _sim;
	
		
	}
	
}
