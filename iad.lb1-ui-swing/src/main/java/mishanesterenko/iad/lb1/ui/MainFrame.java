package mishanesterenko.iad.lb1.ui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import mishanesterenko.iad.lb1.core.DataSet;

public class MainFrame {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, InterruptedException {
		DataSet ds = new DataSet();
		ds.load(new FileReader("E:\\Университет\\4 курс\\ИАД\\лб\\1\\данные\\K-Means\\earthquake.txt"), "\\s+", '.');
		for (int ci = 0; ci < ds.size(); ++ci) {
			DataSet.Vector v = ds.get(ci);
			v = ds.get(ci);
		}
	}

}
