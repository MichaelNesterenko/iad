package mishanesterenko.iad.lb1.stdplugin.clustering;

import java.util.ArrayList;
import java.util.List;

import mishanesterenko.iad.lb1.core.AbstractDataSet;
import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.ClusteringProcessingException;
import mishanesterenko.iad.lb1.core.DataSet;
import mishanesterenko.iad.lb1.core.VectorDimensionMismatch;
import mishanesterenko.iad.lb1.core.plugin.ClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class KMeansClustering implements ClusteringAlgorithm {
	public static final double CONVERGE_EPSILON = 0.0000001;

	public String getName() {
		return "K-Means clustering";
	}

	public List<Cluster> clusterVectors(final DataSet dataSet, final DistanceFunction distanceFunction, final int clusterCount,
			List<Vector> clusterCentroids) throws ClusteringProcessingException {
		if (clusterCentroids != null && clusterCount != clusterCentroids.size()) {
			throw new IllegalArgumentException("clusterCount differs from clusterCentroids.size()");
		}

		List<Cluster> clusters = new ArrayList<Cluster>(clusterCount);
		clusters = initializeClusters(dataSet, clusters, clusterCentroids, clusterCount);

		/*
		 * Clustering process.
		 * PRECONDITION: clusterCentroids contains initial cluster centroids
		 * either randomly computer or received from formal parameter
		 */
		try {
			int dimCount = dataSet.getMinCardinality();
			while (true) {
				assignClusters(dataSet, clusters, distanceFunction);
				recomputeMeans(clusters);
				
				//checking if solution has converged
				boolean converged = true;
				for (int ind = 0; ind < clusterCount; ++ind) {
					Vector centroid = clusters.get(ind).getCentroid();
					Vector prevCentroid = clusterCentroids.get(ind);

					if (converged) {
						for (int dimInd = 0; dimInd < dimCount; ++dimInd) {
							converged = Math.abs(centroid.getValue(dimInd) - prevCentroid.getValue(dimInd)) < CONVERGE_EPSILON;
							if (!converged) {
								break;
							}
						}
					}

					prevCentroid.assignFrom(centroid);
				}
				if (converged) {
					break;
				}
			}
		} catch (VectorDimensionMismatch e) {
			throw new ClusteringProcessingException(e.getMessage(), e);
		}
		return clusters;
	}

	private void assignClusters(DataSet dataSet, List<Cluster> clusters, DistanceFunction distanceFunction) throws VectorDimensionMismatch {
		for (Vector vec : dataSet) {
			double minDistance = Double.MAX_VALUE;
			Cluster nearestCluster = null;
			for (Cluster cluster : clusters) {
				double distance = distanceFunction.computeDistance(vec, cluster.getCentroid());
				if (distance < minDistance) {
					minDistance = distance;
					nearestCluster = cluster;
				}
			}
			nearestCluster.getClusteredVectors().add(vec);
		}
	}

	private void recomputeMeans(List<Cluster> clusters) {

	}

	private List<Cluster> initializeClusters(DataSet dataSet, List<Cluster> clusters, List<Vector> clusterCentroids, int clusterCount) {
		if (clusterCentroids == null) {
			clusterCentroids = new ArrayList<Vector>(clusterCount);

			int dimCount = dataSet.getMinCardinality();
			double []min = new double[dimCount];
			double []max = new double[dimCount];
			// min max initialization
			{
				for (int dimInd = 0; dimInd < dimCount; ++dimInd) {
					min[dimInd] = Double.MAX_VALUE;
					max[dimInd] = Double.MIN_VALUE;
				}
				for (Vector vec : dataSet) {
					for (int dimInd = 0; dimInd < dimCount; ++dimInd) {
						double val = vec.getValue(dimInd);
						if (val < min[dimInd]) {
							min[dimInd] = val;
						}
						if (val > max[dimInd]) {
							max[dimInd] = val;
						}
					}
				}
			}
			Vector centroid = new AbstractDataSet.DetachedVector(dimCount);
			for (int dimInd = 0; dimInd < dimCount; ++dimInd) {
				double dimVal = min[dimInd] + (max[dimInd] - min[dimInd]) * Math.random();
				centroid.setValue(dimInd, dimVal);
			}
		}

		if (clusters == null) {
			clusters = new ArrayList<Cluster>(clusterCount);
		}

		for (int clusterInd = 0; clusterInd < clusterCount; ++clusterInd) {
			clusters.add(new Cluster(clusterCentroids.get(clusterInd)));
		}

		return clusters;
	}
}
