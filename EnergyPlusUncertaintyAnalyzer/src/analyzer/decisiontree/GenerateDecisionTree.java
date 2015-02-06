package analyzer.decisiontree;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;

import com.sun.deploy.uitoolkit.impl.fx.Utils;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class GenerateDecisionTree {
	
//	public Instances createInstance(String[] randVars){
//		
//		return null;
//		
//	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader file;
		try {
			file = new FileReader("C:\\Users\\Weili\\Desktop\\New folder\\test.arff");
			BufferedReader reader = new BufferedReader(file);
			Instances data = new Instances(reader);
			reader.close();
			data.setClassIndex(data.numAttributes()-1);
//			System.out.println("\nDataset:\n");
//			System.out.println(data);
			

			
			REPTree tree = new REPTree();
			tree.buildClassifier(data);

			// Print tree
//			System.out.println(tree);

//			TreeVisualizer tv=new TreeVisualizer(null,tree.graph(),new PlaceNode2());
//			JFrame jf=new JFrame("Weka Classifier Tree Visualizer: J48");
//			jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//			jf.setSize(800,600);
//			jf.getContentPane().setLayout(new BorderLayout());
//			jf.getContentPane().add(tv,BorderLayout.CENTER);
//			jf.setVisible(true);
//			tv.fitToScreen();
			
			AttributeSelection attsel = new AttributeSelection();  // package weka.attributeSelection!
			CfsSubsetEval eval = new CfsSubsetEval();
			GreedyStepwise search = new GreedyStepwise();
			search.setSearchBackwards(true);
			attsel.setEvaluator(eval);
			attsel.setSearch(search);
			attsel.SelectAttributes(data);
			attsel.setRanking(true);
			System.out.println("here");
			  // obtain the attribute indices that were selected
			int[] indices = attsel.selectedAttributes();
			System.out.println(Arrays.toString(indices));
			double[] a=attsel.rankedAttributes()[0];
			System.out.println(Arrays.toString(a));
			
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
