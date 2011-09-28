package mishanesterenko.iad.lb1.core;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.Iterator;

public abstract class AbstractDataSet  implements Iterable<AbstractDataSet.Vector> {
	public abstract void load(Reader readerSource) throws IOException, ParseException;
	public abstract void load(Reader readerSource, String regexSeparator, char decimalSeparator) throws IOException, ParseException;
	public abstract Vector get(int mRow);
	public abstract int getMinCardinality();
	public abstract int getMaxCardinality();
	public abstract int size();
	public abstract double getValueAt(int row, int col);
	public abstract int getCardinalityAt(int row);
		
	public abstract class Vector {
		public abstract double getValue(int index);
		public abstract int getCardinality();
	}

	public abstract class VectorIterator implements Iterator<Vector> {
	}
}
