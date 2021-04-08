/*
 *  File: Audit.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */

import ethicalengine.*;
import java.util.Arrays;

/**
 * This class holds all attributes from persona in the program
 * by running scenarios, this class will cumulate appearances of any attribute in given scenarios
 * It can sort survival ratio and print statistic results
 * and save result to output file
 * @author HongWei-HSU
 */
public class Audit {

    public static final String NEWLINE = "\n";
    private int runCount = 0;
    private String auditType;
    private Scenario[] configScenarios;
    private static double ageAverage = 0;
    private static int ageCountSurvival = 0;
    private static int peopleCountSurvival = 0;

    Attribute you = new Attribute("you");
    Attribute male = new Attribute("male");
    Attribute female = new Attribute("female");
    Attribute baby = new Attribute("baby");
    Attribute child = new Attribute("child");
    Attribute adult = new Attribute("adult");
    Attribute senior = new Attribute("senior");
    Attribute athletic = new Attribute("athletic");
    Attribute average = new Attribute("average");
    Attribute overweight = new Attribute("overweight");
    Attribute doctor = new Attribute("doctor");
    Attribute CEO = new Attribute("CEO");
    Attribute criminal = new Attribute("criminal");
    Attribute homeless = new Attribute("homeless");
    Attribute unemployed = new Attribute("unemployed");
    Attribute student = new Attribute("student");
    Attribute programmer = new Attribute("programmer");
    Attribute pregnant = new Attribute("pregnant");
    Attribute green = new Attribute("green");
    Attribute red = new Attribute("red");
    Attribute passengers = new Attribute("passengers");
    Attribute pedestrians = new Attribute("pedestrians");
    Attribute cat = new Attribute("cat");
    Attribute dog = new Attribute("dog");
    Attribute bird = new Attribute("bird");
    Attribute ferret = new Attribute("ferret");
    Attribute unicorn = new Attribute("unicorn");
    Attribute pet = new Attribute("pet");
    Attribute human = new Attribute("human");
    Attribute animal = new Attribute("animal");

    private final Attribute[] allAttribute = {you, male, female, baby, child, adult, senior, athletic, average,
            overweight, doctor, CEO, criminal, homeless, unemployed, student, programmer, pregnant, green, red,
            passengers, pedestrians, cat, dog, bird, ferret, unicorn, pet, human, animal};

    //constructor
    public Audit(){
        this.auditType = "Algorithm";
    }

    public Audit(Scenario[] scenarios){
        this.configScenarios = scenarios;
    }

    //scenario creator
    public void run(){

        for (Scenario configScenario : configScenarios) {
            EthicalEngine.Decision decision = EthicalEngine.decide(configScenario);
            this.calculate(configScenario, decision);
        }

        this.sortAttribute();
    }

    public void run(int runs) { //generate amount of n scenarios

        ScenarioGenerator scenarioGenerator = new ScenarioGenerator();

        for(int i = 0; i< runs; i++) {
            Scenario scenario = scenarioGenerator.generate();
            System.out.println(scenario.toString());
            EthicalEngine.Decision decision = EthicalEngine.decide(scenario);
            this.calculate(scenario, decision);
        }

        this.sortAttribute();
    }

    //sort attributes by survival ratio
    public void sortAttribute(){
        for( Attribute attribute : allAttribute){
            attribute.calculateRatio();
        }
        Arrays.sort(allAttribute, new AttributeSorter());
    }

    //calculate attribute
    public void calculate(Scenario scenario, EthicalEngine.Decision decision){
        this.runCount += 1;
        Persona[] survival;
        passengers.passengerCount(scenario);
        pedestrians.pedestrianCount(scenario);

        if (decision == EthicalEngine.Decision.PASSENGERS) {
            survival = scenario.getPassengers();//passengers saved
            passengers.passengerSurvivalCount(scenario);
        } else {
            survival = scenario.getPedestrians();//pedestrians saved
            pedestrians.pedestrianSurvivalCount(scenario);
        }

        for (Persona persona : survival) { //cumulate total survivor number, their age and other attributes
            if (persona instanceof Human) {
                ageCountSurvival += persona.getAge();
                peopleCountSurvival += 1;
                ageAverage = Math.round ((double) ageCountSurvival/peopleCountSurvival * 100.0)/100.0; //AVG. survival age
                human.survivalCountUpdate();
                this.genderCountSurvival((Human) persona);
                this.ageCategoryCountSurvival((Human) persona);
                this.bodyCountSurvival((Human) persona);
                this.professionCountSurvival((Human) persona);
                this.pregnantCountSurvival((Human) persona);
                this.isYouCountSurvival((Human) persona);
            } else if (persona instanceof Animal) {
                animal.survivalCountUpdate();
                this.speciesCountSurvival((Animal) persona);
                this.petCountSurvival((Animal) persona);
            }
        }

        //legality count
        if (scenario.isLegalCrossing()) {
            green.totalPersonaUpdate(scenario);
            green.totalSurvivalUpdate(scenario, decision);
        } else {
            red.totalPersonaUpdate(scenario);
            red.totalSurvivalUpdate(scenario, decision);
        }

        for(int j = 0; j< scenario.getPassengerCount(); j++){ //total passengers count
            if(scenario.getPassengers()[j] instanceof Human) {
                Human role = (Human) scenario.getPassengers()[j];
                human.countUpdate();
                this.genderCount(role);
                this.ageCategoryCount(role);
                this.bodyCount(role);
                this.professionCount(role);
                this.pregnantCount(role);
                this.isYouCount(role);

            }else {
                Animal role = (Animal) scenario.getPassengers()[j];
                animal.countUpdate();
                this.speciesCount(role);
                this.petCount(role);
            }
        }

        for(int j = 0; j< scenario.getPedestrianCount(); j++) { //total pedestrians count
            if (scenario.getPedestrians()[j] instanceof Human) {
                Human role = (Human) scenario.getPedestrians()[j];
                human.countUpdate();
                this.genderCount(role);
                this.ageCategoryCount(role);
                this.bodyCount(role);
                this.professionCount(role);
                this.pregnantCount(role);
                this.isYouCount(role);

            } else {
                Animal role = (Animal) scenario.getPedestrians()[j];
                animal.countUpdate();
                this.speciesCount(role);
                this.petCount(role);
            }
        }
    }

    // attribute accumulator
    public void genderCount(Human role){
        if(role.getGender() == Persona.Gender.FEMALE){
            female.countUpdate();
        }else if(role.getGender() == Persona.Gender.MALE) {
            male.countUpdate();
        }
    }

    public void ageCategoryCount(Human role){
        if(role.getAgeCategory() == Human.AgeCategory.CHILD) {
            child.countUpdate();
        }else if(role.getAgeCategory() == Human.AgeCategory.BABY){
            baby.countUpdate();
        }else if(role.getAgeCategory() == Human.AgeCategory.ADULT){
            adult.countUpdate();
        }else if(role.getAgeCategory() == Human.AgeCategory.SENIOR){
            senior.countUpdate();
        }
    }

    public void bodyCount(Human role){
        if (role.getBodyType() == Persona.BodyType.ATHLETIC){
            athletic.countUpdate();
        }else if(role.getBodyType() == Persona.BodyType.AVERAGE){
            average.countUpdate();
        }else if (role.getBodyType() == Persona.BodyType.OVERWEIGHT){
            overweight.countUpdate();
        }
    }

    public void professionCount(Human role){
        Human.Profession profession = role.getProfession();
        if (profession == Human.Profession.DOCTOR){
            doctor.countUpdate();
        }else if(profession == Human.Profession.CEO){
            CEO.countUpdate();
        }else if(profession == Human.Profession.HOMELESS){
            homeless.countUpdate();
        }else if(profession == Human.Profession.UNEMPLOYED){
            unemployed.countUpdate();
        }else if(profession == Human.Profession.CRIMINAL){
            criminal.countUpdate();
        }else if(profession == Human.Profession.STUDENT){
            student.countUpdate();
        }else if(profession == Human.Profession.PROGRAMMER){
            programmer.countUpdate();
        }
    }

    public void pregnantCount(Human role){
        if(role.isPregnant()){
            pregnant.countUpdate();
        }
    }

    public void isYouCount(Human role){
        if(role.isYou()){
            you.countUpdate();
        }
    }

    public void speciesCount(Animal role){
        String species = role.getSpecies();
        switch (species) {
            case "cat":
                cat.countUpdate();
                break;
            case "dog":
                dog.countUpdate();
                break;
            case "bird":
                bird.countUpdate();
                break;
            case "ferret":
                ferret.countUpdate();
                break;
            case "unicorn":
                unicorn.countUpdate();
                break;
        }
    }

    public void petCount(Animal role){
        if (role.isPet()){
            pet.countUpdate();
        }
    }

    // survival attribute accumulator
    public void genderCountSurvival(Human role) {
        if (role.getGender() == Persona.Gender.MALE) {
            male.survivalCountUpdate();
        } else if (role.getGender() == Persona.Gender.FEMALE) {
            female.survivalCountUpdate();
        }
    }

    public void ageCategoryCountSurvival(Human role){
        if(role.getAgeCategory() == Human.AgeCategory.CHILD) {
            child.survivalCountUpdate();
        }else if(role.getAgeCategory() == Human.AgeCategory.BABY){
            baby.survivalCountUpdate();
        }else if(role.getAgeCategory() == Human.AgeCategory.ADULT){
            adult.survivalCountUpdate();
        }else if(role.getAgeCategory() == Human.AgeCategory.SENIOR){
            senior.survivalCountUpdate();
        }
    }

    public void bodyCountSurvival(Human role) {
        if (role.getBodyType() == Persona.BodyType.ATHLETIC) {
            athletic.survivalCountUpdate();
        } else if (role.getBodyType() == Persona.BodyType.AVERAGE) {
            average.survivalCountUpdate();
        } else if (role.getBodyType() == Persona.BodyType.OVERWEIGHT) {
            overweight.survivalCountUpdate();
        }
    }

    public void professionCountSurvival(Human role) {
        Human.Profession profession = role.getProfession();
        if (profession == Human.Profession.DOCTOR) {
            doctor.survivalCountUpdate();
        } else if (profession == Human.Profession.CEO) {
            CEO.survivalCountUpdate();
        } else if (profession == Human.Profession.HOMELESS) {
            homeless.survivalCountUpdate();
        } else if (profession == Human.Profession.UNEMPLOYED) {
            unemployed.survivalCountUpdate();
        } else if (profession == Human.Profession.CRIMINAL) {
            criminal.survivalCountUpdate();
        } else if (profession == Human.Profession.STUDENT) {
            student.survivalCountUpdate();
        } else if (profession == Human.Profession.PROGRAMMER) {
            programmer.survivalCountUpdate();
        }
    }

    public void pregnantCountSurvival(Human role) {
        if (role.isPregnant()) {
            pregnant.survivalCountUpdate();
        }
    }

    public void isYouCountSurvival(Human role) {
        if (role.isYou()) {
            you.survivalCountUpdate();
        }
    }

    public void speciesCountSurvival(Animal role){
        String species = role.getSpecies();
        switch (species) {
            case "cat":
                cat.survivalCountUpdate();
                break;
            case "dog":
                dog.survivalCountUpdate();
                break;
            case "bird":
                bird.survivalCountUpdate();
                break;
            case "ferret":
                ferret.survivalCountUpdate();
                break;
            case "unicorn":
                unicorn.survivalCountUpdate();
                break;
        }
    }

    public void petCountSurvival(Animal role){
        if (role.isPet()){
            pet.survivalCountUpdate();
        }
    }

    //getter and setter
    public String getAuditType(){
        return this.auditType;
    }

    public void setAuditType(String name){
        this.auditType = name;
    }

    //toString method
    public String toString(){
        if(this.runCount != 0){
            String output = "";
            output += "======================================" + NEWLINE;
            output += "# "+this.getAuditType()+" Audit" + NEWLINE;
            output += "======================================" + NEWLINE;
            output += "- % SAVED AFTER " + this.runCount + " RUNS" + NEWLINE;

            for(Attribute attribute : allAttribute){
                if(attribute.getCount() != 0) {
                    output += attribute.getName().toLowerCase() + ": " + attribute.getFinalRatio() + NEWLINE;
                }
            }

            output += "--" + NEWLINE;
            output += "average age: " + String.format("%.2f",ageAverage) ;

            return output;
        }else {
            return ("no audit available");
        }
    }

    public void printStatistic(){
        System.out.println(this.toString());
    }

    //save file method
    public void printToFile(String filePath){
        String outputData = this.toString();
        EthicalEngine.fileManager.setLogger(filePath);
        EthicalEngine.fileManager.LOGGER.info(outputData);
    }

}
