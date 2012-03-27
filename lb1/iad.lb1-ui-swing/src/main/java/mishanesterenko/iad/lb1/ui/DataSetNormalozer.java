package mishanesterenko.iad.lb1.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class DataSetNormalozer {

	/**
	 * @param args
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws URISyntaxException, IOException, ParseException {
		URL datasetUrl = DataSetNormalozer.class.getClass().getResource("/dataset.dat");
		URI datasetUri = datasetUrl.toURI();
		File datasetFile = new File(datasetUri);
		String updDatasetFilePath = datasetFile.toString() + "_upd";
		BufferedReader datasetReader = new BufferedReader(new InputStreamReader(new FileInputStream(datasetFile)));
		BufferedWriter datasetWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(updDatasetFilePath)));
		PrintWriter writer = new PrintWriter(datasetWriter, true);
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(dfs);
		int lineNumber = 0;
		try {
			String line;
			while ((line = datasetReader.readLine()) != null) {
				if (lineNumber++ > 0) {
					writer.print('\n');
				}
				String[] numbers = line.split("\\s+");
				for (int i = 0; i < 3; ++i) {
					if (i > 0) {
						writer.print('\t');
					}
					double val = df.parse(numbers[i]).doubleValue();
					val /= 100;
					writer.print(val);
				}
			}
		} finally {
			try {
				datasetReader.close();
			} catch (IOException e) {
			}
			writer.close();
		}
	}

}
