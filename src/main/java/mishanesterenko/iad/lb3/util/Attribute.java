package mishanesterenko.iad.lb3.util;

import java.util.Collections;
import java.util.List;

public class Attribute<T> {
	private List<T> values;
	private String label;
	private boolean isCategorial;

	public Attribute(String lbl, List<T> vals, boolean categorial) {
		values = vals;
		label = lbl;
		isCategorial = categorial;
	}

	public String getLabel() {
		return label;
	}

	public List<T> getValues() {
		return Collections.unmodifiableList(values);
	}

	public boolean isCategorial() {
		return isCategorial;
	}

	public boolean isValueOk(T value) {
		return values.contains(value);
	}

	public Value createValue(T value) {
		return new Value(value);
	}

	@Override
	public String toString() {
		return label;
	}

	public class Value {
		private int index;

		public Value(T value) {
			index = values.indexOf(value);
			if (index == -1) {
				throw new IllegalArgumentException("wrong attribute value");
			}
		}

		public T getValue() {
			return values.get(index);
		}

		public Attribute<T> getAttribute() {
			return Attribute.this;
		}

		@Override
		public String toString() {
			return values.get(index) + " (" + Attribute.this + ")";
		}
	}
}
