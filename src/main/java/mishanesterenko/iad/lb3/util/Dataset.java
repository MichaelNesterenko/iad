package mishanesterenko.iad.lb3.util;

import java.util.ArrayList;
import java.util.List;

public class Dataset {
	private List<Entry> entries;
	private List<StringAttribute> attributes;
	private StringAttribute categorialAttribute;

	public Dataset() {
		entries = new ArrayList<Entry>();
		attributes = new ArrayList<StringAttribute>();
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public List<StringAttribute> getAttributes() {
		return attributes;
	}

	public StringAttribute getCategorialAttribute() {
		return categorialAttribute;
	}

	public void setCategorialAttribute(StringAttribute attr) {
		categorialAttribute = attr;
	}

	public Dataset subset(StringAttribute.Value val) {
		Dataset newDataSet = new Dataset();
		newDataSet.setCategorialAttribute(categorialAttribute);
		List<Entry> newEntries = newDataSet.getEntries();
		for (Entry entry : entries) {
			if (entry.isEqual(val)) {
				newEntries.add(entry);
			}
		}
		return newDataSet;
	}

	public int count(StringAttribute.Value val) {
		int cnt = 0;
		for (Entry entry : entries) {
			if (entry.isEqual(val)) {
				cnt++;
			}
		}
		return cnt;
	}

	public class Entry {
		private List<StringAttribute.Value> values;
		private StringAttribute.Value category;

		public Entry(StringAttribute.Value cat) {
			this.values = new ArrayList<StringAttribute.Value>();
			this.category = cat;
		}

		public List<StringAttribute.Value> getValues() {
			return this.values;
		}

		public StringAttribute.Value getCategory() {
			return this.category;
		}

		public boolean isEqual(StringAttribute.Value query) {
			if (query.getAttribute().isCategorial()) {
				return query.getAttribute() == getCategorialAttribute() && query.getValue() == getCategory().getValue();
			} else {
				for (StringAttribute.Value val : values) {
					if (query.getAttribute() == val.getAttribute() && query.getValue().equals(query.getValue())) {
						return true;
					}
				}
			}
			return false;
		}
	}
}
