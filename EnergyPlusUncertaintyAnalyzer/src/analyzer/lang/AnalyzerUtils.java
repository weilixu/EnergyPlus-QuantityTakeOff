package analyzer.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration utility class used to load system properties and parse command
 * line arguments.
 * 
 * @author Weili
 *
 */
public final class AnalyzerUtils {
    private static final String CONFIG_PROPERTIES = "eplusconfig.properties";
    private static final String EPLUS_DIR = "eplusconfig.directory";
    private static final String EPLUS_WEA = "eplusconfig.weather";
    private static final String EPLUS_PROC = "eplusconfig.processors";
    
    
    private static String eplusDir = null;
    private static String eplusWea = null;
    private static String eplusProc = null;
    
    private static Properties eplusProps;
    
    static{
	initProperties();
    }
    
    private static void initProperties(){
	String tempDir = eplusDir;
	String tempWea = eplusWea;
	String tempProc = eplusProc;
	try{
	    eplusProps = new Properties();
	    FileInputStream eplusIn = new FileInputStream(CONFIG_PROPERTIES);
	    
	    eplusProps.load(eplusIn);
	    eplusIn.close();
	    	    
	    eplusDir = eplusProps.getProperty(EPLUS_DIR);
	    eplusWea = eplusProps.getProperty(EPLUS_WEA);
	    eplusProc = eplusProps.getProperty(EPLUS_PROC);
	    
	}catch(Exception e){
	    eplusDir = tempDir;
	    eplusWea = tempWea;
	    eplusProc = tempProc;
	}
    }
    
    public static String[] getEplusConfig(){
	String[] config = {eplusDir, eplusWea, eplusProc};
	return config;
    }
    
    public static void setEplusDirectory(String dir){
	eplusProps.setProperty(EPLUS_DIR, dir);
    }
    
    public static void setEplusWeather(String wea){
	eplusProps.setProperty(EPLUS_WEA, wea);
    }
    
    public static void setEplusProcessor(String proc){
	eplusProps.setProperty(EPLUS_PROC, proc);
    }
    
    public static void writeProperties(){
	File file = new File("eplusconfig.properties");
	FileOutputStream fOut;
	try {
	    fOut = new FileOutputStream(file);
	    eplusProps.store(fOut, "eplus configuration");
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e){
	    e.printStackTrace();
	}
    }

}
