package mishanesterenko.iad.lb3.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mishanesterenko.iad.lb3.util.Dataset;
import mishanesterenko.iad.lb3.util.StringAttribute;

public class DefaultAttributeLoader implements Loader {
	private BufferedReader attributeReader;

	public DefaultAttributeLoader(BufferedReader reader) {
		if (reader == null) {
			throw new NullPointerException("data set reader can not be null");
		}
		attributeReader = reader;
	}
	

	@Override
	public void load(Dataset ds) throws IOException {
		String line;
		{ // categorial attribute
			line = attributeReader.readLine();
			String[] values = line.split("\\s+");
			StringAttribute attrib = new StringAttribute("category", new ArrayList<String>(Arrays.asList(values)), true);
			ds.setCategorialAttribute(attrib);
		}

		List<StringAttribute> attributes = ds.getAttributes();
		while ((line = attributeReader.readLine()) != null) {
			String labelAndValues[] = line.split(":");
			String label = labelAndValues[0];
			String[] values = labelAndValues[1].split("\\s+");
			StringAttribute attrib = new StringAttribute(label, new ArrayList<String>(Arrays.asList(values)), false);
			attributes.add(attrib);
		}
	}


}
