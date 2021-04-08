/*
 *  File: InvalidInputException.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */
public class InvalidInputException extends Exception {

    /**
     * Represent input message for consent agreement can not be accepted
     * @author HongWei-HSU
     */
    public InvalidInputException(){
        super();
    }

    public String getMessage(){
        return ("Invalid response. Do you consent to have your decisions saved to a file? (yes/no)");
    }
}