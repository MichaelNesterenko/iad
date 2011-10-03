package mishanesterenko.iad.lb1.stdplugin.clustering;

import java.util.List;

import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.ClusteringProcessingException;
import mishanesterenko.iad.lb1.core.DataSet;
import mishanesterenko.iad.lb1.core.plugin.ClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class KMedoidsClusteringPlugin implements ClusteringAlgorithm {

	public String getName() {
		return "K-Medoids clustering";
	}

	public List<Cluster> clusterVectors(DataSet dataSet, DistanceFunction distanceFunction, int clusterCount,
			List<Vector> clusterCentroids) throws ClusteringProcessingException {
		// TODO Auto-generated method stub
		return null;
	}

	private void initialzeClusters(DataSet dataSet, List<Vector>)

}
