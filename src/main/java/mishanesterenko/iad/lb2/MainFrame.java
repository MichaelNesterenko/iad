package mishanesterenko.iad.lb2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.Graphs;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.graph.JGraphSimpleLayout;
import com.jgraph.layout.organic.JGraphOrganicLayout;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private ListenableUndirectedWeightedGraph<String, MyWeightedEdge> graph;
	private ListenableUndirectedWeightedGraph<String, MyWeightedEdge> pristineGraph;
	private JGraph jgraph;
	private JGraphModelAdapter<String, MyWeightedEdge> adapter;
	private JTextField textField;

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

	private MyWeightedEdge currentEdge;
	private JButton btnApply;
	
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		graph = new ListenableUndirectedWeightedGraph<String, MyWeightedEdge>(MyWeightedEdge.class);
		
		jgraph = new JGraph(adapter = new JGraphModelAdapter<String, MyWeightedEdge>(graph));
		jgraph.setDisconnectable(false);
		jgraph.setConnectable(false);
		jgraph.setMoveBeyondGraphBounds(false);
		contentPane.add(jgraph, BorderLayout.CENTER);
		jgraph.setEditable(false);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Work");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jgraph.clearSelection();
				KruskalMinimumSpanningTree<String, MyWeightedEdge> algo = new KruskalMinimumSpanningTree<String, MyWeightedEdge>(graph);

				Set<MyWeightedEdge> graphEdges = graph.edgeSet();
				Set<MyWeightedEdge> algoEdges = algo.getEdgeSet();

				Set<MyWeightedEdge> tempEdges = new HashSet<MyWeightedEdge>(graphEdges);
				tempEdges.removeAll(algoEdges);
				graph.removeAllEdges(tempEdges);
			}
		});
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Reset");
		btnNewButton_1.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				graph.removeAllEdges(new HashSet<MyWeightedEdge>(graph.edgeSet()));
				graph.removeAllVertices(new HashSet<String>(graph.vertexSet()));
				copyGraph(pristineGraph, graph);
			}
		});
		panel.add(btnNewButton_1);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Weight:");
		panel_1.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setEnabled(false);
		lblNewLabel.setLabelFor(textField);
		panel_1.add(textField);
		textField.setColumns(10);
		
		btnApply = new JButton("Apply");
		btnApply.setEnabled(false);
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					graph.setEdgeWeight(currentEdge, (Double.parseDouble(textField.getText())));
				} catch (NumberFormatException ee) {
					JOptionPane.showMessageDialog(MainFrame.this, "Wrong number");
				}
			}
		});
		panel_1.add(btnApply);
		jgraph.addGraphSelectionListener(new GraphSelectionListener() {
			@Override
			public void valueChanged(GraphSelectionEvent arg0) {
				if (arg0.getCell() instanceof DefaultEdge) {
					textField.setEnabled(true);
					btnApply.setEnabled(true);

					currentEdge = (MyWeightedEdge) ((DefaultEdge) arg0.getCell()).getUserObject();
					textField.setText(Double.toString(graph.getEdgeWeight(currentEdge)));
				} else {
					textField.setText("");
					textField.setEnabled(false);
					btnApply.setEnabled(false);
				}
			}
		});

		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");

		Graphs.addEdge(graph, "v1", "v2", 0);
		Graphs.addEdge(graph, "v1", "v3", 1);
		Graphs.addEdge(graph, "v1", "v4", 2);
		Graphs.addEdge(graph, "v2", "v3", 3);
		Graphs.addEdge(graph, "v2", "v4", 4);
		Graphs.addEdge(graph, "v3", "v4", 5);

		pristineGraph = cloneGraph(graph, MyWeightedEdge.class);
	}

	public static <V, E> ListenableUndirectedWeightedGraph<V, E> cloneGraph(ListenableUndirectedWeightedGraph<V, E> source, Class<? extends E> edgeClass) {
		Set<E> edges = new HashSet<E>(source.edgeSet());
		Set<V> vertices = new HashSet<V>(source.vertexSet());

		ListenableUndirectedWeightedGraph<V, E> dest = new ListenableUndirectedWeightedGraph<V, E>(edgeClass);
		Graphs.addAllVertices(dest, vertices);
		for (E edge : edges) {
			Graphs.addEdge(dest, source.getEdgeSource(edge), source.getEdgeTarget(edge), source.getEdgeWeight(edge));
		}
		return dest;
	}

	public static <V, E> ListenableUndirectedWeightedGraph<V, E> copyGraph(ListenableUndirectedWeightedGraph<V, E> source, ListenableUndirectedWeightedGraph<V, E> dest) {
		Set<E> edges = new HashSet<E>(source.edgeSet());
		Set<V> vertices = new HashSet<V>(source.vertexSet());

		Graphs.addAllVertices(dest, vertices);
		for (E edge : edges) {
			Graphs.addEdge(dest, source.getEdgeSource(edge), source.getEdgeTarget(edge), source.getEdgeWeight(edge));
		}
		return dest;
	}

}
