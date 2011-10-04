package mishanesterenko.iad.lb1.core.plugin;

import java.util.List;

import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.ClusteringProcessingException;
import mishanesterenko.iad.lb1.core.DataSet;

/**
 * Base interface for clustering algorithm.
 * 
 * @author Michael Nesterenko
 * 
 */
public interface ClusteringAlgorithm {
	/**
	 * Return human readable algorithm name in English.
	 * 
	 * @return string with algorithm name
	 */
	public String getName();

	/**
	 * Clusters vectors using minimum available vector dimension. Clustering
	 * algorithm depends on implementation. Implementation should be thread
	 * safe.
	 * 
	 * @param dataSet
	 *            source of observations. All dimensions are used to build
	 *            clusters.
	 * @param clusterCount
	 *            count of clusters, must be equal to clusterCentroids.size()
	 * @param clusterCentroids
	 *            list of vectors defining initial cluster centroids if
	 *            necessary. This centroids could be changed during clustering
	 *            process.
	 * @return
	 */
	public List<Cluster> clusterVectors(DataSet dataSet, ClusteringConfiguration configuration) throws ClusteringProcessingException;
			//DistanceFunction distanceFunction, int clusterCount, List<Vector> clusterCentroids
}
