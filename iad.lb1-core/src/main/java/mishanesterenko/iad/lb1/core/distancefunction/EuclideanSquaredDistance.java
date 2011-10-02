package mishanesterenko.iad.lb1.core.distancefunction;

import mishanesterenko.iad.lb1.core.VectorDimensionMismatch;
import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class EuclideanSquaredDistance implements DistanceFunction {
	public double computeDistance(Vector v1, Vector v2) throws VectorDimensionMismatch {
		return computeEuclideanSquaredDistance(v1, v2);
	}

	public static double computeEuclideanSquaredDistance(Vector v1, Vector v2) throws VectorDimensionMismatch {
		v1.validateDimensions(v2);
		double res = 0;
		int dimCount = v1.getCardinality();
		for (int valInd = 0; valInd < dimCount; ++valInd) {
			double tmp = v1.getValue(valInd) - v2.getValue(valInd);
			res += tmp * tmp;
		}
		return res;
	}

	public String getName() {
		return "Euclidean squared distance";
	}
}
