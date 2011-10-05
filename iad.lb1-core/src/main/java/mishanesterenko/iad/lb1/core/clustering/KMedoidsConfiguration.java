package mishanesterenko.iad.lb1.core.clustering;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mishanesterenko.iad.lb1.core.plugin.ClusteringConfiguration;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class KMedoidsConfiguration extends ClusteringConfiguration {
	private Set<Integer> m_indices;

	public KMedoidsConfiguration(int dataSetSize, int clusterCount, DistanceFunction distanceFunction) {
		super(distanceFunction);
		if (dataSetSize <= 0) {
			throw new IllegalArgumentException("data set size can not be less or equal to zero");
		}
		if (clusterCount <= 0) {
			throw new IllegalArgumentException("cluster count can not be less or equal to zero");
		}
		if (clusterCount > dataSetSize) {
			throw new IllegalArgumentException("cluset count can not be more than data set size");
		}

		Set<Integer> inds = new HashSet<Integer>(clusterCount);
		while (clusterCount-- > 0) {
			int ind = (int) Math.round(Math.random()*clusterCount);
			inds.add(ind);
		}
		setIndices(inds);
	}

	public KMedoidsConfiguration(Set<Integer> indices, boolean doCopyStorage, DistanceFunction distanceFunction) {
		super(distanceFunction);
		if (doCopyStorage) {
			Set<Integer> inds = new HashSet<Integer>(indices.size());
			for (Integer ind : indices) {
				inds.add(ind);
			}
			setIndices(inds);
		} else {
			setIndices(indices);
		}
	}

	public KMedoidsConfiguration(Set<Integer> indices, DistanceFunction distanceFunction) {
		this(indices, true, distanceFunction);
	}

	protected Set<Integer> internalGetIndices() {
		return m_indices;
	}

	protected void setIndices(Set<Integer> indices) {
		m_indices = indices;
	}
	
	public Set<Integer> getIndices() {
		return Collections.unmodifiableSet(internalGetIndices());
	}
}
