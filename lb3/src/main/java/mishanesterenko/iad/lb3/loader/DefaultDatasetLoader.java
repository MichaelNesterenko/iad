package mishanesterenko.iad.lb3.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import mishanesterenko.iad.lb3.util.Dataset;
import mishanesterenko.iad.lb3.util.Dataset.Entry;
import mishanesterenko.iad.lb3.util.StringAttribute;

public class DefaultDatasetLoader implements Loader {
	private BufferedReader datasetReader;

	public DefaultDatasetLoader(BufferedReader reader) {
		if (reader == null) {
			throw new NullPointerException("data set reader can not be null");
		}
		datasetReader = reader;
	}
	

	@Override
	public void load(Dataset ds) throws IOException {
		boolean containsCategorial = false;
		List<StringAttribute> attributes = ds.getAttributes();
		StringAttribute categorialAttribute = ds.getCategorialAttribute();
		List<Entry> dsEntries = ds.getEntries();
		String line;
		
		line = datasetReader.readLine();
		if (line != null) {
			String values[] = line.split("\\s+");
			containsCategorial = values.length > 1 && categorialAttribute.isValueOk(values[values.length -1]);
			if (containsCategorial && values.length - 1 != attributes.size() || !containsCategorial && values.length != attributes.size()) {
				throw new IllegalStateException("wrong attribute value count");
			}

			Entry entry = createEntry(ds, containsCategorial, attributes, categorialAttribute, values);
			dsEntries.add(entry);
		}
		while ((line = datasetReader.readLine()) != null) {
			String values[] = line.split("\\s+");
			Entry entry = createEntry(ds, containsCategorial, attributes, categorialAttribute, values);
			dsEntries.add(entry);
		}
	}

	private Entry createEntry(Dataset ds, boolean containsCategorial, List<StringAttribute> attributes,
			StringAttribute categorialAttribute, String[] values) {
		Entry entry;
		if (containsCategorial) {
			StringAttribute.Value attrib = categorialAttribute.createValue(values[values.length -1]);
			entry = ds.new Entry(attrib);
		} else {
			entry = ds.new Entry(null);
		}
		for (int i = 0; i < values.length - (containsCategorial ? 1 : 0); ++i) {
			StringAttribute attr = attributes.get(i);
			StringAttribute.Value value = attr.createValue(values[i]);
			entry.getValues().add(value);
		}
		return entry;
	}

}
