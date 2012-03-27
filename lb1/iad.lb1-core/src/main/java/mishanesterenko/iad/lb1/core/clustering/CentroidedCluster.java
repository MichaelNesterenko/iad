package mishanesterenko.iad.lb1.core.clustering;

import java.util.List;

import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector;

public class CentroidedCluster extends Cluster {
	private Vector centroid;

	public CentroidedCluster() {
	}

	public CentroidedCluster(Vector centroid) {
		setCentroid(centroid);
	}

	public CentroidedCluster(Vector centroid, List<Vector> clusteredVectors) {
		super(clusteredVectors);
		setCentroid(centroid);
	}

	public Vector getCentroid() {
		return centroid;
	}

	public void setCentroid(Vector centroid) {
		this.centroid = centroid;
	}

	public CentroidedCluster clone(boolean cloneVectors) {
		CentroidedCluster cloned = (CentroidedCluster) super.clone(cloneVectors);
		cloned.setCentroid(getCentroid().clone());
		return cloned;
	}

	public CentroidedCluster clone() {
		return clone(true);
	}
}
