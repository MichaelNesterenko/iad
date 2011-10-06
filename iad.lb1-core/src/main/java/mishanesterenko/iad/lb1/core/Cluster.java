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
		setClusteredVectors(initialVectors);
	}

	public List<Vector> getClusteredVectors() {
		return clusteredVectors;
	}

	public void setClusteredVectors(List<Vector> clusteredVectors) {
		this.clusteredVectors = clusteredVectors;
	}

	public Vector getCentroid() {
		return centroid;
	}

	public void setCentroid(Vector centroid) {
		this.centroid = centroid;
	}

	public Cluster clone(boolean cloneVectors) {
		Cluster newCluster;
		if (cloneVectors) {
			List<Vector> newClusteredVectors = new ArrayList<Vector>(getClusteredVectors().size());
			for (Vector vec : getClusteredVectors()) {
				newClusteredVectors.add(vec.clone());
			}
			newCluster = new Cluster(newClusteredVectors, getCentroid() != null ? getCentroid().clone() : null);
		} else {
			newCluster = new Cluster(getCentroid() != null ? getCentroid().clone() : null);
		}

		return newCluster;
	}

	public Cluster clone() {
		return clone(true);
	}
}
