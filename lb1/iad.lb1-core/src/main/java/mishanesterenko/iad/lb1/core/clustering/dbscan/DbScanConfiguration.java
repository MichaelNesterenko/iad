package mishanesterenko.iad.lb1.core.clustering.dbscan;

import mishanesterenko.iad.lb1.core.dataset.DataSet;
import mishanesterenko.iad.lb1.core.plugin.ClusteringConfiguration;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class DbScanConfiguration extends ClusteringConfiguration {
	private double eps;
	private int minCount;

	public DbScanConfiguration(DataSet dataSet, DistanceFunction df, double eps, int minCount) {
		super(dataSet, df);
		if (minCount <= 0) {
			throw new IllegalArgumentException();
		}
		this.eps = eps;
		this.minCount = minCount;
	}

	public double getEps() {
		return eps;
	}

	public int getMinCount() {
		return minCount;
	}

}
