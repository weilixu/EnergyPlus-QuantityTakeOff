package analyzer.eplus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import analyzer.htmlparser.EnergyPlusHTMLParser;
import analyzer.lang.AnalyzerUtils;
import jmetal.core.Variable;
import jmetal.util.JMException;

public class RunEnergyPlusOptimization {
    private static final String EPLUSBAT = "RunEplus.bat";

    private File folder;
    private File eplusFolder;
    private Integer simulationCount;


    //private EnergyPlusHTMLParser parser;
    private final IdfReader idfData;
    private File weaFile;

    private String energyplus_dir;
    private String weather_dir;

    public RunEnergyPlusOptimization(IdfReader reader) {
	idfData = reader;

	String[] config = AnalyzerUtils.getEplusConfig();
	energyplus_dir = config[0];
	weather_dir = config[1];

    }

    public void setSimulationTime(Integer simulationTime) {
	simulationCount = simulationTime;
    }

    public void setFolder(File f) {
	folder = f;
    }

    public EnergyPlusHTMLParser runSimulation() throws IOException, JMException {
	//create the energyplus folder
	eplusFolder = new File(folder.getAbsolutePath() + "\\"
		+ simulationCount.toString());
	eplusFolder.mkdir();
	//create a copy of weather file
	File weatherFile = new File(energyplus_dir + "WeatherData\\"
		+ weather_dir + ".epw");
	weaFile = copyWeatherFile(weatherFile);
	//create a batch file to run the simulation
	File eplusBatFile = createBatchFile();
	//make it in a folder
	File energyPlusFile = new File(folder.getAbsolutePath() + "\\"
		+ simulationCount.toString()+"\\"+simulationCount.toString()+".idf");
	System.out.println(energyPlusFile.getAbsolutePath());
	//commandline: eplus batch file directory energyplus file (without postfix) and weather file name (without postfix)
	String[] commandline = {
		eplusBatFile.getAbsolutePath(),
		energyPlusFile.getAbsolutePath().substring(0,
			energyPlusFile.getAbsolutePath().indexOf(".")),
		"weather"};

	try {
	    //run the simulation
	    Process p = Runtime.getRuntime().exec(commandline, null, eplusFolder);
	    ThreadedInputStream errStr = new ThreadedInputStream(
		    p.getErrorStream());
	    errStr.start();
	    ThreadedInputStream outStr = new ThreadedInputStream(
		    p.getInputStream());
	    outStr.start();
	    p.waitFor();

	    errStr.join();
	    outStr.join();
	    //finished simulation, delete the file
	    eplusBatFile.delete();

	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	EnergyPlusHTMLParser parser = null;
	// get the output from HTML
	File[] fileList = eplusFolder.listFiles();
	for (File f : fileList) {
	    if (f.getAbsolutePath().contains(".html")) {
		parser = new EnergyPlusHTMLParser(f);
	    }
	}
	//return the EUI (fitness value)
	return parser;
    }
    
    /*
     * copy weather file into a directory
     */
    private File copyWeatherFile(File weatherFile) throws IOException {
	BufferedReader br = new BufferedReader(
		new FileReader(
			weatherFile));
	StringBuilder sb = new StringBuilder();
	File file = new File(eplusFolder.getAbsolutePath() + "\\" + "weather.epw");

	try {
	    String line = br.readLine();

	    while (line != null) {
		sb.append(line);
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
    
    /*
     * Create a batch file in the directory
     */
    private File createBatchFile() throws IOException {
	String keyWord = "set program_path=";
	String weaWord = "set weather_path=";
	File file = new File(eplusFolder.getAbsolutePath() + "\\" + EPLUSBAT);
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
		}else if (line.contains(weaWord)) {
		    sb.append(weaWord);
		    sb.append(eplusFolder.getAbsolutePath() + "\\");
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

    class ThreadedInputStream extends Thread {
	protected IOException ioExc;
	protected InputStream is;
	protected StringBuffer sb = null;

	public ThreadedInputStream(InputStream inputStream) {
	    is = inputStream;
	    sb = new java.lang.StringBuffer();
	    ioExc = null;
	}

	public void run() {
	    try {
		byte[] by = new byte[1];
		while (this != null) {
		    int ch = is.read(by);
		    if (ch != -1) { // -1 indicates the end of the stream
			sb.append((char) by[0]);
		    } else {
			break;
		    }
		}
		is.close();
	    } catch (IOException e) {
		ioExc = e;
	    }
	}

	public void throwStoredException() throws IOException {
	    if (ioExc != null) {
		throw ioExc;
	    }
	}

	public String getInputStream() {
	    return new String(sb.toString());
	}
    }

}
