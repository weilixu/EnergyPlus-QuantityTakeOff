package analyzer.model;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class GenerateStatistics {

	private final double[] data;
	private final SummaryStatistics stats;
	private final double mean;

	public GenerateStatistics(double[] args) {
		this.data = args;
		stats = new SummaryStatistics();
		for (int i = 0; i < data.length; i++) {
			stats.addValue(data[i]);
		}
		mean = stats.getMean();
	}

	public double getMean() {
		return mean;
	}

	public double[] getCI(double level) {
		double[] ci = new double[2];
		try {
			TDistribution tDist = new TDistribution(
					stats.getN() - 1);
			double tScore = tDist
					.inverseCumulativeProbability(1.0 - (1.0 - level) / 2);
			double interval = tScore * stats.getStandardDeviation()
					/ Math.sqrt(stats.getN());
			ci[0] = mean - interval;
			ci[1] = mean + interval;
		} catch (MathIllegalArgumentException e) {
			System.err.println("invalid values in calculating CI");
		}

		return ci;
	}

}
