package analyzer.eplus;

public interface EnergyPlusFilesGenerator {
    /**
     * writing the database to a single EnergyPlus idf file
     * 
     * @param fileID
     */
    public String WriteIdf(String path, String fileID);

    /**
     * clone the current idf data
     * 
     * @return
     */
    public EnergyPlusFilesGenerator cloneIdfData();

    /**
     * @require specialCharactor is exist in the data structure, value is valid
     *          for the simulation
     * @ensure replace the special character with the value. This method allows
     *         replacing same variable characters at multiple locations
     * 
     * @param specialCharactor
     * @param value
     */
    public void modifySpecialCharactor(String specialCharactor, String value);

}
