/*
 *  File: Attribute.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */
import ethicalengine.Scenario;

/**
 * track appearance times of an attribute after audited scenarios
 * also track attributes from survivors
 * calculate survival ratio of each attribute in this program
 * @author HongWei-HSU
 */
public class Attribute {

    private String name;
    private int count=0; //denominator of survival ratio
    private int survivalCount=0; // fraction of survival ratio
    private double finalRatio=0;

    //constructor
    public Attribute(String name){
        this.name = name;
    }

    //setter
    public void setName(String name){
        this.name = name;
    }

    public void setTotalCount(int totalCount){
        this.count = totalCount;
    }

    public void setSurvivalCount(int survivalCount){
        this.survivalCount = survivalCount;
    }

    public void setFinalRatio(float finalRatio){
        this.finalRatio = finalRatio;
    }

    //getter
    public String getName(){
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getSurvivalCount() {
        return survivalCount;
    }

    public String getFinalRatio() {
        return String.format("%.2f", finalRatio);
    }

    //updater
    public void countUpdate(){
        this.count += 1;
    }

    public void survivalCountUpdate(){
        this.survivalCount += 1;
    }

    //passenger and pedestrian
    public void passengerCount(Scenario scenario){
        count += scenario.getPassengerCount();
    }

    public void pedestrianCount(Scenario scenario){
        count += scenario.getPedestrianCount();
    }

    public void passengerSurvivalCount(Scenario scenario){
        survivalCount += scenario.getPassengerCount();
    }

    public void pedestrianSurvivalCount(Scenario scenario){
        survivalCount += scenario.getPedestrianCount();
    }

    //green and red (legality)
    public void totalPersonaUpdate(Scenario scenario){
        count += scenario.getPassengerCount();
        count += scenario.getPedestrianCount();
    }

    public void totalSurvivalUpdate(Scenario scenario, EthicalEngine.Decision decision){
        if (decision == EthicalEngine.Decision.PASSENGERS) {//when passengers survive
            survivalCount += scenario.getPassengerCount();
        } else {//when pedestrians survive
            survivalCount += scenario.getPedestrianCount();
        }
    }

    //ratio calculator
    public void calculateRatio(){
        this.finalRatio =  Math.ceil((double) this.survivalCount / this.count * 100.0)/100.0;
    }

}
