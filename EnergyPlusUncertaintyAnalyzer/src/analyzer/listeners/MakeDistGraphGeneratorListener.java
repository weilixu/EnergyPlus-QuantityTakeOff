package analyzer.listeners;

public interface MakeDistGraphGeneratorListener {
    
    public void onUpdatedDistGenerated(double[] distSamples);
    
    public String getVariable();

}
