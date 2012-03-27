package mishanesterenko.iad.lb1.core.dataset;

import mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector;

public class UnmodifyableVector extends Vector {
	private Vector wrappedVector;

	public UnmodifyableVector(Vector vector) {
		if (vector == null) {
			throw new NullPointerException("Vector can not be null");
		}
		wrappedVector = vector;
	}

	@Override
	public double getValue(int index) {
		return wrappedVector.getValue(index);
	}

	@Override
	public void setValue(int index, double newValue) {
		throw new UnsupportedOperationException("Can not change immutable object");
	}

	@Override
	public int getCardinality() {
		return wrappedVector.getCardinality();
	}

	@Override
	public Vector clone() {
		return wrappedVector.clone();
	}

}
