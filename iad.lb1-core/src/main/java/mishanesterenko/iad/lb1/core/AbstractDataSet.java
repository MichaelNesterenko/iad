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
	}

	public abstract class VectorIterator implements Iterator<Vector> {
	}
}
