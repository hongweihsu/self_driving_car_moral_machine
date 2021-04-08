/*
 *  File: InvalidCharacteristicException.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */
public class InvalidCharacteristicException extends Exception {
    /**
     * Represent error when input data has parameters not contained in this program
     * @author HongWei-HSU
     */
    private final int lineCount;

    public InvalidCharacteristicException(int lineCount){
        super();
        this.lineCount = lineCount;
    }

    public String getMessage(){
        return ("\"WARNING: invalid characteristic format in config file in line" +
                this.lineCount + "\"");
    }

}



