package analyzer.distributions;

public enum DistributionType {
    BETA("Beta Distribution","Continuous"),
    BINOM("Binomial Distribution","Discrete"),
    GAMMA("Gamma Distribution","Continuous"),
    LOGISTIC("Logistic Distribution","Continuous"),
    LOGNORMAL("Lognormal Distribution","Continuous"),
    NAKAGAMI("Nakagami Distribution","Continuous"),
    NORMAL("Normal Distribution","Continuous"),
    POISSON("Poisson Distribution","Discrete"),
    UNIFORMC("Uniform Distribution","Continuous"),
    UNIFORMD("Uniform Distribution","Discrete"),
    WEIBULL("Weibull Distribution","Continuous"),
    TRIANGULAR("Triangular Distribution","Continuous"),
    EXPON("Exponential Distribution","Continuous");
    
    private String type;
    private String category;
    
    private DistributionType(String type,String cat){
	this.type = type;
	this.category = cat;
    }
    
    @Override
    public String toString(){
	return type+" ("+category+")";
    }
}
