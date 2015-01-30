package analyzer.lang;

public class AnalyzerException extends Exception {

    /** System dependent line separator */
    private final static String LS = System.getProperty("line.separator");

    /** number of errors */
    private int numErr;

    /** error message */
    private String errMes;

    /**
     * Constructs a OptimizerException Object with no detail message.<d> The
     * error counter is not set to <B>0</B> (which means no error occured.
     */
    public AnalyzerException() {
	super();
	numErr = 0;
	errMes = new String("");
    }

    /**
     * Constructs a OptimizerException with the specified detail message.<d> The
     * error counter is set to <B>1</B>.
     * 
     * @param s
     *            the detail message
     */
    public AnalyzerException(String s) {
	super(s);
	numErr++;
	errMes = new String(s);
    }

    /**
     * sets another Throwable
     * 
     * @param t
     *            the Throwable
     */
    public void setThrowable(Throwable t) {
	numErr++;
	String em = new String(t.getClass().getName() + ": " + t.getMessage());
	append(em);
	return;
    }

    /**
     * sets an error
     * 
     * @param errorMessage
     *            the error message
     */
    public void setMessage(String errorMessage) {
	numErr++;
	append(errorMessage);
	return;
    }

    /**
     * appends an error message
     * 
     * @param errorMessage
     *            the error message
     */
    private void append(String errorMessage) {
	if (numErr == 1)
	    errMes = new String(errorMessage);
	else
	    errMes = new String(errMes + LS + errorMessage);
    }

    /**
     * gets all error messages
     * 
     * @return the error messages
     */
    public String getMessage() {
	return new String(errMes);
    }

    /**
     * gets the number of error
     * 
     * @return the number of errors
     */
    public int getNumberOfErrors() {
	return numErr;
    }

}
