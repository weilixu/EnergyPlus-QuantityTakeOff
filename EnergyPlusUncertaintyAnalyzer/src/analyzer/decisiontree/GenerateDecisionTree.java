package analyzer.decisiontree;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
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
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class GenerateDecisionTree {
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {


			File file = new File("/Users/Adrian/Dropbox/test.csv");
			CSVLoader loader = new CSVLoader();
			
			loader.setSource(file);
			
			Instances data = loader.getDataSet();

		
			data.setClassIndex(data.numAttributes()-1);
			
			REPTree tree = new REPTree();
			tree.buildClassifier(data);
			
//			J48 tree = new J48(); // new instance of tree 
//			tree.buildClassifier(data);
			
			System.out.println(tree);

			TreeVisualizer tv=new TreeVisualizer(null,tree.graph(),new PlaceNode2());
			JFrame jf=new JFrame("Weka Classifier Tree Visualizer: J48");
			jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			jf.setSize(800,600);
			jf.getContentPane().setLayout(new BorderLayout());
			jf.getContentPane().add(tv,BorderLayout.CENTER);
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
