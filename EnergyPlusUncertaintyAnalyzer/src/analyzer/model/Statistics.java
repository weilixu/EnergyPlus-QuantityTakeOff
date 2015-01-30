package analyzer.model;

public class Statistics {
	public double calculateMean(double[] args){
		double sum = 0;
		for (int i=0; i<args.length; i++){
			sum = sum + args[i];
		}
		return sum/args.length;
	}
	

}
