/*
 *  File: ScenarioGenerator.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */

package ethicalengine;

import java.util.Date;
import java.util.Random;

/**
 * Used to generate scenario randomly or with specific setting
 * It holds attributes to constrain max and min number of passengers and pedestrians
 * @author HongWei-HSU
 */
public class ScenarioGenerator {

    private int passengerCountMin = 1;
    private int passengerCountMax = 5;
    private int pedestrianCountMin = 1;
    private int pedestrianCountMax = 5;
    Random random = new Random();

    //constructor
    public ScenarioGenerator(){
        long seed = new Date().getTime();
        this.random.setSeed(seed);
    }

    public ScenarioGenerator(long seed){
        this.random.setSeed(seed);
    }

    public ScenarioGenerator(long seed, int passengerCountMinimum, int passengerCountMaximum, int pedestrianCountMinimum, int pedestrianCountMaximum){
        this.random.setSeed(seed);

        if(passengerCountMinimum < 1){
            passengerCountMinimum = 1;
        }

        if(passengerCountMaximum < 2){
            passengerCountMaximum = 2;
        }

        if(passengerCountMaximum < passengerCountMinimum){
            this.passengerCountMin = passengerCountMaximum;
            this.passengerCountMax = passengerCountMinimum;
        }else {
            this.passengerCountMin = passengerCountMinimum;
            this.passengerCountMax = passengerCountMaximum;
        }

        if(pedestrianCountMinimum < 1){
            pedestrianCountMinimum = 1;
        }

        if(pedestrianCountMaximum < 2){
            pedestrianCountMaximum = 2;
        }

        if(pedestrianCountMaximum < pedestrianCountMinimum){
            this.pedestrianCountMin = pedestrianCountMaximum;
            this.pedestrianCountMax = pedestrianCountMinimum;
        }else {
            this.pedestrianCountMin = pedestrianCountMinimum;
            this.pedestrianCountMax = pedestrianCountMaximum;
        }
    }

    //getter and setter

    /**
     * set minimum number of passengers in a scenario.
     * @param min: minimum number of passengers.
     */
    public void setPassengerCountMin(int min){
        this.passengerCountMin = min;
    }

    /**
     * set maximum number of passengers in a scenario.
     * @param max: maximum number of passengers.
     */
    public void setPassengerCountMax(int max){
        this.passengerCountMax = max;
    }

    /**
     * set minimum number of pedestrians in a scenario.
     * @param min: minimum number of pedestrians.
     */
    public void setPedestrianCountMin(int min){
        this.pedestrianCountMin = min;
    }

    /**
     * set maximum number of passengers in a scenario.
     * @param max: maximum number of passengers.
     */
    public void setPedestrianCountMax(int max){
        this.pedestrianCountMax = max;
    }

    /**
     * chose an attribute randomly form any kind of enum.
     * @param choice: Enum class which should be chosen from.
     * @param <T> type of Enum class.
     * @return T[index]: a random member of an enum.
     */
    public <T extends Enum<?>> T randomEnum(Class<T> choice) {
        T[] values = choice.getEnumConstants();
        int length = values.length;
        int randIndex = this.random.nextInt(length);
        return values[randIndex];
    }


    /**
     * create a human randomly.
     * @return Human: a human with random attributes setting.
     */
    public Human getRandomHuman(){
        Human randomHuman = new Human(this.random.nextInt(120), randomEnum(Human.Profession.class), randomEnum(Persona.Gender.class), randomEnum(Persona.BodyType.class), this.random.nextBoolean());
        return randomHuman;
    }

    /**
     * create an animal randomly.
     * @return Animal: a human with random attributes setting.
     */
    public Animal getRandomAnimal(){
        int randomSpecies = this.random.nextInt(Animal.getSpeciesList().length);
        String species = Animal.getSpeciesList()[randomSpecies];
        Animal randomAnimal = new Animal(species);
        return randomAnimal;
    }

    //generator

    /**
     * automatically create a scenario with random passengers and pedestrians sets.
     * @return Scenario: scenario with a random setting.
     */
    public Scenario generate(){

        int passengersNumber;
        int humanPassengersNumber;

        passengersNumber = this.random.nextInt((passengerCountMax+1) - passengerCountMin) + passengerCountMin;
        if(passengersNumber ==1){ //if only one passenger, it should be a human.
            humanPassengersNumber = 1;
        }else {
            humanPassengersNumber = this.random.nextInt(passengersNumber)+1; //at least one human
        }
        Persona[] passengers = new Persona[passengersNumber];

        int pedestriansNumber;
        int humanPedestriansNumber;

        pedestriansNumber = this.random.nextInt((pedestrianCountMax+1) - pedestrianCountMin) + pedestrianCountMin;
        if(pedestriansNumber ==1){
            humanPedestriansNumber =1;
        }else {
            humanPedestriansNumber = this.random.nextInt(pedestriansNumber) + 1; //at least one human
        }
        Persona[] pedestrians = new Persona[pedestriansNumber];

        int youInPassengerNumber = -1;
        int youInPedestrianNumber = -1;
        long seed = new Date().getTime();
        this.random.setSeed(seed);
        if(this.random.nextInt()%2 == 0){ //50% chance 'you' in scenario
            if (this.random.nextInt()%2 ==0){ //25% chance 'you' is a passenger
                youInPassengerNumber = this.random.nextInt(humanPassengersNumber); //'you' index of passenger[]
            }else {//25% chance you is a pedestrian
                youInPedestrianNumber = this.random.nextInt(humanPedestriansNumber); //'you' index of pedestrian[]
            }
        }

        for(int i =0; i<humanPassengersNumber; i++){ // generate human in car
            Human randomHuman = this.getRandomHuman();
            if(i == youInPassengerNumber){
                randomHuman.setAsYou(true);
            }
            passengers[i] = randomHuman;
        }
        for(int i =humanPassengersNumber; i<passengersNumber; i++){ //generate animal in car
            Animal randomAnimal = this.getRandomAnimal();
            randomAnimal.setPet(random.nextBoolean()); // animals in car should not always be a pet
            passengers[i] = randomAnimal;
        }

        for(int i =0; i<humanPedestriansNumber; i++){ // generate human on road
            Human randomHuman = this.getRandomHuman();
            if(i == youInPedestrianNumber){
                randomHuman.setAsYou(true);
            }
            pedestrians[i] = randomHuman;
        }
        for(int i =humanPedestriansNumber; i<pedestriansNumber; i++){ //generate animal on road
            Animal randomAnimal = this.getRandomAnimal();
            randomAnimal.setPet(random.nextBoolean());
            pedestrians[i] = randomAnimal;
        }

        Scenario scenario = new Scenario(passengers, pedestrians,this.random.nextBoolean());
        return scenario;
    }

}
