package analyzer.decisiontree;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.classifiers.trees.REPTree;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class GenerateDecisionTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader file;
		try {
			file = new FileReader("/Users/Adrian/Dropbox/test.arff");
			BufferedReader reader = new BufferedReader(file);
			Instances data = new Instances(reader);
			reader.close();
			data.setClassIndex(data.numAttributes() - 1);
			REPTree tree = new REPTree();
			tree.buildClassifier(data);

			// Print tree
			System.out.println(tree);

			// display classifier
			javax.swing.JFrame jf = new javax.swing.JFrame(
					"Weka Classifier Tree Visualizer: J48");
			jf.setSize(1000, 400);
			jf.getContentPane().setLayout(new BorderLayout());
			TreeVisualizer tv = new TreeVisualizer(null,
					tree.graph(), new PlaceNode2());
			jf.getContentPane().add(tv, BorderLayout.CENTER);
			jf.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(
						java.awt.event.WindowEvent e) {
					jf.dispose();
				}
			});
			jf.setVisible(true);
			tv.fitToScreen();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Instances Weka failed");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
