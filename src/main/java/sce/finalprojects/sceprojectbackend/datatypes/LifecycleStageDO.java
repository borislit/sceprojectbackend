package sce.finalprojects.sceprojectbackend.datatypes;

public class LifecycleStageDO {
	private double from;
	private double to;
	private double interval;
	
	public LifecycleStageDO(double from, double to, double interval) {
		super();
		this.from = from;
		this.to = to;
		this.interval = interval;
	}

	public double getFrom() {
		return from;
	}

	public double getTo() {
		return to;
	}

	public double getInterval() {
		return interval;
	}
	
}
