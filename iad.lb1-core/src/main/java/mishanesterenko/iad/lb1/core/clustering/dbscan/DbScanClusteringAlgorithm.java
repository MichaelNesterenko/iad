package mishanesterenko.iad.lb1.core.clustering.dbscan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.DataSet;
import mishanesterenko.iad.lb1.core.exception.ClusteringProcessingException;
import mishanesterenko.iad.lb1.core.exception.VectorDimensionMismatch;
import mishanesterenko.iad.lb1.core.plugin.ClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.plugin.ClusteringConfiguration;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class DbScanClusteringAlgorithm implements ClusteringAlgorithm {

	public String getName() {
		return "DbScan clustering";
	}

	public List<Cluster> clusterVectors(ClusteringConfiguration configuration) throws ClusteringProcessingException {
		DbScanConfiguration dbScanConfig = (DbScanConfiguration)configuration;
		DataSet dataSet = dbScanConfig.getDataSet();
		double eps = dbScanConfig.getEps();
		int minCount = dbScanConfig.getMinCount();
		List<Cluster> clusters = new ArrayList<Cluster>();
		Set<Vector> visited = new HashSet<Vector>(dbScanConfig.getDataSet().size());
		Set<Vector> noise = new HashSet<Vector>();
		DistanceFunction df = dbScanConfig.getDistanceFunction();

		try {
			for (Vector vec : dbScanConfig.getDataSet()) {
				if (visited.contains(vec)) {
					continue;
				}
				List<Vector> neibhours = getNeighbours(dataSet, df, vec, eps);
				if (neibhours.size() < minCount) {
					noise.add(vec);
				} else {
					visited.add(vec);
					Cluster cluster = new Cluster(null);
					clusters.add(cluster);
					expandCluster(dataSet, cluster, vec, neibhours, df, eps, minCount, visited, noise);
				}
			}
		} catch (VectorDimensionMismatch e) {
			throw new ClusteringProcessingException(e.getMessage(), e);
		}

		return clusters;
	}

	protected void expandCluster(DataSet dataSet, Cluster cluster, Vector firstPoint, 
			List<Vector> neighbours, DistanceFunction df, double eps, int minCount, Set<Vector> visited, Set<Vector> noise) throws VectorDimensionMismatch {
		List<Vector> clusteredVectors = cluster.getClusteredVectors();
		clusteredVectors.add(firstPoint);

		for (Vector neighbour : neighbours) {
			if (noise.contains(neighbour)) {
				clusteredVectors.add(neighbour);
				noise.remove(neighbour);
				visited.add(neighbour);
			} else {
				List<Vector> moreNeighbours = getNeighbours(dataSet, df, neighbour, eps);
				if (moreNeighbours.size() >= minCount) {
					expandCluster(dataSet, cluster, neighbour, moreNeighbours, df, eps, minCount, visited, noise);
				}
			}
		}
	}

	protected List<Vector> getNeighbours(DataSet dataSet, DistanceFunction df, Vector center, double eps) throws VectorDimensionMismatch {
		List<Vector> neibhours = new ArrayList<Vector>();
		for (Vector vec : dataSet) {
			double distance = df.computeDistance(vec, center);
			if (distance != 0 && distance < eps) {
				neibhours.add(vec);
			}
		}
		return neibhours;
	}

}
