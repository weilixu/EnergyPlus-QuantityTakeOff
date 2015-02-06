package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import analyzer.recommender.DistributionInfo;
import analyzer.recommender.ObjectProperty;

public class RecommenderPanel extends JPanel implements TreeSelectionListener{
    
    private final DefaultMutableTreeNode node;
    private final JScrollPane treeScroller;
    private final JScrollPane editorScroller;
    private final JTree tree;
    private final JTextArea infoText;
    private final JTextArea tempText;
    private final JPanel editorPanel;
    private final JSplitPane splitPane;
    
    public RecommenderPanel(DefaultMutableTreeNode n){
	node = n;
	tree = new JTree(node);
	tree.getSelectionModel().setSelectionMode(
		TreeSelectionModel.SINGLE_TREE_SELECTION);
	tree.addTreeSelectionListener(this);
	
	treeScroller = new JScrollPane(tree);
	treeScroller.setBackground(Color.WHITE);
	
	editorPanel = new JPanel(new BorderLayout());
	editorPanel.setBackground(Color.WHITE);
	
	ObjectProperty op = (ObjectProperty)node.getUserObject();
	
	infoText = new JTextArea(op.getFullDescription());
	infoText.setLineWrap(true);
	infoText.setFont(new Font("Arial", Font.BOLD, 12));
	infoText.setEditable(false);
	
	tempText = new JTextArea("Data Info");
	tempText.setLineWrap(true);
	editorPanel.add(infoText, BorderLayout.PAGE_START);
	editorPanel.add(tempText,BorderLayout.CENTER);
	
	editorScroller = new JScrollPane(editorPanel);
	editorScroller.setBackground(Color.WHITE);
	
	splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	splitPane.setTopComponent(treeScroller);
	splitPane.setBottomComponent(editorScroller);
	
	Dimension minimumSize = new Dimension(100, 50);
	treeScroller.setMinimumSize(minimumSize);
	editorScroller.setMinimumSize(minimumSize);
	splitPane.setDividerLocation(500);
	//splitPane.setPreferredSize(new Dimension(300, 150));
	
	Border blackline = BorderFactory.createLineBorder(Color.black);
	TitledBorder title = BorderFactory.createTitledBorder(
                blackline, "Recommender");
	title.setTitleJustification(TitledBorder.LEFT);
	setBorder(title);
	setLayout(new BorderLayout());
	add(splitPane, BorderLayout.CENTER);
	setBackground(Color.WHITE);
    }

    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
		.getLastSelectedPathComponent();
	
	if(node == null){
	    return;
	}
	
	Object nodeInfo = node.getUserObject();
	if(node.getParent().getParent()==null){
	    //do nothing
	}else if(node.isLeaf()){
	    DistributionInfo di = (DistributionInfo)node.getUserObject();
	    displayLeaves(di);
	}
    }
    
    
    private void displayLeaves(DistributionInfo di){
	tempText.setText(di.getFullDescription());
	tempText.setEditable(false);
    }
}
