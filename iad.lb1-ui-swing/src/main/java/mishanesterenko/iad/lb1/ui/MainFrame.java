package mishanesterenko.iad.lb1.ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.clustering.dbscan.DbScanClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.clustering.dbscan.DbScanConfiguration;
import mishanesterenko.iad.lb1.core.clustering.kmeans.KMeansClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.clustering.kmeans.KMeansConfiguration;
import mishanesterenko.iad.lb1.core.clustering.kmedoids.KMedoidsClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.clustering.kmedoids.KMedoidsConfiguration;
import mishanesterenko.iad.lb1.core.dataset.DataSet;
import mishanesterenko.iad.lb1.core.distancefunction.EuclideanDistance;
import mishanesterenko.iad.lb1.core.distancefunction.EuclideanSquaredDistance;
import mishanesterenko.iad.lb1.core.exception.ClusteringProcessingException;
import mishanesterenko.iad.lb1.core.plugin.ClusteringAlgorithm;
import mishanesterenko.iad.lb1.core.plugin.ClusteringConfiguration;
import mishanesterenko.iad.lb1.core.plugin.DistanceFunction;
import mishanesterenko.iad.lb1.stdplugin.distance.ChebyshevDistancePlugin;
import mishanesterenko.iad.lb1.stdplugin.distance.ManhattenDistancePlugin;

import javax.swing.DefaultComboBoxModel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class MainFrame extends JFrame {
	public static final String DATASET_PATH = "/dataset.dat";

	private static final String[] CLUSTERING_ALGORITHM_NAME = new String[] {"K-means", "K-medoids", "Db-scan"};
	private static final ClusteringAlgorithm[] CLUSTERING_ALGORITHM_IMPLEMENTATION = new ClusteringAlgorithm[CLUSTERING_ALGORITHM_NAME.length];
	private static final String[] DISTANCE_FUNCTION_NAME = new String[] {"Euclidean", "Euclidean squared", "Manhatten", "Chebyshev"};
	private static final DistanceFunction[] DISTANCE_FUNCTION_IMPLEMENTATION = new DistanceFunction[DISTANCE_FUNCTION_NAME.length];
	private final JPanel[] optionPanes = new JPanel[CLUSTERING_ALGORITHM_IMPLEMENTATION.length];

	private JPanel contentPane;
	private JButton btnCluster;

	private DataSet dataSet = new DataSet();
	private ClusteringApplication clusteringApplication;
	private ClusteringAlgorithm currentClusteringAlgorithm;
	private DistanceFunction currentDistanceFunction;
	private ClusteringConfiguration currentClusteringConfiguration;
	private JPanel currentOptionPane;

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

	/**
	 * Create the frame.
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public MainFrame() throws URISyntaxException, IOException, ParseException {
		loadDataSet();

		CLUSTERING_ALGORITHM_IMPLEMENTATION[0] = new KMeansClusteringAlgorithm();
		CLUSTERING_ALGORITHM_IMPLEMENTATION[1] = new KMedoidsClusteringAlgorithm();
		CLUSTERING_ALGORITHM_IMPLEMENTATION[2] = new DbScanClusteringAlgorithm();
		currentClusteringAlgorithm = CLUSTERING_ALGORITHM_IMPLEMENTATION[0];

		DISTANCE_FUNCTION_IMPLEMENTATION[0] = new EuclideanDistance();
		DISTANCE_FUNCTION_IMPLEMENTATION[1] = new EuclideanSquaredDistance();
		DISTANCE_FUNCTION_IMPLEMENTATION[2] = new ManhattenDistancePlugin();
		DISTANCE_FUNCTION_IMPLEMENTATION[3] = new ChebyshevDistancePlugin();
		currentDistanceFunction = DISTANCE_FUNCTION_IMPLEMENTATION[0];

		optionPanes[0] = getKMeansOptionPanel();
		optionPanes[1] = getKMedoidsOptionPanel();
		optionPanes[2] = getDbScanOptionPanel();
		currentOptionPane = optionPanes[0];

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 722, 409);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JToolBar toolBar = new JToolBar();
		toolBar.setRollover(true);
		contentPane.add(toolBar, BorderLayout.NORTH);

		JLabel lblAlgorithm = new JLabel("Algorithm:");
		toolBar.add(lblAlgorithm);

		JComboBox comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					return;
				}
				JComboBox source = (JComboBox) e.getSource();
				int selectedIndex = source.getSelectedIndex();
				currentClusteringAlgorithm = CLUSTERING_ALGORITHM_IMPLEMENTATION[selectedIndex];
				contentPane.remove(currentOptionPane);
				contentPane.add(currentOptionPane = optionPanes[selectedIndex], BorderLayout.EAST);
				contentPane.validate();
				currentOptionPane.repaint();
				btnCluster.setEnabled(false);
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(CLUSTERING_ALGORITHM_NAME));
		toolBar.add(comboBox);

		JLabel lblDistanceFunction = new JLabel("Distance function:");
		toolBar.add(lblDistanceFunction);

		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					return;
				}
				JComboBox source = (JComboBox) e.getSource();
				int selectedIndex = source.getSelectedIndex();
				currentDistanceFunction = DISTANCE_FUNCTION_IMPLEMENTATION[selectedIndex];
				btnCluster.setEnabled(false);
			}
		});
		comboBox_1.setModel(new DefaultComboBoxModel(DISTANCE_FUNCTION_NAME));
		toolBar.add(comboBox_1);

		btnCluster = new JButton("Cluster");
		btnCluster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					final List<? extends Cluster> clusters = currentClusteringAlgorithm.clusterVectors(currentClusteringConfiguration);
					clusteringApplication.enqueue(new Callable<Void>() {
						public Void call() throws Exception {
							clusteringApplication.setClusters(clusters);
							return null;
						}
					});
				} catch (ClusteringProcessingException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(MainFrame.this, e1.getMessage(), "Clustering error", JOptionPane.ERROR_MESSAGE);
				}
				JButton source = (JButton) e.getSource();
				source.setEnabled(false);
			}
		});
		btnCluster.setEnabled(false);
		toolBar.add(btnCluster);

		{ // option pane
			JPanel panel = currentOptionPane;
			contentPane.add(panel, BorderLayout.EAST);
		}

		Canvas j3dCanvas = createCanvas();
		contentPane.add(j3dCanvas, BorderLayout.CENTER);
	}

	protected JPanel getKMeansOptionPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(150, 10));

		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel = new JLabel("count:");
		panel.add(lblNewLabel);

		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(10, 0, 100000, 1));
		panel.add(spinner);

		JButton btnNewButton = new JButton("Configure");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentClusteringConfiguration = new KMeansConfiguration((Integer) spinner.getValue(), dataSet, currentDistanceFunction);
				btnCluster.setEnabled(true);
			}
		});
		panel.add(btnNewButton);
		return panel;
	}

	protected JPanel getKMedoidsOptionPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(150, 10));

		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel = new JLabel("count:");
		panel.add(lblNewLabel);

		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(10, 0, 100000, 1));
		panel.add(spinner);

		JButton btnNewButton = new JButton("Configure");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentClusteringConfiguration = new KMedoidsConfiguration(dataSet, (Integer) spinner.getValue(), currentDistanceFunction);
				btnCluster.setEnabled(true);
			}
		});
		panel.add(btnNewButton);
		return panel;
	}

	protected JPanel getDbScanOptionPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(150, 10));

		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		final JSpinner epsilonSpinner = new JSpinner();
		final JSpinner minPtsSpinner = new JSpinner();

		{
			JLabel lblNewLabel = new JLabel("Epsilon:");
			panel.add(lblNewLabel);

			epsilonSpinner.setModel(new SpinnerNumberModel(0.1, 0, 100000, 0.01));
			JSpinner.NumberEditor spinnerEditor = new JSpinner.NumberEditor(epsilonSpinner, "#,##0.00");
			epsilonSpinner.setEditor(spinnerEditor);
			panel.add(epsilonSpinner);
		}

		{
			JLabel minPtsLabel = new JLabel("Min pts:");
			panel.add(minPtsLabel);

			minPtsSpinner.setModel(new SpinnerNumberModel(5, 1, 100000, 1));
			panel.add(minPtsSpinner);
		}

		JButton btnNewButton = new JButton("Configure");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentClusteringConfiguration = new DbScanConfiguration(dataSet, currentDistanceFunction, (Double) epsilonSpinner.getValue(), (Integer) minPtsSpinner.getValue());
				btnCluster.setEnabled(true);
			}
		});
		panel.add(btnNewButton);
		return panel;
	}

	protected void loadDataSet() throws URISyntaxException, IOException, ParseException {
		InputStream resource = MainFrame.class.getClass().getResourceAsStream(DATASET_PATH);
		Reader dataSetReader = new BufferedReader(new InputStreamReader(resource));
		try {
			dataSet.load(dataSetReader, " +", '.');
		} finally {
			dataSetReader.close();
		}
	}

	private Canvas createCanvas() {
		AppSettings settings = new AppSettings(true);
		settings.setResolution(800, 600);
		settings.setFrameRate(60);
		settings.setAudioRenderer(null);

		clusteringApplication = new ClusteringApplication(dataSet);
		clusteringApplication.setPauseOnLostFocus(false);
		clusteringApplication.setSettings(settings);
		clusteringApplication.createCanvas();
		clusteringApplication.startCanvas();

		JmeCanvasContext context = (JmeCanvasContext) clusteringApplication.getContext();
		Canvas j3dCanvas = context.getCanvas();

		return j3dCanvas;
	}
}
