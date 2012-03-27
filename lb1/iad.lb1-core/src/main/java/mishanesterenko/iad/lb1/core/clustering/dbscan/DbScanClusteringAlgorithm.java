package mishanesterenko.iad.lb1.core.clustering.dbscan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.dataset.DataSet;
import mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector;
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
		ClusteringParams params = new ClusteringParams();
		
		DbScanConfiguration dbScanConfig = (DbScanConfiguration)configuration;
		params.dataSet = dbScanConfig.getDataSet();
		params.eps = dbScanConfig.getEps();
		params.minCount = dbScanConfig.getMinCount();
		List<Cluster> clusters = new ArrayList<Cluster>();
		params.visited = new HashSet<Vector>(dbScanConfig.getDataSet().size());
		params.distanceFunction = dbScanConfig.getDistanceFunction();

		try {
			for (Vector vec : dbScanConfig.getDataSet()) {
				if (params.visited.contains(vec)) {
					continue;
				}
				List<Vector> neibhours = getNeighbours(vec, params);
				if (neibhours.size() < params.minCount) {
					params.noise.add(vec);
				} else {
					params.visited.add(vec);
					Cluster cluster = new Cluster();
					clusters.add(cluster);
					expandCluster(cluster, vec, neibhours, params);
				}
			}
		} catch (VectorDimensionMismatch e) {
			throw new ClusteringProcessingException(e.getMessage(), e);
		}

		return clusters;
	}

	protected void expandCluster(Cluster cluster, Vector pointInCluster, List<Vector> neighbours, ClusteringParams params)
			throws VectorDimensionMismatch {
		List<Vector> clusteredVectors = cluster.getClusteredVectors();
		clusteredVectors.add(pointInCluster);
		for (Vector neighbour : neighbours) {
			if (params.visited.contains(neighbour)) {
				continue;
			}
			clusteredVectors.add(neighbour);
			params.noise.remove(neighbour);
			params.visited.add(neighbour);

			List<Vector> moreNeighbours = getNeighbours(neighbour, params);
			if (moreNeighbours.size() >= params.minCount) {
				expandCluster(cluster, neighbour, moreNeighbours, params);
			}
		}
	}

	protected List<Vector> getNeighbours(Vector center, ClusteringParams params) throws VectorDimensionMismatch {
		List<Vector> neibhours = new ArrayList<Vector>();
		for (Vector vec : params.dataSet) {
			double distance = params.distanceFunction.computeDistance(vec, center);
			if (distance != 0 && distance < params.eps) {
				neibhours.add(vec);
			}
		}
		return neibhours;
	}

	class ClusteringParams {
		public Set<Vector> noise = new HashSet<Vector>();
		public DistanceFunction distanceFunction;
		public DataSet dataSet;
		public int minCount;
		public double eps;
		public Set<Vector> visited;
	}
}
