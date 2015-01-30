package analyzer.listeners;

/**
 * GUI listeners that interacts with Model for reading the generated image
 * 
 * @author Weili
 *
 */

public interface DistGenerationListeners {

    /**
     * The variable name is an identification of this DistGenerationListenrs.
     * The <link>Model<link> calls the listener and identify its variable before
     * plot the image
     * 
     * @return
     */
    public String getVariable();

    /**
     * Load the image from a specific directory. The <link>Model<link> generate
     * the image and provide its directory to the listener, listener will then
     * plot the image on the display panel
     * 
     * @param imageDir
     */
    public void loadDistImage(String imageDir);
}
