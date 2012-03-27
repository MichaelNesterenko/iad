package mishanesterenko.iad.lb3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import mishanesterenko.iad.lb3.id3.DecisionTreeNode;
import mishanesterenko.iad.lb3.id3.ID3Algorithm;
import mishanesterenko.iad.lb3.id3.LeafDecisionTreeNode;
import mishanesterenko.iad.lb3.loader.DefaultAttributeLoader;
import mishanesterenko.iad.lb3.loader.DefaultDatasetLoader;
import mishanesterenko.iad.lb3.util.Dataset;

public class EntryPoint {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Dataset ds = new Dataset();

		{
			InputStream attributeStream = EntryPoint.class.getResourceAsStream("/attributes.dat");
			BufferedReader attributesReader = new BufferedReader(new InputStreamReader(attributeStream));
			DefaultAttributeLoader attributesLoader = new DefaultAttributeLoader(attributesReader);
			attributesLoader.load(ds);
		}

		{
			InputStream trainingStream = EntryPoint.class.getResourceAsStream("/training.dat");
			BufferedReader trainingReader = new BufferedReader(new InputStreamReader(trainingStream));
			DefaultDatasetLoader trainingLoader = new DefaultDatasetLoader(trainingReader);
			trainingLoader.load(ds);
		}

		DecisionTreeNode root = ID3Algorithm.buildDecisionTree(ds);
		printTree(root, 0);
	}

	public static void printTree(DecisionTreeNode root, int level) {
		for (int i = 0; i < level; ++i) {
			System.out.print("|-");
		}
		if (root instanceof LeafDecisionTreeNode) {
			System.out.print(root.getParent().getSplittingAttribute().getLabel());
			int index = root.getParent().getChildren().indexOf(root);
			System.out.print(" (" + root.getParent().getSplittingAttribute().getValues().get(index) + ")");
			System.out.println();

			for (int i = 0; i < level + 1; ++i) {
				System.out.print("|-");
			}

			System.out.print(((LeafDecisionTreeNode)root).getCategory());
		} else if (root.getParent() != null) {
			System.out.print(root.getParent().getSplittingAttribute().getLabel());
			int index = root.getParent().getChildren().indexOf(root);
			System.out.print(" (" + root.getParent().getSplittingAttribute().getValues().get(index) + ")");
		} else {
			System.out.print(root.getSplittingAttribute().getLabel());
		}
		System.out.println();
		for (DecisionTreeNode child : root.getChildren()) {
			printTree(child, level + 1);
		}
	}

}
