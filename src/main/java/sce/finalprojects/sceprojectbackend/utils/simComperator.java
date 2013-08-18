package sce.finalprojects.sceprojectbackend.utils;
import sce.finalprojects.sceprojectbackend.datatypes.Ccell;

import java.util.Comparator;

/**
 * Comparator for similarity between two clusters
 * @author Yuval Simhon
 *
 */
public class simComperator implements Comparator<Ccell> {

	private static simComperator sim;
	
	private simComperator() {
	}
	
	public static simComperator getInstance() {
		
		if(sim == null)
			sim = new simComperator();
		return sim;
	}
	
	/**
	 * return values:
	 * <li> -99 - one of the compare objects is null
	 * <li> 1  -object number one is bigger than object 2
	 * <li> 2 - object number two is bigger than object 1 
	 */
	@Override
	public int compare(Ccell o1, Ccell o2) {
		
		if(o1 == null || o2 == null)
			return -99;
		if(o1.sim < o2.sim)
		{
			return 1;
		}
		if(o1.sim > o2.sim)
		{
			return -1;
		}
		return 0;
	}

}
