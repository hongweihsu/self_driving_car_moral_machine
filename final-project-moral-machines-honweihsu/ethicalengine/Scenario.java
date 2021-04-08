/*
 *  File: Scenario.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */

package ethicalengine;

/**
 * Represents a scenario to decide on
 * A Scenario consist of passengers, pedestrians and legality
 * @author HongWei-HSU
 */
public class Scenario {

    public static final String NEWLINE = "\n";

    private Persona[] passengers;
    private Persona[] pedestrians;
    private boolean isLegalCrossing;
    private boolean isYouInCar = false;
    private boolean isYouInLane = false;

    public Scenario(Persona[] passengers, Persona[] pedestrians, boolean isLegalCrossing) {
        this.passengers = passengers;
        this.pedestrians = pedestrians;
        this.isLegalCrossing = isLegalCrossing;
    }

    //getter

    /**
     * check whether user in car
     * @return Boolean: user in car (or not)
     */
    public boolean hasYouInCar(){
        return this.isYouInCar;
    }

    /**
     * check whether user in lane
     * @return Boolean: user in lane (or not)
     */
    public boolean hasYouInLane(){
        return this.isYouInLane;
    }

    /**
     * get passengers list from a scenario
     * @return Persona[]: array of passengers
     */
    public Persona[] getPassengers(){
        return this.passengers;
    }
    /**
     * get pedestrians list from a scenario
     * @return Persona[]: array of pedestrians
     */
    public Persona[] getPedestrians(){
        return this.pedestrians;
    }

    /**
     * check whether pedestrian cross the road legally
     * @return Boolean: is legal (or not)
     */
    public boolean isLegalCrossing(){
        return this.isLegalCrossing;
    }

    /**
     * get total numbers of passengers.
     * @return int: amount of passengers.
     */
    public int getPassengerCount(){

        return this.passengers.length;
    }
    /**
     * get total numbers of pedestrians.
     * @return int: amount of pedestrians.
     */
    public int getPedestrianCount(){

        return this.pedestrians.length;
    }

    //setter

    /**
     * set the scenario with legal (or not) pedestrians
     * @param isLegalCrossing: legal (or not)
     */
    public void setLegalCrossing(boolean isLegalCrossing){
        this.isLegalCrossing = isLegalCrossing;
    }

    /**
     * assign passengers by taking an Persona array
     * @param passengers: array filled with human and animal
     */
    public void setPassengers(Persona[] passengers){
        this.passengers = passengers;
    }

    /**
     * assign pedestrians by taking an Persona array
     * @param pedestrians: array filled with human and animal
     */
    public void setPedestrians(Persona[] pedestrians){
        this.pedestrians = pedestrians;
    }

    // toString methods

    /**
     * print out scenario contents
     * @return String: a description of the scenario
     */

    public String toString(){

        String output = "======================================" + NEWLINE;
        output += "# Scenario" + NEWLINE;
        output += "======================================" + NEWLINE;
        output += "Legal Crossing: ";
        if(isLegalCrossing){
            output += "yes"+ NEWLINE;
        }else {
            output += "no"+ NEWLINE;
        }
        output += "Passengers ("+ this.getPassengerCount()+ ")" + NEWLINE;
        for (int i=0; i<this.passengers.length; i++){
            output += "- " + this.passengers[i].toString() + NEWLINE;
        }
        output += "Pedestrians ("+ this.getPedestrianCount() + ")" + NEWLINE;
        for (int i=0; i<this.pedestrians.length; i++){
            if(i == this.pedestrians.length-1){
                output += "- " + this.pedestrians[i].toString().toLowerCase();
            }else {
                output += "- " + this.pedestrians[i].toString().toLowerCase() + NEWLINE;
            }
        }

        return output;
    }
}
