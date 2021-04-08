/*
 *  File: InvalidDataFormatException.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */
public class InvalidDataFormatException extends Exception{
    /**
     * Error by data type mismatch.
     * @author HongWei-HSU
     */
    private final int lineCount;

    public InvalidDataFormatException(int lineCount){
        super();
        this.lineCount = lineCount;
    }

    public String getMessage(){
        return ("\"WARNING: invalid data format in config file in line" +
                this.lineCount + "\"");
    }

}

