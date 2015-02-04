package analyzer.recommender;

import java.io.File;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;


public class Recommender {
    
    private final SAXBuilder builder;
    private final File recommendation;
    private Document document;
    
    private final String FILE_NAME= "recommender.xml";
    
    public Recommender(){
	builder = new SAXBuilder();
	recommendation = new File(FILE_NAME);
	
	try{
	    document = (Document) builder.build(recommendation);
	}catch(Exception e){
	    //do nothing
	}
    }

}
