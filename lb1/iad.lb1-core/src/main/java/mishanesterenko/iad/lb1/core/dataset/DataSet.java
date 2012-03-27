package mishanesterenko.iad.lb1.core.dataset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataSet extends AbstractDataSet {
	private List<double[]> mDataSet = new ArrayList<double[]>();
	private int mMinCardinality, mMaxCardinality;
	private WeakReference<Vector>[] mVectorCacheArray;

	@Override
	public void load(Reader readerSource) throws IOException, ParseException {
		load(readerSource, ";", '.');
	}

	@Override
	public void load(Reader readerSource, String regexSeparator, char decimalSeparator) throws IOException, ParseException {
		List<double[]> dataSet = getDataSet();
		dataSet.clear();

		int minCardinality = Integer.MAX_VALUE, maxCardinality = Integer.MIN_VALUE;
		BufferedReader br = new BufferedReader(readerSource);
		DecimalFormat df = new DecimalFormat();
		{ // decimal format symbols
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator(decimalSeparator);
			df.setDecimalFormatSymbols(dfs);
		}
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] data = line.split(regexSeparator);
			if (data.length < minCardinality) {
				minCardinality = data.length;
			} else if (data.length > maxCardinality) {
				maxCardinality = data.length;
			}
			double[] dataValues = new double[data.length];
			for (int dataIndex = 0; dataIndex < data.length; ++dataIndex) {
				dataValues[dataIndex] = df.parse(data[dataIndex]).doubleValue();
			}
			dataSet.add(dataValues);
		}
		mMinCardinality = minCardinality;
		mMaxCardinality = maxCardinality;

		mVectorCacheArray = new WeakReference[dataSet.size()];
	}

	@Override
	public Vector get(int mRow) {
		WeakReference<Vector>[] cachedVectors = getCache();
		WeakReference<Vector> cachedVectorReference = cachedVectors[mRow];
		Vector clusteredVector;
		if (cachedVectorReference == null) {
			clusteredVector = new Vector(mRow);
			cachedVectors[mRow] = new WeakReference<Vector>(clusteredVector);
		} else {
			clusteredVector = cachedVectorReference.get();
			if (clusteredVector == null) {
				clusteredVector = new Vector(mRow);
				cachedVectors[mRow] = new WeakReference<Vector>(clusteredVector);
			}
		}
		return clusteredVector;
	}

	@Override
	public int getMinCardinality() {
		return mMinCardinality;
	}

	@Override
	public int getMaxCardinality() {
		return mMaxCardinality;
	}

	@Override
	public int size() {
		return getDataSet().size();
	}

	protected List<double[]> getDataSet() {
		return mDataSet;
	}

	private WeakReference<Vector>[] getCache() {
		return mVectorCacheArray;
	}

	public Iterator<AbstractDataSet.Vector> iterator() {
		return (Iterator<AbstractDataSet.Vector>) new VectorIterator();
	}

	@Override
	public double getValueAt(int row, int col) {
		return getDataSet().get(row)[col];
	}

	@Override
	public void setValueAt(int row, int col, double newValue) {
		getDataSet().get(row)[col] = newValue;
	}

	@Override
	public int getCardinalityAt(int row) {
		return getDataSet().get(row).length;
	}

	public class Vector extends AbstractDataSet.Vector {
		private int mRow;
		
		protected Vector(int row) {
			mRow = row;
		}

		@Override
		public double getValue(int index) {
			return getValueAt(mRow, index);
		}

		@Override
		public int getCardinality() {
			return getCardinalityAt(mRow);
		}

		@Override
		public void setValue(int index, double newValue) {
			setValueAt(mRow, index, newValue);
		}

		@Override
		public mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector clone() {
			return this;
		}
	}

	public class VectorIterator extends AbstractDataSet.VectorIterator {
		private int mPos = -1;

		public void remove() {
			if (mPos < 0 || mPos > getDataSet().size()) {
				throw new IndexOutOfBoundsException();
			}
		}
		
		public Vector next() {
			Vector v = get(++mPos);
			return v;
		}
		
		public boolean hasNext() {
			return mPos + 1 < getDataSet().size();
		}
	}
}
