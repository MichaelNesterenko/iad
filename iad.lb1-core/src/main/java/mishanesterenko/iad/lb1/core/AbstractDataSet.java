package mishanesterenko.iad.lb1.core;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.Iterator;

import mishanesterenko.iad.lb1.core.exception.VectorDimensionMismatch;

public abstract class AbstractDataSet  implements Iterable<AbstractDataSet.Vector> {
	public abstract void load(Reader readerSource) throws IOException, ParseException;
	public abstract void load(Reader readerSource, String regexSeparator, char decimalSeparator) throws IOException, ParseException;
	public abstract Vector get(int mRow);
	public abstract int getMinCardinality();
	public abstract int getMaxCardinality();
	public abstract int size();
	public abstract double getValueAt(int row, int col);
	public abstract void setValueAt(int row, int col, double newValue);
	public abstract int getCardinalityAt(int row);
		
	public abstract static class Vector {
		public abstract double getValue(int index);
		public abstract void setValue(int index, double newValue);
		public abstract int getCardinality();

		public final void validateDimensions(Vector other) throws VectorDimensionMismatch {
			if (!compareDimensions(other)) {
				throw new VectorDimensionMismatch();
			}
		}

		public boolean compareDimensions(Vector other) {
			int ownDimension = getCardinality();
			int otherDimension = other.getCardinality();
			return ownDimension == otherDimension;
		}

		/**
		 * Return copy of vector detached from its dataset, or just copy of vector if it is already detached. 
		 */
		public Vector detach() {
			return new DetachedVector(this);
		}

		public void assignFrom(Vector other) throws VectorDimensionMismatch {
			validateDimensions(other);
			int dimCount = getCardinality();
			for (int dimInd = 0; dimInd < dimCount; ++dimInd) {
				setValue(dimInd, other.getValue(dimInd));
			}
		}

		public abstract Vector clone();

		public boolean equals(Vector other) {
			int dimCount;
			if ((dimCount = other.getCardinality()) != getCardinality()) {
				return false;
			}
			for (int dimInd = 0; dimInd < dimCount; ++dimInd) {
				if (getValue(dimInd) != other.getValue(dimInd)) {
					return false;
				}
			}
			return true;
		}
		
		@Override
		public final boolean equals(Object other) {
			if (other instanceof Vector) {
				return equals((Vector)other);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			long hash = 0;
			int dimCount = getCardinality();
			for (int dimInd = 0; dimInd < dimCount; ++dimInd) {
				hash = 31 * hash + Double.doubleToLongBits(getValue(dimInd));
			}
			return (int) hash;
		}
	}

	public abstract class VectorIterator implements Iterator<Vector> {
	}
}
