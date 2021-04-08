/*
 *  File: FileManager.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */

import ethicalengine.Animal;
import ethicalengine.Human;
import ethicalengine.Persona;
import ethicalengine.Scenario;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class FileManager {

    /**
     * File manager deal with every thing about outside file
     * It hold a logger to save output file
     * It read legality for each scenario
     * and also create persona by input info and add it into the scenario after read each line
     */
    private static Handler fileHandler = null;
    public final Logger LOGGER = Logger.getLogger(FileManager.class.getName());

    //constructor
    public FileManager(){
    }

    //output method
    public void setLogger(String saveFilepath) {

        try {
            fileHandler = new FileHandler(saveFilepath, true);//append file
            LogFormatter logFormat = new LogFormatter();
            fileHandler.setFormatter(logFormat);
            LOGGER.addHandler(fileHandler);//adding Handler for file

        } catch (IOException e) {
            System.out.println("ERROR: could not print results. Target directory does not exist.");
            e.printStackTrace();
        }
    }

    //input method
    public ArrayList<Scenario> inputCSV(String csvFilePath){

        ArrayList<Scenario> scenarios = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(csvFilePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            String strLine;
            String[] lineToken;
            int lineCount = 0;
            int scenarioCount = 0;
            boolean isLegal;

            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console
                lineCount += 1;
                lineToken = strLine.split(",");

                try { //check input row with correct number of attributes
                    this.checkArgumentNumber(lineCount, lineToken.length);
                    if(lineToken.length == 10) { //persona data
                        if(!lineToken[0].equals("class")) {
                            try { // check whether data type of age can be transform to integer
                                Integer.parseInt(lineToken[2]);
                            } catch (java.lang.NumberFormatException e) {
                                lineToken[2] = "1"; //if age can not be integer numbers
                                System.out.println("\"WARNING: invalid number format in config file in line" +
                                        lineCount + "\"");
                            }
                            try { // check input attributes has corresponding enum value
                                this.checkCharacter(lineCount, lineToken);
                            } catch (InvalidCharacteristicException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }else if(lineToken.length == 1){ //about legality
                        try { // check input attributes has corresponding enum value
                            this.checkCharacter(lineCount, lineToken);
                        } catch (InvalidCharacteristicException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                } catch (InvalidDataFormatException | java.lang.NumberFormatException e) {
                    System.out.println(e.getMessage());
                }

                //generate scenario when read legality
                if(lineToken.length == 1) {
                    Persona[] passengers = {};
                    Persona[] pedestrians = {};
                    isLegal = lineToken[0].equals("scenario:green");
                    scenarioCount += 1;
                    Scenario scenario = new Scenario(passengers, pedestrians, isLegal);
                    scenarios.add(scenario);
                }else if(lineToken.length == 10){//generate persona to passengers and pedestrians
                    if(!lineToken[0].equals("class")) {
                        addPersonaToScenario(lineToken, scenarios.get(scenarioCount-1) ,scenarios.get(scenarioCount-1).getPassengers(),
                                scenarios.get(scenarioCount-1).getPedestrians());
                    }
                }
            }

            fileInputStream.close(); //Close the input stream

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: could not find config file.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("ERROR: could not find Line.");
        }
        return scenarios;
    }

    //welcome message
    public void readWelcome(String welcomeFilePath){
        try (BufferedReader reader = new BufferedReader(new FileReader(welcomeFilePath))) {
            int lineCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                lineCount += 1;
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //datatype transform method
    private void addPersonaToScenario(String[] lineToken, Scenario scenario, Persona[] passengers, Persona[] pedestrians){

        Human.Profession profession = Human.Profession.NONE;
        boolean isPregnant = false;
        boolean isYou = false;
        boolean isPet = false;
        Persona.Gender gender = Persona.Gender.valueOf(lineToken[1].toUpperCase());
        int age = Integer.parseInt(lineToken[2]);
        Persona.BodyType bodyType = Persona.BodyType.valueOf(lineToken[3].toUpperCase());
        if(lineToken[0].equals("human")) {
            profession = Human.Profession.valueOf(lineToken[4].toUpperCase());
            isPregnant = Boolean.parseBoolean(lineToken[5].toLowerCase());
            isYou = Boolean.parseBoolean(lineToken[6].toLowerCase());
        }
        if(lineToken[0].equals("animal")){
            isPet = Boolean.parseBoolean(lineToken[8].toLowerCase());
        }

        if (lineToken[9].equals("passenger")){
            if(lineToken[0].equals("human")) {
                Human human = new Human(age, profession, gender, bodyType, isPregnant);
                human.setAsYou(isYou);
                passengers = Arrays.copyOf(passengers, passengers.length+1);
                passengers[passengers.length-1] = human;
                scenario.setPassengers(passengers);

            }else if(lineToken[0].equals("animal")){
                Animal animal = new Animal(lineToken[7]);
                animal.setPet(isPet);
                passengers = Arrays.copyOf(passengers, passengers.length+1);
                passengers[passengers.length-1] = animal;
                scenario.setPassengers(passengers);
            }
        }else if(lineToken[9].equals("pedestrian")){
            if(lineToken[0].equals("human")) {
                Human human = new Human(age, profession, gender, bodyType, isPregnant);
                human.setAsYou(isYou);
                pedestrians = Arrays.copyOf(pedestrians, pedestrians.length+1);
                pedestrians[pedestrians.length-1] = human;
                scenario.setPedestrians(pedestrians);
            }else if(lineToken[0].equals("animal")){
                Animal animal = new Animal(lineToken[7]);
                animal.setPet(isPet);
                pedestrians = Arrays.copyOf(pedestrians, pedestrians.length+1);
                pedestrians[pedestrians.length-1] = animal;
                scenario.setPedestrians(pedestrians);
            }
        }
    }

    //data quality check method
    private void checkArgumentNumber(int lineCount,int lineLength) throws InvalidDataFormatException{
        if (lineLength != 1 && lineLength != 10){
            throw new InvalidDataFormatException(lineCount);
        }
    }

    private void checkCharacter(int lineCount, String[] lineToken) throws InvalidCharacteristicException{
        boolean throwError = false;
        boolean checkOK = false;

        if(lineToken.length ==10){
            //check gender
            if(lineToken[1].equals("")){
                lineToken[1] = "unknown";
            }
            for(Persona.Gender gender : Persona.Gender.values()){
                if (gender.name().equals(lineToken[1].toUpperCase())) {
                    checkOK = true;
                    break;
                }
            }
            if(!checkOK) {
                lineToken[1] = "unknown";
                throwError = true;
                checkOK = false;
            }
            //check body type
            if(lineToken[3].equals("")){
                lineToken[3] = "unspecified";
            }
            for (Persona.BodyType bodyType : Persona.BodyType.values()){
                if (bodyType.name().equals(lineToken[3].toUpperCase())) {
                    checkOK = true;
                    break;
                }
            }
            if(!checkOK){
                lineToken[3] = "unspecified";
                throwError = true;
                checkOK = false;
            }
            //check profession
            if(lineToken[0].toLowerCase().equals("human")) {
                if (lineToken[4].equals("")) {
                    lineToken[4] = "none";
                }
                for (Human.Profession profession : Human.Profession.values()) {
                    if (!profession.name().equals(lineToken[4].toUpperCase())) {
                        checkOK = true;
                        break;
                    }
                }
                if (!checkOK) {
                    lineToken[4] = "none";
                    throwError = true;
                    checkOK = false;
                }
            }
            //check pregnant
            if(lineToken[0].toLowerCase().equals("human")) {
                if (!lineToken[5].toLowerCase().equals("false") && !lineToken[5].toLowerCase().equals("true")) {
                    lineToken[5] = "false";
                    throwError = true;
                }
            }
            //check is you
            if(lineToken[0].toLowerCase().equals("human")) {
                if (!lineToken[6].toLowerCase().equals("false") && !lineToken[6].toLowerCase().equals("true")) {
                    lineToken[6] = "false";
                    throwError = true;
                }
            }
            //check species
            if(lineToken[0].toLowerCase().equals("animal")) {
                for (String species : Animal.getSpeciesList()) {
                    if (species.equals(lineToken[7].toLowerCase())) {
                        checkOK = true;
                        break;
                    }
                }
                if(!checkOK){
                    lineToken[7] = "cat"; //default species
                    throwError = true;
                }
            }
            //check is pet
            if(lineToken[0].toLowerCase().equals("animal")) {
                if (!lineToken[8].toLowerCase().equals("false") && !lineToken[8].toLowerCase().equals("true")) {
                    lineToken[8] = "false";
                    throwError = true;
                }
            }
            //check role
            if(!lineToken[9].equals("passenger") && !lineToken[9].equals("pedestrian")){
                lineToken[9] = "passenger";
                throwError = true;
            }
            //isError
            if(throwError){
                throw new InvalidCharacteristicException(lineCount);
            }
        }else if(lineToken.length == 1) {
            if (!lineToken[0].equals("scenario:green") && !lineToken[0].equals("scenario:red")) {
                lineToken[0] = "scenario:green";
                throw new InvalidCharacteristicException(lineCount);
            }
        }
    }
}
