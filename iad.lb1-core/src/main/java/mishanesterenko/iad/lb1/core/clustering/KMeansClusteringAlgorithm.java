package mishanesterenko.iad.lb1.core.clustering;

import java.util.ArrayList;
import java.util.List;

import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.DataSet;
import mishanesterenko.iad.lb1.core.exception.ClusteringProcessingException;
import mishanesterenko.iad.lb1.core.exception.VectorDimensionMismatch;
import mishanesterenko.iad.lb1.core.plugin.ClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.plugin.ClusteringConfiguration;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class KMeansClusteringAlgorithm implements ClusteringAlgorithm {
	public static final double CONVERGE_EPSILON = 0.0000001;

	public String getName() {
		return "K-Means clustering";
	}

	public List<Cluster> clusterVectors(final ClusteringConfiguration conf) throws ClusteringProcessingException {
		KMeansConfiguration kmeansConf = (KMeansConfiguration)conf;
		DataSet dataSet = kmeansConf.getDataSet();
		List<Vector> clusterCentroids = kmeansConf.getCentroids();

		if (dataSet.size() < clusterCentroids.size()) {
			throw new IllegalArgumentException("clusterCount is bigger than count of vectors");
		}

		List<Cluster> clusters = initializeClusters(clusterCentroids);

		try {
			final int dimCount = dataSet.getMinCardinality();
			final int clusterCount = clusterCentroids.size();
			final DistanceFunction df = kmeansConf.getDistanceFunction();

			while (true) {
				assignClusters(dataSet, clusters, df);
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
		for (Cluster cluster : clusters) {
			cluster.getClusteredVectors().clear();
		}
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
		if (clusters.size() > 0) {
			int dimCount = clusters.get(0).getCentroid().getCardinality();
			double []newCentroid = new double[dimCount];
			for (Cluster cluster : clusters) {
				int vecCount = cluster.getClusteredVectors().size();
				Vector centroid = cluster.getCentroid();
				for (Vector vec : cluster.getClusteredVectors()) {
					for (int cInd = 0; cInd < dimCount; ++cInd) {
						newCentroid[cInd] += vec.getValue(cInd);
					}
				}
				for (int cInd = 0; cInd < dimCount; ++cInd) {
					newCentroid[cInd] /= vecCount;
					centroid.setValue(cInd, newCentroid[cInd]);
					newCentroid[cInd] = 0;
				}
			}
		}
	}

	private List<Cluster> initializeClusters(List<Vector> clusterCentroids) {
		int clusterCount = clusterCentroids.size();

		List<Cluster> clusters = new ArrayList<Cluster>(clusterCount);
		
		for (int clusterInd = 0; clusterInd < clusterCount; ++clusterInd) {
			clusters.add(new Cluster(clusterCentroids.get(clusterInd)));
		}

		return clusters;
	}
}
