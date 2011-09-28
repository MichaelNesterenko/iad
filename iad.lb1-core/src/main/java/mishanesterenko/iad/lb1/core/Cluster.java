package mishanesterenko.iad.lb1.core;

import java.util.ArrayList;
import java.util.List;

import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;


public class Cluster {
	private List<Vector> clusteredVectors;

	public Cluster(List<Vector> initialVectors) {
		setClusteredVectors(new ArrayList<Vector>(initialVectors));
	}

	public List<Vector> getClusteredVectors() {
		return clusteredVectors;
	}

	private void setClusteredVectors(List<Vector> clusteredVectors) {
		this.clusteredVectors = clusteredVectors;
	}
}
