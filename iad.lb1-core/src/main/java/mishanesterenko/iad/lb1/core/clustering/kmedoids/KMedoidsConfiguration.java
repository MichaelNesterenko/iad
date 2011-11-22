package mishanesterenko.iad.lb1.core.clustering.kmedoids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mishanesterenko.iad.lb1.core.dataset.DataSet;
import mishanesterenko.iad.lb1.core.plugin.ClusteringConfiguration;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class KMedoidsConfiguration extends ClusteringConfiguration {
	private Set<Integer> m_indices;

	public KMedoidsConfiguration(DataSet dataSet, int clusterCount, DistanceFunction distanceFunction) {
		super(dataSet, distanceFunction);
		if (clusterCount <= 0) {
			throw new IllegalArgumentException("cluster count can not be less or equal to zero");
		}
		if (clusterCount > dataSet.size()) {
			throw new IllegalArgumentException("cluset count can not be more than data set size");
		}

		List<Integer> indices = new ArrayList<Integer>(dataSet.size());
		for (int ind = 0; ind < dataSet.size(); ++ind) {
			indices.add(ind);
		}
		while (indices.size() > clusterCount) {
			indices.remove((int) Math.floor(Math.random() * indices.size()));
		}
		Set<Integer> inds = new HashSet<Integer>(indices);
		setIndices(inds);
	}

	public KMedoidsConfiguration(DataSet dataSet, Set<Integer> indices, boolean doCopyStorage, DistanceFunction distanceFunction) {
		super(dataSet, distanceFunction);
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

	public KMedoidsConfiguration(DataSet dataSet, Set<Integer> indices, DistanceFunction distanceFunction) {
		this(dataSet, indices, true, distanceFunction);
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
