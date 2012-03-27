package mishanesterenko.iad.lb1.core.clustering;

import mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector;

public class IndexCentroidedCluster extends CentroidedCluster {
	private int centroidIndex;

	public IndexCentroidedCluster(Vector centroid, int index) {
		super(centroid);
		setCentroidIndex(index);
	}

	public int getCentroidIndex() {
		return centroidIndex;
	}

	public void setCentroidIndex(int centroidIndex) {
		this.centroidIndex = centroidIndex;
	}

	public IndexCentroidedCluster clone(boolean cloneVectors) {
		IndexCentroidedCluster cloned = (IndexCentroidedCluster) super.clone(cloneVectors);
		cloned.setCentroid(getCentroid().clone());
		return cloned;
	}
}
