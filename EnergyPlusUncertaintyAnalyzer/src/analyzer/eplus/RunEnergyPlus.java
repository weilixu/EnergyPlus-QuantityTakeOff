package analyzer.eplus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import analyzer.lang.AnalyzerException;

public class RunEnergyPlus {

    private final String EPLUSBAT = "RunDirMulti.bat";
    // folder should be the folder that contains energyplus files
    private File folder;
    private String energyplus_dir;
    private String weather_file;
    private String number_proc;
    
    private boolean directoryEntered = false;

    public RunEnergyPlus(File f) {
	folder = f;
    }

    // setter method to set the energyplus directory. This input must be
    // specified before the simulation
    public void setEnergyPlusDirectory(String ed) {
	energyplus_dir = ed;
	directoryEntered = true;
    }

    // setter method to set the weather file. If not specified, default weather
    // file will be used
    public void setWeatherFile(String wf) {
	weather_file = wf;
    }

    // setter method to set the number of processors, if not specified, default
    // number (4) will be used
    public void setNumberOfProcessor(String np) {
	number_proc = np;
    }

    public void startSimulation() throws IOException, InterruptedException, AnalyzerException  {
	if(!directoryEntered){
	    throw new AnalyzerException("Can not find EnergyPlus File");
	}else{
	    runSimulation();
	}
    }
    
    private void runSimulation()throws IOException, InterruptedException{
	File batchFile = createBatchFile();
	String[] commandline = { batchFile.getAbsolutePath(), weather_file,
		number_proc };
	try {
	    Process p = Runtime.getRuntime().exec(commandline, null, folder);

	    p.waitFor();

	    batchFile.delete();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private File createBatchFile() throws IOException {
	String keyWord = "SET maindir=";
	File file = new File(folder.getAbsoluteFile() + "\\" + EPLUSBAT);
	file.createNewFile();

	// reading file and write to the new file

	BufferedReader br = new BufferedReader(new FileReader(EPLUSBAT));
	StringBuilder sb = new StringBuilder();

	try {
	    String line = br.readLine();

	    while (line != null) {
		if (line.contains(keyWord)) {
		    sb.append(keyWord);
		    sb.append(energyplus_dir);
		} else {
		    sb.append(line);
		}
		sb.append(System.lineSeparator());
		line = br.readLine();
	    }
	} finally {
	    FileWriter results = null;
	    try {
		results = new FileWriter(file, true);
		PrintWriter pw = new PrintWriter(results);
		pw.append(sb.toString());
		pw.close();
	    } catch (IOException e) {
		// some warning??
	    }
	    // close the file
	    br.close();
	}
	return file;
    }
}
