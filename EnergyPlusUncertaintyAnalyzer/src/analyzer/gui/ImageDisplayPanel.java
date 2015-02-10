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
    private final JLabel imageLabel;
    
    public ImageDisplayPanel(Model m, String v){
	setBackground(Color.WHITE);
	model = m;
	variable  = v;
	imageLabel = new JLabel();
	add(imageLabel);
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
	imageLabel.removeAll();
	imageLabel.setIcon(imageIcon);
    }

    @Override
    public String getVariable() {
	return variable;
    }

}
