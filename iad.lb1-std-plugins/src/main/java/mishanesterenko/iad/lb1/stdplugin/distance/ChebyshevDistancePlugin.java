package mishanesterenko.iad.lb1.stdplugin.distance;

import mishanesterenko.iad.lb1.core.VectorDimensionMismatch;
import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

public class ChebyshevDistancePlugin implements DistanceFunction {

	public double computeDistance(Vector v1, Vector v2) throws VectorDimensionMismatch {
		v1.validateDimensions(v2);

		double distance = 0;
		int dimCount = v1.getCardinality();
		for (int valInd = 0; valInd < dimCount; ++valInd) {
			double possibleDistance = Math.abs(v1.getValue(valInd) - v2.getValue(valInd));
			if (possibleDistance > distance) {
				distance = possibleDistance;
			}
		}
			
		return distance;
	}

	public String getName() { 
		return "Chebyshev distance";
	}

}