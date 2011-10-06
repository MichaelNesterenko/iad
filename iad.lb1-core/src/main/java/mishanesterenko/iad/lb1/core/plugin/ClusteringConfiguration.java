package mishanesterenko.iad.lb1.core.plugin;

import mishanesterenko.iad.lb1.core.DataSet;

public class ClusteringConfiguration {
	private DistanceFunction m_distanceFunction;
	private DataSet m_dataSet;

	public ClusteringConfiguration(DataSet dataSet, DistanceFunction distanceFunction) {
		if (distanceFunction == null) {
			throw new NullPointerException("distanceFunction can not be null");
		}
		if (dataSet == null) {
			throw new NullPointerException("dataSet can not be null");
		}
		this.m_distanceFunction = distanceFunction;
		this.m_dataSet = dataSet;
	}

	public DistanceFunction getDistanceFunction() {
		return m_distanceFunction;
	}

	public DataSet getDataSet() {
		return m_dataSet;
	}
}
