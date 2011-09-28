package mishanesterenko.iad.lb1.core;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.Iterator;

public class DataSetProjection extends AbstractDataSet {
	private AbstractDataSet underlyingDataset;
	private int[] selectedIndices;

	public DataSetProjection(AbstractDataSet dataSet, int[] selectedIndices) {
		if (dataSet == null) {
			throw new NullPointerException("Undelying dataset can not be null.");
		}
		int[] localCopy = new int[selectedIndices.length];
		System.arraycopy(selectedIndices, 0, localCopy, 0, selectedIndices.length);
		this.selectedIndices = localCopy;

		int minCardinality = dataSet.getMinCardinality();
		for(int ind = 0; ind < localCopy.length; ++ind) {
			if (localCopy[ind] < 0) {
				throw new IndexOutOfBoundsException("Selected index is less that zero.");
			} else if (localCopy[ind] != minCardinality) {
				throw new IndexOutOfBoundsException("Selected index is not globally accessible.");
			}
		}
		setUnderlyingDataset(dataSet);
	}

	public Iterator<AbstractDataSet.Vector> iterator() {
		return (Iterator<AbstractDataSet.Vector>)new VectorIterator(getUnderlyingDataset().iterator());
	}

	@Override
	public void load(Reader readerSource) throws IOException, ParseException {
		getUnderlyingDataset().load(readerSource);
	}

	@Override
	public void load(Reader readerSource, String regexSeparator,
			char decimalSeparator) throws IOException, ParseException {
		getUnderlyingDataset().load(readerSource, regexSeparator, decimalSeparator);
	}

	@Override
	public Vector get(int mRow) {
		return new ProjectedVector(getUnderlyingDataset().get(mRow));
	}

	@Override
	public int getMinCardinality() {
		return getCardinality();
	}

	@Override
	public int getMaxCardinality() {
		return getCardinality();
	}

	@Override
	public int size() {
		return getUnderlyingDataset().size();
	}

	@Override
	public double getValueAt(int row, int col) {
		return getUnderlyingDataset().getValueAt(row, getSelectedIndices()[col]);
	}

	@Override
	public int getCardinalityAt(int row) {
		return getCardinality();
	}

	public AbstractDataSet getUnderlyingDataset() {
		return underlyingDataset;
	}

	private void setUnderlyingDataset(AbstractDataSet underlyingDataset) {
		this.underlyingDataset = underlyingDataset;
	}

	public int[] getSelectedIndices() {
		int[] copy = new int[selectedIndices.length];
		System.arraycopy(selectedIndices, 0, copy, 0, copy.length);
		return copy;
	}

	protected int getCardinality() {
		return getSelectedIndices().length;
	}

	public class ProjectedVector extends AbstractDataSet.Vector {
		private Vector wrappedVector;

		protected ProjectedVector(Vector vector) {
			if (vector == null) {
				throw new NullPointerException("Wrapped vector can not be null");
			}
			setWrappedVector(vector);
		}

		public double getValue(int index) {
			return getWrappedVector().getValue(getSelectedIndices()[index]);
		}

		public int getCardinality() {
			return DataSetProjection.this.getCardinality();
		}

		public Vector getWrappedVector() {
			return wrappedVector;
		}

		private void setWrappedVector(Vector wrappedVector) {
			this.wrappedVector = wrappedVector;
		}
	}

	public class VectorIterator extends AbstractDataSet.VectorIterator {
		private Iterator<AbstractDataSet.Vector> wrappedIterator;

		public VectorIterator(Iterator<AbstractDataSet.Vector> iterator) {
			if (iterator == null) {
				throw new NullPointerException("Wrapped iterator can not be null.");
			}
			setWrappedIterator(iterator);
		}

		public Iterator<AbstractDataSet.Vector> getWrappedIterator() {
			return wrappedIterator;
		}

		private void setWrappedIterator(Iterator<AbstractDataSet.Vector> wrappedIterator) {
			this.wrappedIterator = wrappedIterator;
		}

		public boolean hasNext() {
			return getWrappedIterator().hasNext();
		}

		public Vector next() {
			return new ProjectedVector(getWrappedIterator().next());
		}

		public void remove() {
			getWrappedIterator().remove();
		}
	}
}
