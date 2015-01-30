package analyzer.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import analyzer.listeners.DistGenerationListeners;
import analyzer.model.Model;

/**
 * Indicates the distribution image on the panel. It listens to the <link>Model<link> data generation
 * and retrieve the generated .jpg image.
 * 
 * @author Weili
 *
 */
public class ImageDisplayPanel extends JPanel implements DistGenerationListeners{
    
    private final Model model;
    private final String variable;
    
    public ImageDisplayPanel(Model m, String v){
	setBackground(Color.WHITE);
	model = m;
	variable  = v;
	//register the listener
	m.addDistGeneListeners(this);
    }

    @Override
    public void loadDistImage(String imageDir) {
	BufferedImage image = null;
	try{
	    image = ImageIO.read(new File(imageDir));
	}catch(Exception e){
	    //do something
	    e.printStackTrace();
	}
	ImageIcon imageIcon = new ImageIcon(image);
	JLabel label = new JLabel();
	label.setIcon(imageIcon);
	
	add(label);
    }

    @Override
    public String getVariable() {
	return variable;
    }

}
