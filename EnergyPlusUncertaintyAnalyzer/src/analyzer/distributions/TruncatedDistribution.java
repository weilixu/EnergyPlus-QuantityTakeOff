package analyzer.distributions;

/**
 * An interface that generates truncated distributions The truncated samples
 * implementation is similar.
 * 
 * @author Weili
 *
 */
public interface TruncatedDistribution {

    /**
     * draw one sample from the distribution space by applying the truncated
     * lower/higher limitation
     * 
     * @return
     */
    public double truncatedSample();
    
    /**
     * draw an array of samples that is follows the constraints
     * @param num
     * @return
     */
    public double[] truncatedSample(int num);

}
