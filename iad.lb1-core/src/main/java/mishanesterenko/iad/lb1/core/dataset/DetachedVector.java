package mishanesterenko.iad.lb1.core.dataset;

import mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector;

public class DetachedVector extends Vector {
	private double[] values;

	public DetachedVector(int dimensionCount) {
		values = new double[dimensionCount];
	}

	public DetachedVector(Vector vector) {
		int dimCount = vector.getCardinality();
		values = new double[dimCount];
		for (int valInd = 0; valInd < dimCount; ++valInd) {
			values[valInd] = vector.getValue(valInd);
		}
	}

	@Override
	public double getValue(int index) {
		return values[index];
	}

	@Override
	public void setValue(int index, double newValue) {
		values[index] = newValue;
	}

	@Override
	public int getCardinality() {
		return values.length;
	}

	@Override
	public Vector clone() {
		return new DetachedVector(this);
	}
}