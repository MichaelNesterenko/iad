package mishanesterenko.iad.lb1.core.clustering.kmeans;

import java.util.ArrayList;
import java.util.List;

import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.clustering.CentroidedCluster;
import mishanesterenko.iad.lb1.core.dataset.DataSet;
import mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector;
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

	public List<? extends Cluster> clusterVectors(final ClusteringConfiguration conf) throws ClusteringProcessingException {
		KMeansConfiguration kmeansConf = (KMeansConfiguration)conf;
		DataSet dataSet = kmeansConf.getDataSet();

		List<Vector> clusterCentroids = new ArrayList<Vector>(kmeansConf.getCentroids().size());
		for (Vector centroid : kmeansConf.getCentroids()) {
			clusterCentroids.add(centroid.detach());
		}

		if (dataSet.size() < clusterCentroids.size()) {
			throw new IllegalArgumentException("clusterCount is bigger than count of vectors");
		}

		List<CentroidedCluster> clusters = initializeClusters(clusterCentroids);

		try {
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

					double diff = df.computeDistance(centroid, prevCentroid);
					System.out.println("ind: " + ind + " diff: " + diff);
					if (converged) {
						converged = diff < CONVERGE_EPSILON;
					}

					prevCentroid.assignFrom(centroid);
				}
				System.out.println();
				if (converged) {
					break;
				}
			}
		} catch (VectorDimensionMismatch e) {
			throw new ClusteringProcessingException(e.getMessage(), e);
		}
		return clusters;
	}

	private void assignClusters(DataSet dataSet, List<CentroidedCluster> clusters, DistanceFunction distanceFunction) throws VectorDimensionMismatch {
		for (Cluster cluster : clusters) {
			cluster.getClusteredVectors().clear();
		}
		for (Vector vec : dataSet) {
			double minDistance = Double.MAX_VALUE;
			CentroidedCluster nearestCluster = null;
			for (CentroidedCluster cluster : clusters) {
				double distance = distanceFunction.computeDistance(vec, cluster.getCentroid());
				if (distance < minDistance) {
					minDistance = distance;
					nearestCluster = cluster;
				}
			}
			nearestCluster.getClusteredVectors().add(vec);
		}
	}

	private void recomputeMeans(List<CentroidedCluster> clusters) {
		if (clusters.size() > 0) {
			int dimCount = clusters.get(0).getCentroid().getCardinality();
			double []newCentroid = new double[dimCount];
			for (CentroidedCluster cluster : clusters) {
				int vecCount = cluster.getClusteredVectors().size();
				if (vecCount == 0) {
					continue;
				}
				Vector centroid = cluster.getCentroid();
				for (Vector vec : cluster.getClusteredVectors()) {
					for (int cInd = 0; cInd < dimCount; ++cInd) {
						newCentroid[cInd] += vec.getValue(cInd);
					}
				}
				for (int cInd = 0; cInd < dimCount; ++cInd) {
					double prevVal = newCentroid[cInd];
					newCentroid[cInd] /= vecCount;
					centroid.setValue(cInd, newCentroid[cInd]);
					newCentroid[cInd] = 0;
				}
			}
		}
	}

	private List<CentroidedCluster> initializeClusters(List<Vector> clusterCentroids) {
		int clusterCount = clusterCentroids.size();

		List<CentroidedCluster> clusters = new ArrayList<CentroidedCluster>(clusterCount);
		
		for (int clusterInd = 0; clusterInd < clusterCount; ++clusterInd) {
			clusters.add(new CentroidedCluster(clusterCentroids.get(clusterInd).detach()));
		}

		return clusters;
	}
}
