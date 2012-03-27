package mishanesterenko.iad.lb1.core.clustering.kmedoids;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.clustering.IndexCentroidedCluster;
import mishanesterenko.iad.lb1.core.dataset.DataSet;
import mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.exception.ClusteringProcessingException;
import mishanesterenko.iad.lb1.core.exception.VectorDimensionMismatch;
import mishanesterenko.iad.lb1.core.plugin.ClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.plugin.ClusteringConfiguration;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public final class KMedoidsClusteringAlgorithm implements ClusteringAlgorithm {
	public static final double CONVERGE_EPSILON = 0.0000001;

	public String getName() {
		return "K-Medoids clustering";
	}

	public List<? extends Cluster> clusterVectors(ClusteringConfiguration conf) throws ClusteringProcessingException {
		try {
			KMedoidsConfiguration kMedoidsConf = (KMedoidsConfiguration) conf;
			DataSet dataSet = kMedoidsConf.getDataSet();
			DistanceFunction df = kMedoidsConf.getDistanceFunction();
			Set<Integer> indices = kMedoidsConf.getIndices();
			List<IndexCentroidedCluster> clusters = initializeClusters(indices, dataSet, df);

			double cost = Double.MAX_VALUE;
			List<IndexCentroidedCluster> workingClustersCopy = new ArrayList<IndexCentroidedCluster>(clusters.size());
			List<IndexCentroidedCluster> possibleEnhancementClustersCopy = new ArrayList<IndexCentroidedCluster>(clusters.size());
			while (true) {
				assignClusters(clusters, dataSet, df); // we have here medoids set
				double tempCost = computeCost(clusters, df);
				if (tempCost < cost) {
					cost = tempCost;
					copyClusters(possibleEnhancementClustersCopy, clusters);
				}

				boolean foundLess = false;
				Iterator<IndexCentroidedCluster> clusterIterator = clusters.iterator();
				while (clusterIterator.hasNext()) {
					IndexCentroidedCluster cluster = clusterIterator.next();
					Vector medoid = cluster.getCentroid();
					List<Vector> clusteredVectors = cluster.getClusteredVectors();
					Iterator<Vector> clusteredVectorIterator = clusteredVectors.iterator();
					while (clusteredVectorIterator.hasNext()) {
						Vector vec = clusteredVectorIterator.next();
						swapVector(medoid, vec);
						copyClusters(workingClustersCopy, clusters);
						swapVector(medoid, vec);
						
						assignClusters(workingClustersCopy, dataSet, df);
						tempCost = computeCost(workingClustersCopy, df);
						if (tempCost < cost) {
							cost = tempCost;
							copyClusters(possibleEnhancementClustersCopy, clusters);
							foundLess = true;
						}
					}
				}

				if (foundLess) {
					clusters = possibleEnhancementClustersCopy;
				} else {
					break;
				}
			}
			
			return clusters;
		} catch (Exception e) {
			throw new ClusteringProcessingException(e.getMessage(), e);
		}
	}

	protected void copyClusters(List<IndexCentroidedCluster> dest, List<IndexCentroidedCluster> source) {
		dest.clear();
		for(IndexCentroidedCluster sourceCluster : source) {
			dest.add(sourceCluster.clone(false));
		}
	}
	
	protected double computeCost(List<IndexCentroidedCluster> clusters, DistanceFunction df) throws VectorDimensionMismatch {
		double res = 0;
		for (IndexCentroidedCluster cluster : clusters) {
			Vector centroid = cluster.getCentroid();
			for (Vector vec : cluster.getClusteredVectors()) {
				res += df.computeDistance(centroid, vec);
			}
		}
		return res;
	}
	
	protected List<IndexCentroidedCluster> initializeClusters(Set<Integer> indices, DataSet dataSet, DistanceFunction df) throws VectorDimensionMismatch {
		int clusterCount = indices.size();
		List<IndexCentroidedCluster> clusters = new ArrayList<IndexCentroidedCluster>(clusterCount);

		for (Integer clusterInd : indices) {
			Vector centroid = dataSet.get(clusterInd).detach();
			IndexCentroidedCluster cluster = new IndexCentroidedCluster(centroid, clusterInd);
			clusters.add(cluster);
		}

		return clusters;
	}

	protected void assignClusters(List<IndexCentroidedCluster> clusters, DataSet dataSet, DistanceFunction df) throws VectorDimensionMismatch {
		for (Cluster cluster : clusters) {
			cluster.getClusteredVectors().clear();
		}

		int vecCount = dataSet.size();
		for (int vecInd = 0; vecInd < vecCount; ++vecInd) {
			Vector vec = dataSet.get(vecInd);
			Cluster addTo = null;
			double dist = Double.MAX_VALUE;
			for (IndexCentroidedCluster cluster : clusters) {
				Vector centroid = cluster.getCentroid();
				int centroidIndex = cluster.getCentroidIndex();
				if (centroidIndex == vecInd) {
					continue;
				}
				double d = df.computeDistance(centroid, vec);
				if (d < dist) {
					dist = d;
					addTo = cluster;
				}
			}
			if (addTo != null) {
				addTo.getClusteredVectors().add(vec);
			}
		}
	}

	public static void swapVector(Vector v1, Vector v2) throws VectorDimensionMismatch {
		if (v1.getCardinality() != v2.getCardinality()) {
			throw new VectorDimensionMismatch();
		}

		int dimCount = v1.getCardinality();
		for (int dimInd = 0; dimInd < dimCount; ++dimInd) {
			double transit = v1.getValue(dimInd);
			v1.setValue(dimInd, v2.getValue(dimInd));
			v2.setValue(dimInd, transit);
		}
	}
}
