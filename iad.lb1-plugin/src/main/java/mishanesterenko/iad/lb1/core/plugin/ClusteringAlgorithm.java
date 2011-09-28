package mishanesterenko.iad.lb1.core.plugin;

import java.util.List;

import mishanesterenko.iad.lb1.core.AbstractDataSet;
import mishanesterenko.iad.lb1.core.Cluster;

/**
 * Base interface for clustering algorithm.
 * @author Michael Nesterenko
 *
 */
public interface ClusteringAlgorithm {
	/**
	 * Return human readable algorithm name in English.
	 * @return string with alghorithm name
	 */
	public String getName();

	public List<Cluster> clusterVectors(List<AbstractDataSet.Vector> vectors);
}
