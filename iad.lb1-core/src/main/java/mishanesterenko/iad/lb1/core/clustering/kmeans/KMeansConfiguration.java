package mishanesterenko.iad.lb1.core.clustering.kmeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.DataSet;
import mishanesterenko.iad.lb1.core.DetachedVector;
import mishanesterenko.iad.lb1.core.UnmodifyableVector;
import mishanesterenko.iad.lb1.core.plugin.ClusteringConfiguration;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class KMeansConfiguration extends ClusteringConfiguration {
	private List<Vector> m_centroids;

	public KMeansConfiguration(int clusterCount, DataSet dataSet, DistanceFunction distanceFunction) {
		super(dataSet, distanceFunction);
		setCentroids(new ArrayList<Vector>(clusterCount));
		initializeCentroids(intGetCentroids(), clusterCount, dataSet);
	}

	public KMeansConfiguration(DataSet dataSet, List<Vector> centroids, DistanceFunction distanceFunction) {
		this(dataSet, centroids, true, distanceFunction);
	}

	public KMeansConfiguration(DataSet dataSet, List<Vector> centroids, boolean doCopyCentroids, DistanceFunction distanceFunction) {
		super(dataSet, distanceFunction);
		if (doCopyCentroids) {
			List<Vector> cntrds = new ArrayList<Vector>(centroids.size());
			for (Vector vec : centroids) {
				cntrds.add(new UnmodifyableVector(vec.detach()));
			}
			setCentroids(cntrds);
		} else {
			setCentroids(centroids);
		}
	}

	protected void initializeCentroids(List<Vector> centroids, int centroidCount, DataSet dataSet) {
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
		for (int centroidInd = 0; centroidInd < centroidCount; ++centroidInd) {
			Vector centroid = new DetachedVector(dimCount);
			for (int dimInd = 0; dimInd < dimCount; ++dimInd) {
				double dimVal = min[dimInd] + (max[dimInd] - min[dimInd]) * Math.random();
				centroid.setValue(dimInd, dimVal);
			}
			centroids.add(new UnmodifyableVector(centroid));
		}
	}

	protected List<Vector> intGetCentroids() {
		return m_centroids;
	}

	public List<Vector> getCentroids() {
		return Collections.unmodifiableList(intGetCentroids());
	}

	protected void setCentroids(List<Vector> m_centroids) {
		this.m_centroids = m_centroids;
	}
}
