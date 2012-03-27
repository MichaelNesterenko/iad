package mishanesterenko.iad.lb1.core;

import java.util.ArrayList;
import java.util.List;

import mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector;


public class Cluster implements Cloneable {
	private List<Vector> clusteredVectors;

	public Cluster() {
		this(new ArrayList<Vector>());
	}
	
	public Cluster(List<Vector> initialVectors) {
		setClusteredVectors(initialVectors);
	}

	public List<Vector> getClusteredVectors() {
		return clusteredVectors;
	}

	public void setClusteredVectors(List<Vector> clusteredVectors) {
		this.clusteredVectors = clusteredVectors;
	}

	public Cluster clone(boolean cloneVectors) {
		Cluster newCluster;
		if (cloneVectors) {
			List<Vector> newClusteredVectors = new ArrayList<Vector>(getClusteredVectors().size());
			for (Vector vec : getClusteredVectors()) {
				newClusteredVectors.add(vec.clone());
			}
			try {
				newCluster = (Cluster) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new IllegalStateException();
			}
			newCluster.setClusteredVectors(newClusteredVectors);
		} else {
			try {
				newCluster = (Cluster) super.clone();
				List<Vector> newVectors = new ArrayList<Vector>(getClusteredVectors().size());
				for(Vector v : getClusteredVectors()) {
					newVectors.add(v);
				}
				newCluster.setClusteredVectors(newVectors);
			} catch (CloneNotSupportedException e) {
				throw new IllegalStateException();
			}
		}

		return newCluster;
	}

	public Cluster clone() {
		return clone(true);
	}
}
