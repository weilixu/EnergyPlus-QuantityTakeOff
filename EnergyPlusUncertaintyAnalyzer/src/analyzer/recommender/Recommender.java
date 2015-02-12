package analyzer.recommender;

import java.io.File;
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
	    e.printStackTrace();
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
	// DefaultMutableTreeNode leafNode = new DefaultMutableTreeNode();
	// DistributionInfo end = new DistributionInfo();
	while (iterator.hasNext()) {
	    Element firstLevel = iterator.next();
	    DefaultMutableTreeNode firstNode = new DefaultMutableTreeNode();

	    ObjectProperty op = new ObjectProperty(
		    firstLevel.getAttributeValue("object"));
	    firstNode.setUserObject(op);
	    root.add(firstNode);
	    buildSecondLevel(firstLevel, firstNode);
	}
    }
    
    /**
     * build as the second level of the XML data structure
     * @param current
     * @param root
     */
    private void buildSecondLevel(Element current, DefaultMutableTreeNode root) {
	List<Element> children = current.getChildren();
	Iterator<Element> iterator = children.iterator();
	while (iterator.hasNext()) {
	    Element secondLevel = iterator.next();
	    DefaultMutableTreeNode secondNode = new DefaultMutableTreeNode();

	    String field = secondLevel.getAttributeValue("field");
	    String unit = secondLevel.getAttributeValue("unit");
	    String notes = secondLevel.getAttributeValue("notes");

	    FieldProperty fp = new FieldProperty(field, unit, notes);
	    secondNode.setUserObject(fp);
	    root.add(secondNode);
	    buildOthers(secondLevel, secondNode);
	}
    }
    
    /**
     * Build other levels of the XML data structure
     * @param current
     * @param root
     */
    private void buildOthers(Element current, DefaultMutableTreeNode root) {
	List<Element> children = current.getChildren();
	Iterator<Element> iterator = children.iterator();
	while (iterator.hasNext()) {
	    Element otherLevel = iterator.next();
	    //System.out.println(otherLevel.getName());
	    DefaultMutableTreeNode otherNode = new DefaultMutableTreeNode();
	    // passing the middle node, has only description
	    if (otherLevel.getAttributes().size() == 1) {
		String temp = otherLevel.getAttributeValue("description");
		otherNode.setUserObject(temp);
		root.add(otherNode);
		buildOthers(otherLevel,otherNode);
		// find the leaf node (not include distribution...)
	    } else if (otherLevel.getAttributes().size() == 3) {

		String description = otherLevel
			.getAttributeValue("description");
		String reference = otherLevel.getAttributeValue("reference");
		String notes = otherLevel.getAttributeValue("notes");
		String distributionName = otherLevel
			.getChildText("distributionName");
		String distributionParam = otherLevel
			.getChildText("distributionParameters");
		String min = otherLevel.getChildText("minimum");
		String max = otherLevel.getChildText("maximum");
		
		LeafProperty lp = new LeafProperty(description, reference,
			notes, distributionName, distributionParam, min, max);
		otherNode.setUserObject(lp);
		root.add(otherNode);
		//thats it, end of the building
	    }
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
	DefaultMutableTreeNode root = new DefaultMutableTreeNode();
	
	int count = node.getChildCount();
	for (int i = 0; i < count; i++) {
	    DefaultMutableTreeNode child = (DefaultMutableTreeNode) node
		    .getChildAt(i);
	    ObjectProperty cp = (ObjectProperty) child.getUserObject();
	    // find the first level item
	    if(cp.getObject().equals(object)){
		root.setUserObject(cp);
		root.add(findChildren(child, field));
	    }
	}
	if(root.getUserObject()==null){
	    return node;
	}
	return root;
    }
    
    private DefaultMutableTreeNode findChildren(DefaultMutableTreeNode child,String field){
	DefaultMutableTreeNode children = new DefaultMutableTreeNode();
	
	int count = child.getChildCount();
	for(int i=0; i<count; i++){
	    DefaultMutableTreeNode temp = (DefaultMutableTreeNode) child
		    .getChildAt(i);
	    FieldProperty fp = (FieldProperty) temp.getUserObject();
	    if(fp.getField().equals(field)){
		children.setUserObject(fp);
		while(temp.getChildCount()>0){
		    DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) temp.getChildAt(0);
		    children.add(tempNode);
		}
	    }
	}
	return children;
    }
}
