package analyzer.distributions;

public class DistributionFactory {
    private DistributionType distType;
    
    //the parameter array has to follow strict rules:
    //the last two elements have to be lower truncation value, higher truncation value
    //the first element must be not null value. The second element can be null or double    
    private final int firstIndex = 0;
    private final int secondIndex = 1;
    private final int lowerIndex = 2;
    private final int higherIndex = 3;

    public DistributionFactory() {
	// this class allows to instantiate this object without specify the
	// distribution type
    }
    
    //constructor that only defines the distribution type
    public DistributionFactory(DistributionType type) {
	this.distType = type;
    }
    
    /**
     * get the distribution type that is current defined in the factory
     * @return
     */
    public DistributionType getDistributionType(){
	return distType;
    }
    
    /**
     * Set the distribution type in this factory
     * @param type
     */
    public void setDistributionType(DistributionType type){
	distType = type;
    }
    
    public TruncatedDistribution getDistribution(double[] parameters){
	double firstParam = parameters[firstIndex];
	double secondParam = parameters[secondIndex];
	double lowerParam = parameters[lowerIndex];
	double higherParam = parameters[higherIndex];
	
	switch(distType){
	case BETA:
	    return new TruncatedBetaDistribution(firstParam, secondParam, lowerParam, higherParam);
	case BINOM:
	    return new TruncatedBinomialDistribution(firstParam, secondParam, lowerParam, higherParam);
	case GAMMA:
	    return new TruncatedGammaDistribution(firstParam, secondParam, lowerParam, higherParam);
	case LOGISTIC:
	    return new TruncatedLogisticDistribution(firstParam, secondParam, lowerParam, higherParam);
	case LOGNORMAL:
	    return new TruncatedLogNormalDistribution(firstParam, secondParam, lowerParam, higherParam);
	case NAKAGAMI:
	    return new TruncatedNakagamiDistribution(firstParam, secondParam, lowerParam, higherParam);
	case NORMAL:
	    return new TruncatedNormalDistribution(firstParam, secondParam, lowerParam, higherParam);
	case POISSON:
	    return new TruncatedPoissonDistribution(firstParam, lowerParam, higherParam);
	case UNIFORMC:
	    return new TruncatedUniformRealDistribution(firstParam, secondParam);
	case UNIFORMD:
	    return new TruncatedUniformIntegerDistribution(firstParam, secondParam);
	case WEIBULL:
	    return new TruncatedWeibullDistribution(firstParam, secondParam, lowerParam, higherParam);
	case EXPON:
	    return new TruncatedExponentialDistribution(firstParam, lowerParam, higherParam);
	}
	//hopefully we won't get to this step (technically we will never came to this line)
	return null;
    }
    
    

}
