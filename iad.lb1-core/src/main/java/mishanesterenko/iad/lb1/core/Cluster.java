package mishanesterenko.iad.lb1.core;

import java.util.ArrayList;
import java.util.List;

import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;


public class Cluster {
	private List<Vector> clusteredVectors;
	private Vector centroid;

	public Cluster(Vector centroid) {
		this(new ArrayList<Vector>(), centroid);
	}
	
	public Cluster(List<Vector> initialVectors, Vector centroid) {
		setClusteredVectors(new ArrayList<Vector>(initialVectors));
		
	}

	public List<Vector> getClusteredVectors() {
		return clusteredVectors;
	}

	private void setClusteredVectors(List<Vector> clusteredVectors) {
		this.clusteredVectors = clusteredVectors;
	}

	public Vector getCentroid() {
		return centroid;
	}

	public void setCentroid(Vector centroid) {
		this.centroid = centroid;
	}
}
