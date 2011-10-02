package mishanesterenko.iad.lb1.stdplugin.distance;

import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.VectorDimensionMismatch;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class ManhattenDistancePlugin implements DistanceFunction {

	public double computeDistance(Vector v1, Vector v2) throws VectorDimensionMismatch {
		v1.validateDimensions(v2);
		double distance = 0;
		int dimCount = v1.getCardinality();
		for (int valInd = 0; valInd < dimCount; ++valInd) {
			distance += Math.abs(v1.getValue(valInd)) - Math.abs(v2.getValue(valInd));
		}
		return distance;
	}

	public String getName() {
		return "Manhatten distance";
	}

}
