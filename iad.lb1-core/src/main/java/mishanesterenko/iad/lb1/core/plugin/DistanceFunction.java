package mishanesterenko.iad.lb1.core.plugin;

import mishanesterenko.iad.lb1.core.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.exception.VectorDimensionMismatch;

public interface DistanceFunction {
	/**
	 * Computes distance between two vectors.
	 * Implementation should be thread safe.
	 * @param v1 the first vector
	 * @param v2 the second vector
	 * @return distance between vectors
	 * @throws VectorDimensionMismatch if vector dimension mismatches
	 */
	public double computeDistance(Vector v1, Vector v2) throws VectorDimensionMismatch;

	/**
	 * Return human readable name of distance function.
	 * @return String with function name
	 */
	public String getName();
}
