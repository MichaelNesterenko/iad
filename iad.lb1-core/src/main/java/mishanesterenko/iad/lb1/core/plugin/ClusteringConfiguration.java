package mishanesterenko.iad.lb1.core.plugin;

public class ClusteringConfiguration {
	private DistanceFunction m_distanceFunction;

	public ClusteringConfiguration(DistanceFunction distanceFunction) {
		if (distanceFunction == null) {
			throw new NullPointerException("distanceFunction can not be null");
		}
		this.m_distanceFunction = distanceFunction;
	}

	public DistanceFunction getDistanceFunction() {
		return m_distanceFunction;
	}
}
