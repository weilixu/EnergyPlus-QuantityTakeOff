package analyzer.model;

import java.util.Arrays;
import java.util.Random;

import allfitdist.FitDist;

import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class RVGenerator {

    // Strings for the file names
    private final String DIST_NAME = "DIST_";
    private final String IMAGE_POST = ".jpg";

    private String distSummary;
    private String variableName;

    public RVGenerator() {
	distSummary = "";
    }

    public void setVariableName(String variable) {
	variableName = variable;
    }

    /**
     * 
     * @param source
     *            Directory to save image to
     * @param imgName
     *            Name of file to save image to (e.g. "testImage.jpg")
     * @param data
     *            data to fit distribution to
     * @param numRV
     *            number of random variables to generate
     * @param sortby
     *            sortby is the likelihood function used to sort the fitted
     *            probability distribtuions. Valid inputs are: NLogL - Negative
     *            Log Likelihood BIC - Bayesian Information criterion AIC -
     *            Akaike information criterion AICc
     * @param dataType
     *            dataType specifies whether to fit a 'DISCRETE' or 'CONTINUOUS'
     *            distribution
     * @param lower
     *            lower bound where the generated random variables will be
     *            truncated tor
     * @param upper
     *            upper bound where the generated random variables will be
     *            truncated tor
     * @return
     */
    public double[] fitData(String path, double[] data, Double simNumber,
	    String sortby, String dataType, String lower, String upper) {
	FitDist fitDistr = null;
	Object[] fitDistInputs = new Object[8];
	fitDistInputs[0] = path;
	fitDistInputs[1] = DIST_NAME + variableName + IMAGE_POST;
	fitDistInputs[2] = data;
	fitDistInputs[3] = simNumber;
	fitDistInputs[4] = sortby;
	fitDistInputs[5] = dataType;
	fitDistInputs[6] = Double.parseDouble(lower);
	fitDistInputs[7] = Double.parseDouble(upper);

	System.out.println(path + " " + fitDistInputs[1] + " " + simNumber
		+ " " + sortby + " " + dataType + " " + fitDistInputs[6] + " "
		+ fitDistInputs[7]);

	Object[] fitDistResult = null;
	try {
	    fitDistr = new FitDist();
	    fitDistResult = fitDistr.allfitdist(2, fitDistInputs);
	} catch (MWException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	MWNumericArray rndVars = (MWNumericArray) fitDistResult[0];

	distSummary = fitDistResult[1].toString(); // convert to String
	editDistSummary();

	return rndVars.getDoubleData();
    }

    public String getDistSummary() {
	return distSummary;
    }

    private void editDistSummary() {
	String[] distString = null;
	if (distSummary != null) {
	    distString = distSummary.split("   ");
	}

	StringBuffer temp = new StringBuffer();
	for (int i = 0; i < distString.length; i++) {
	    if (!distString[i].isEmpty()) {
		temp.append(distString[i].trim());
		temp.append("\n");
	    }
	}
	distSummary = temp.toString();
    }

//    public static void main(String[] args) {
//	RVGenerator generator = new RVGenerator();
//
//	Random randomNormal = new Random();
//	int n = 1000;
//	double[] data = new double[n];
//	for (int i = 0; i < n; i++) {
//	    data[i] = randomNormal.nextGaussian();
//	}
//
//	generator.setVariableName("test");
//	double[] results = generator.fitData(
//		"C:\\Users\\Weili\\Desktop\\New folder", data, 50.0, "BIC",
//		"CONTINUOUS", "-20.0", "20.0");
//	System.out.println(Arrays.toString(results));
//
//    }

}
