package sce.finalprojects.sceprojectbackend.datatypes;

public class XmlElement {

	public String id;
	public int level;
	public double mergeSim;
	

	public XmlElement(String _id, int _level, double _sim) {
		
		this.id = _id;
		this.level = _level;
		this.mergeSim = _sim;
	}
}
