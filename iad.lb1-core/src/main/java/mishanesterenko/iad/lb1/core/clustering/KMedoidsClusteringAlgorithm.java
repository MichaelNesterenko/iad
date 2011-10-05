package mishanesterenko.iad.lb1.core.clustering;

import java.util.List;

import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.ClusteringProcessingException;
import mishanesterenko.iad.lb1.core.DataSet;
import mishanesterenko.iad.lb1.core.plugin.ClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.plugin.ClusteringConfiguration;

public class KMedoidsClusteringAlgorithm implements ClusteringAlgorithm {

	public String getName() {
		return "K-Medoids clustering";
	}

	public List<Cluster> clusterVectors(DataSet dataSet, ClusteringConfiguration configuration)
			throws ClusteringProcessingException {
		// TODO Auto-generated method stub
		return null;
	}

}
