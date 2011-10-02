package mishanesterenko.iad.lb1.core.distancefunction;

import mishanesterenko.iad.lb1.core.VectorDimensionMismatch;
import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class EuclideanDistance implements DistanceFunction {

	public double computeDistance(Vector v1, Vector v2) throws VectorDimensionMismatch {
		return Math.sqrt(EuclideanSquaredDistance.computeEuclideanSquaredDistance(v1, v2));
	}

	public String getName() {
		return "Euclidean distance";
	}

}
