package analyzer.eplus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import analyzer.htmlparser.EnergyPlusHTMLParser;

public class RunEnergyPlusOptimization {
    private static final String EPLUSBAT = "RunEplus.bat";

    private File folder;

    private String energyplus_dir;
    private String weather_file;
    private File eplusBatFile;
    
    private EnergyPlusHTMLParser parser;

    public RunEnergyPlusOptimization() {

    }

    public void setFolder(File f) {
	folder = f;
    }

    // setter method to set the energyplus directory. This input must be
    // specified before the simulation
    public void setEnergyPlusDirectory(String ed) {
	energyplus_dir = ed;
    }

    // setter method to set the weather file. If not specified, default weather
    // file will be used
    public void setWeatherFile(String wf) {
	weather_file = wf;
    }

    public double runSimulation(String pathToIdf) throws IOException {
	File wea = new File(weather_file);
	File weatherFile = copyWeatherFile(wea);
	String[] commandline = { eplusBatFile.getAbsolutePath(),
		pathToIdf.substring(0, pathToIdf.indexOf(".")),
		weatherFile.getName() };
	
	try {
	    Process p = Runtime.getRuntime().exec(commandline, null, folder);
	    ThreadedInputStream errStr = new ThreadedInputStream(
		    p.getErrorStream());
	    errStr.start();
	    ThreadedInputStream outStr = new ThreadedInputStream(
		    p.getInputStream());
	    outStr.start();
	    p.waitFor();

	    errStr.join();
	    outStr.join();
	    eplusBatFile.delete();

	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	File[] fileList = folder.listFiles();
	for (File f : fileList) {
	    if (f.getAbsolutePath().contains(".html")) {
		parser = new EnergyPlusHTMLParser(f);
		double eui = parser.getEUI();
		return eui;
	    }
	}
	return Double.MAX_VALUE;
    }

    private File copyWeatherFile(File weatherFile) throws IOException {
	BufferedReader br = new BufferedReader(new FileReader("E:\\01_Software\\EnergyPlusV8-2-0\\WeatherData\\USA_PA_Pittsburgh-Allegheny.County.AP.725205_TMY3.epw"));
	StringBuilder sb = new StringBuilder();
	File file = new File(folder.getAbsolutePath() + "\\" + "weather.epw");

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

    public void createBatchFile() throws IOException {
	String keyWord = "SET maindir=";
	File file = new File(folder.getAbsolutePath() + "\\" + EPLUSBAT);
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
	eplusBatFile = file;
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
