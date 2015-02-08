package analyzer.recommender;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

public class Recommender {

    private final SAXBuilder builder;
    private final File recommendation;
    private Document document;
    private DefaultMutableTreeNode node;

    private final String FILE_NAME = "recommender.xml";

    public Recommender() {
	builder = new SAXBuilder();
	recommendation = new File(FILE_NAME);
	node = new DefaultMutableTreeNode("Recommender");

	try {
	    document = (Document) builder.build(recommendation);
	} catch (Exception e) {
	    // do nothing
	}
	recommenderBuilder();
    }

    /**
     * Building the XML tree for the display purpose
     */
    private void recommenderBuilder() {
	Element root = document.getRootElement();
	builderHelper(root, node);
    }

    /**
     * helper method to build the tree. Note that when one child from a parents
     * carries text, this method will assume all the children under this parent
     * carry text information. This should be part of the XML schema
     * 
     * @param current
     * @param root
     */
    private void builderHelper(Element current, DefaultMutableTreeNode root) {
	List<Element> children = current.getChildren();
	Iterator<Element> iterator = children.iterator();
	DefaultMutableTreeNode leafNode = new DefaultMutableTreeNode();
	DistributionInfo end = new DistributionInfo();
	while (iterator.hasNext()) {
	    Element child = iterator.next();
	    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
	    if (child.hasAttributes() && child.getParent().getParent() != null) {
		System.out.println(child.getName());
		// for condition where the node has attributes
		String field = child.getAttributeValue("field");
		String unit = child.getAttributeValue("unit");
		String reference = child.getAttributeValue("reference");
		ObjectProperty op = new ObjectProperty(field, unit, reference);
		childNode.setUserObject(op);
		root.add(childNode);
		builderHelper(child, childNode);
	    } else if (child.getChildren().isEmpty()) {
		// for condition where the nodes carries text
		end.addString(child.getName() + " : " + child.getText());
	    } else if (child.getParent().getParent() == null) {
		String object = child.getAttributeValue("object");
		CategoryProperty cp = new CategoryProperty(object);
		childNode.setUserObject(cp);
		root.add(childNode);
		builderHelper(child,childNode);
	    } else {
		// for condition where the nodes is only contains name
		childNode.setUserObject(child.getName());
		root.add(childNode);
		builderHelper(child, childNode);
	    }
	}
	// if this is leaf nodes, then we need to wait for the loop completed
	// and then add it to the parent node
	if (end.isFilled()) {
	    leafNode.setUserObject(end);
	    root.add(leafNode);
	}
    }

    /**
     * getter method to retrieve the complete tree
     * 
     * @return
     */
    public DefaultMutableTreeNode getTree() {
	return node;
    }

    /**
     * retrieve a partial tree at object information level (2nd level)
     * 
     * @param object
     * @param info
     * @return
     */
    public DefaultMutableTreeNode getPartialTree(String object, String field) {
	int count = node.getChildCount();
	System.out.println(count);
	for (int i = 0; i < count; i++) {
	    DefaultMutableTreeNode child = (DefaultMutableTreeNode) node
		    .getChildAt(i);
	    CategoryProperty cp = (CategoryProperty) child.getUserObject();
	    //find the first level item
	    if (cp.getObject().equals(object)) {
		int grandChildCount = child.getChildCount();
		for(int j=0; j<grandChildCount; j++){
		    DefaultMutableTreeNode grandChild = (DefaultMutableTreeNode) child
			    .getChildAt(i);
		    ObjectProperty op = (ObjectProperty) grandChild.getUserObject();
		    //find the correct field
		    if(op.getField().equals(field)){
			return grandChild;
		    }
		}
	    }
	}
	return null;
    }
}
