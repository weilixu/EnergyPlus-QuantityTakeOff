package analyzer.distributions;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * This is a subclass of normal distribution class. The class performs a
 * truncated normal distribution based on user inputs
 * 
 * This class only takes mean, standard deviation, lower truncation and higher
 * truncations to initialize
 * 
 * @author weilix
 *
 */
public class TruncatedNormalDistribution extends NormalDistribution implements TruncatedDistribution{

    private double lower;
    private double higher;

    public TruncatedNormalDistribution(double m, double std, double l, double h) {
	super(m, std);
	lower = l;
	higher = h;
    }

    @Override
    public double truncatedSample() {
	double rnd = sample();
	while (rnd < lower || rnd > higher) {
	    rnd = sample();
	}
	return rnd;
    }
    
    @Override
    public double[] truncatedSample(int num){
	double[] samples = new double[num];
	for(int i=0; i<num; i++){
	    samples[i]=truncatedSample();
	}
	return samples;
    }
    
    public double[] lhs(int num){
	double[] data = new double[num];
	double segmentSize = 1.0/num;
	double segmentMin = segmentSize;
	double pointValue = lhSample(segmentMin, segmentSize);
	int counter = 0;
	int locker = 0;
	while(counter<num){
	    //if the sample is not within the requirement, resample
	    //or if stalling for 100 iteration, then give up this segment
	    if(pointValue<lower||pointValue>higher||locker<100){
		pointValue = lhSample(segmentMin, segmentSize);
		locker++;
	    }else{
		//record the sample
		data[counter] = pointValue;
		segmentMin = counter*segmentSize;//move to next segment
		pointValue = lhSample(segmentMin,segmentSize);//sample again
		counter++;//increase the counter
		locker = 0;//reset the stalling indicator
	    }
	}
//	for(int i =0; i<num; i++){
//	    double segmentMin = i*segmentSize;
//	    //double segmentMax = (i+1)*segmentSize;
//	    double pointValue = lhSample(segmentMin, segmentSize);
//	    int counter = 0;
//	    while(pointValue<lower||pointValue>higher||counter<100){
//		//System.out.println(pointValue);
//		pointValue = lhSample(segmentMin, segmentSize);
//		counter++;
//	    }
//	    data[i] = pointValue;
//	}
	return data;
    }
    
    private double lhSample(double min, double size){
	    double point = min+(Math.random()*size);
	    //System.out.println(point);
	    return inverseCumulativeProbability(point);
    }
    
    public static void main(String[] args){
	TruncatedNormalDistribution test = new TruncatedNormalDistribution(0,1,-3,3);
	double[] data = test.lhs(100);
	double[] monteCarlo = test.truncatedSample(100);
	try{
	    FileWriter writer = new FileWriter("C:\\Users\\Weili\\Desktop\\New folder\\tests.csv");
	    for(int i=0; i<data.length; i++){
		writer.append(data[i]+"");
		writer.append(",");
		writer.append(monteCarlo[i]+"");
		writer.append("\n");
	    }
	    writer.flush();
	    writer.close();
	}catch(IOException e){
	    e.printStackTrace();
	}
    }
}
