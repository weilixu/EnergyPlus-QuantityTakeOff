package analyzer.distributions;

public enum DistributionType {
    BETA("Beta Distribution"),
    BINOM("Binomial Distribution"),
    GAMMA("Gamma Distribution"),
    LOGISTIC("Logistic Distribution"),
    LOGNORMAL("Lognormal Distribution"),
    NAKAGAMI("Nakagami Distribution"),
    NORMAL("Normal Distribution"),
    POISSON("Poisson Distribution"),
    UNIFORMC("Uniform Continuous Distribution"),
    UNIFORMD("Uniform Discrete Distribution"),
    WEIBULL("Weibull Distribution"),
    EXPON("Exponential Distribution");
    
    private String type;
    
    private DistributionType(String type){
	this.type = type;
    }
    
    @Override
    public String toString(){
	return type;
    }
}
