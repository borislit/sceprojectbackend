package sce.finalprojects.sceprojectbackend.datatypes;



public class XmlElement {

	public static final String ELEMENT_CLUSTER = "cl";
	public static final String ELEMENT_ID = "id";
	public static final String ELEMENT_LEVEL = "lv";
	public static final String ELEMENT_MERG_SIM = "ms";
	
	public String id;
	public int level;
	public double mergeSim;
	

	public XmlElement(String _id, int _level, double _sim) {
		
		this.id = _id;
		this.level = _level;
		this.mergeSim = _sim;
	}
}
