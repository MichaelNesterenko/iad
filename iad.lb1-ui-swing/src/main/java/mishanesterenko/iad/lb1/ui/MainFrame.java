package mishanesterenko.iad.lb1.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.clustering.dbscan.DbScanClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.clustering.dbscan.DbScanConfiguration;
import mishanesterenko.iad.lb1.core.clustering.kmeans.KMeansClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.clustering.kmeans.KMeansConfiguration;
import mishanesterenko.iad.lb1.core.clustering.kmedoids.KMedoidsClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.clustering.kmedoids.KMedoidsConfiguration;
import mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.dataset.DataSet;
import mishanesterenko.iad.lb1.core.distancefunction.EuclideanDistance;
import mishanesterenko.iad.lb1.core.distancefunction.EuclideanSquaredDistance;
import mishanesterenko.iad.lb1.core.exception.ClusteringProcessingException;
import mishanesterenko.iad.lb1.core.plugin.ClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.plugin.ClusteringConfiguration;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;
import mishanesterenko.iad.lb1.stdplugin.distance.ChebyshevDistancePlugin;
import mishanesterenko.iad.lb1.stdplugin.distance.ManhattenDistancePlugin;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 667780228063658143L;

	public static final String DATASET_PATH = "/dataset_2d.dat";

	private JPanel contentPane;

	private DataSet dataSet = new DataSet();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Random r = new Random();

	/**
	 * Create the frame.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public MainFrame() throws URISyntaxException, IOException, ParseException {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					if (e.getKeyChar() == '1') {
						KMeansConfiguration conf = new KMeansConfiguration(10, dataSet, new EuclideanDistance());
						KMeansClusteringAlgorithm algo = new KMeansClusteringAlgorithm();
						clusters = algo.clusterVectors(conf);
					} else if (e.getKeyChar() == '2') {
						KMedoidsConfiguration conf = new KMedoidsConfiguration(dataSet, 10, new EuclideanDistance());
						KMedoidsClusteringAlgorithm algo = new KMedoidsClusteringAlgorithm();
						clusters = algo.clusterVectors(conf);
					} else if (e.getKeyChar() == '3') {
						DbScanConfiguration conf = new DbScanConfiguration(dataSet, new EuclideanDistance(), 7, 5);
						DbScanClusteringAlgorithm algo = new DbScanClusteringAlgorithm();
						clusters = algo.clusterVectors(conf);
					}
				} catch (ClusteringProcessingException e1) {
					JOptionPane.showMessageDialog(MainFrame.this, "Alas, there a problem with clustering. " + e1.getMessage());
				}

				colors = new ArrayList<Color>(clusters.size());
				for (int i = 0; i < clusters.size(); ++i) {
					colors.add(new Color(Math.abs(r.nextInt()) % 256, Math.abs(r.nextInt()) % 256, Math.abs(r.nextInt()) % 256, 255));
				}
				repaint();
			}
		});
		loadDataSet();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 526, 611);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(new DrawPanel(), BorderLayout.CENTER);
	}

	protected void loadDataSet() throws URISyntaxException, IOException, ParseException {
		InputStream resource = MainFrame.class.getClass().getResourceAsStream(DATASET_PATH);
		Reader dataSetReader = new BufferedReader(new InputStreamReader(resource));
		try {
			dataSet.load(dataSetReader, "\\s+", '.');
		} finally {
			dataSetReader.close();
		}
	}

	private List<? extends Cluster> clusters;
	List<Color> colors;

	protected class DrawPanel extends JPanel {
		public void paint(Graphics g) {
			if (clusters == null) {
				for (Vector v : dataSet) {
					g.drawRect((int) v.getValue(0), (int) v.getValue(1), 1, 1);
				}
			} else {
				int index = 0;
				for (Cluster c : clusters) {
					for (Vector v : c.getClusteredVectors()) {
						g.setColor(colors.get(index));
						g.drawRect((int) v.getValue(0), (int) v.getValue(1), 1, 1);
					}
					index++;
				}
			}
		}
	}
}
