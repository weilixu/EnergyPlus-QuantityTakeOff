package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import analyzer.lifecyclecost.DataObjects;
import analyzer.lifecyclecost.FieldElement;
import analyzer.lifecyclecost.LifeCycleCostModel;
import analyzer.lifecyclecost.TemplateObject;
import analyzer.model.Model;

public class LifeCycleCostPanel extends JPanel implements TreeSelectionListener {

    private final Model model;
    private JTree tree;
    private final JScrollPane treeView;

    private final JPanel editorPanel;
    private final JPanel tablePanel;
    private final JScrollPane editorView;

    private final JSplitPane splitPane;

    public LifeCycleCostPanel(Model m) {
	super(new BorderLayout());
	model = m;

	tree = new JTree(model.getLCCCompleteTree());
	tree.getSelectionModel().setSelectionMode(
		TreeSelectionModel.SINGLE_TREE_SELECTION);
	tree.addTreeSelectionListener(this);
	treeView = new JScrollPane(tree);

	editorPanel = new JPanel();
	editorPanel.setLayout(new BorderLayout());
	editorPanel.setBackground(Color.WHITE);

	tablePanel = new JPanel();
	tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.PAGE_AXIS));
	tablePanel.setBackground(Color.WHITE);

	editorView = new JScrollPane(editorPanel);

	splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	splitPane.setTopComponent(treeView);
	splitPane.setBottomComponent(editorView);

	add(splitPane, BorderLayout.CENTER);

    }

    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
		.getLastSelectedPathComponent();

	if (node == null) {
	    return;
	}

	Object nodeInfo = node.getUserObject();
	if (node.isLeaf()) {
	    DataObjects dataSet = (DataObjects) nodeInfo;
	    DataObjects copiedDataset = model.getCopiedDataObject(dataSet);
	    model.setCurrentDataset(copiedDataset);
	    displayObject(copiedDataset);
	}
    }

    private void displayObject(DataObjects dataSet) {
	editorPanel.removeAll();
	tablePanel.removeAll();
	JTextField dataSetName = new JTextField(dataSet.getSetName());
	dataSetName.setBackground(Color.darkGray);
	dataSetName.setFont(new Font("Helvetica", Font.BOLD, 22));
	dataSetName.setForeground(Color.WHITE);
	dataSetName.setEditable(false);
	editorPanel.add(dataSetName, BorderLayout.PAGE_START);

	ArrayList<TemplateObject> objects = dataSet.getObjects();
	for (TemplateObject object : objects) {
	    JPanel tempPanel = new JPanel(new BorderLayout());
	    JTextField objectName = new JTextField(object.getObject());
	    objectName.setBackground(Color.BLACK);
	    objectName.setFont(new Font("Helvetica", Font.BOLD, 14));
	    objectName.setForeground(Color.WHITE);
	    objectName.setEditable(false);

	    tempPanel.add(objectName, BorderLayout.PAGE_START);
	    ArrayList<FieldElement> fields = object.getFieldList();
	    String[] columnNames = { "Field", "Inputs", "Minimum", "Maximum" };
	    Object[][] data = new Object[fields.size()][4];
	    JTable table = null;
	    if (object.getReference().equals("Template")) {
		for (int i = 0; i < fields.size(); i++) {
		    data[i][0] = fields.get(i).getDescription();
		    data[i][2] = fields.get(i).getMinimum();
		    data[i][3] = fields.get(i).getMaximum();
		    if (fields.get(i).isKeyElement()) {
			data[i][1] = new JComboBox<String>(fields.get(i)
				.getOptionList()) {
			    @Override
			    public String toString() {
				return "Options";
			    }
			};
		    } else {
			data[i][1] = fields.get(i).getType();
		    }
		}
		table = createFalseEnabledTable(data, columnNames);
	    } else {
		for (int i = 0; i < fields.size(); i++) {
		    data[i][0] = fields.get(i).getDescription();
		    data[i][1] = fields.get(i).getValue();
		}
		table = createTrueEnabledTable(data, columnNames, dataSet,
			fields);
	    }

	    JScrollPane scrollPane = new JScrollPane(table);
	    tempPanel.add(scrollPane, BorderLayout.CENTER);

	    tablePanel.add(tempPanel);
	    tablePanel.setBackground(Color.WHITE);
	}

	editorPanel.add(tablePanel, BorderLayout.CENTER);

	editorPanel.revalidate();
	editorPanel.repaint();
    }

    private JTable createFalseEnabledTable(Object[][] data, String[] columnNames) {
	JTable table = new JTable(data, columnNames) {
	    @Override
	    public TableCellEditor getCellEditor(int row, int column) {
		Object value = super.getValueAt(row, column);
		if (value != null) {
		    if (value instanceof JComboBox) {
			return new DefaultCellEditor((JComboBox) value);
		    }
		    return getDefaultEditor(value.getClass());
		}
		return super.getCellEditor(row, column);
	    }
	};

	table.getColumnModel().getColumn(1)
		.setCellRenderer(new DefaultTableCellRenderer());
	table.setEnabled(false);
	return table;
    }

    private JTable createTrueEnabledTable(Object[][] data,
	    String[] columnNames, DataObjects dataSet,
	    ArrayList<FieldElement> fields) {
	JTable table = new JTable(data, columnNames) {
	    @Override
	    public TableCellEditor getCellEditor(int row, int column) {
		Object value = super.getValueAt(row, column);
		if (value != null) {
		    if (value instanceof JComboBox) {
			return new DefaultCellEditor((JComboBox) value);
		    }
		    return getDefaultEditor(value.getClass());
		}
		return super.getCellEditor(row, column);
	    }
	};

	table.getModel().addTableModelListener(new TableModelListener() {

	    @Override
	    public void tableChanged(TableModelEvent e) {
		// grab the changes
		int col = e.getColumn();
		int row = e.getLastRow();
		String value = (String) table.getValueAt(row, col);
		FieldElement temp = fields.get(row);
		temp.setValue(value);
		// update the current dataSet
		model.setCurrentDataset(dataSet);
	    }
	});

	table.getColumnModel().getColumn(1)
		.setCellRenderer(new DefaultTableCellRenderer());
	return table;

    }
}
