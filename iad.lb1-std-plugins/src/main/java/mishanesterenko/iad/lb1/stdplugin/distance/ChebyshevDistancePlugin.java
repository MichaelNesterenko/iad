package mishanesterenko.iad.lb1.stdplugin.distance;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.exception.VectorDimensionMismatch;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;

@PluginImplementation
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
