package mishanesterenko.iad.lb1.core.plugin;

import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;

public interface DistanceFunction {
	public double computeDistance(Vector v1, Vector v2);
}
