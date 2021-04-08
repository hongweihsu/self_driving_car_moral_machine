/*
 *  File: EthicalEngine.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */

import ethicalengine.Human;
import ethicalengine.Persona;
import ethicalengine.Scenario;
import ethicalengine.ScenarioGenerator;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main process of the program
 * Read arguments to determine the operation flow
 * It support any combination of command line args sets (-c, -r, -h, -i)  in any order.
 * Holds a decision method to determine which group of roles should live
 * default setting is run audit to 100 random scenarios
 * @author HongWei-HSU
 */
public class EthicalEngine {

    public enum Decision {PASSENGERS, PEDESTRIANS};
    public static final FileManager fileManager = new FileManager();
    private static final String defaultSavePath = "./logs/results.log";
    private static final String userDefaultSavePath = "./logs/user.log";
    private static final String welcomePath = "./welcome.ascii";
    private static final String consentInfo = "Do you consent to have your decisions saved to a file? (yes/no)";
    private static final String helpInfo = "EthicalEngine - COMP90041 - Final Project\n\n" +
            "Usage: java EthicalEngine [arguments]\n\n" +
            "Arguments:\n" +
            "   -c or --config      Optional: path to config file\n" +
            "   -h or --help        Print Help (this message) and exit\n" +
            "   -r or --results     Optional: path to results log file\n" +
            "   -i or --interactive Optional: launches interactive mode";

    /**
     * Decides whether to save the passengers or the pedestrians
     * @param  scenario: the ethical dilemma
     * @return Decision: which group to save
     */
    public static Decision decide(Scenario scenario) {

        //which group has less marks will not survive
        int passengersMarks = 0;
        int pedestriansMarks = 0;

        Persona[] passengers = scenario.getPassengers();
        Persona[] pedestrians = scenario.getPedestrians();

        if(scenario.isLegalCrossing()){
            pedestriansMarks += 2;
        }else {
            passengersMarks += 2;
        }

        for(Persona role : passengers){
            if(role instanceof Human){
                passengersMarks +=1;
                if(((Human) role).isPregnant()){
                    passengersMarks +=2;
                }

            }
        }

        for(Persona role : pedestrians){
            if(role instanceof Human){
                pedestriansMarks+=1;
                if(((Human) role).isPregnant()){
                    pedestriansMarks +=2;
                }

            }
        }

        if (passengersMarks > pedestriansMarks) {
            return Decision.PASSENGERS;
        } else if(pedestriansMarks > passengersMarks){
            return Decision.PEDESTRIANS;
        }else {
            if (Math.random() > 0.5) {
                return Decision.PASSENGERS;
            } else {
                return Decision.PEDESTRIANS;
            }
        }
    }

    public static void main(String[] args) {

        boolean needHelp = false;
        boolean isInteractive = false;
        boolean isConfig = false;
        String specifiedSavePath = "";
        String specifiedSourcePath = "";
        Audit myAudit = new Audit();
        Scanner keyboard = new Scanner(System.in);
        ScenarioGenerator scenarioGenerator = new ScenarioGenerator();
        String decision ;
        String agreement ;
        Decision confirmDecision = null;
        String isContinue ;
        String notYetChoice = "";

        //read arguments each by each
        for (int i = 0; i < args.length; i++) {
            switch (args[i].toLowerCase()) {
                case "-h":
                case "--help":
                    needHelp = true;
                    break;
                case "-r":
                case "--results":
                    if (i < args.length - 1 && !args[i + 1].startsWith("-")) {
                        specifiedSavePath = args[i + 1];
                    }
                    break;
                case "-i":
                case "--interactive":
                    isInteractive = true;
                    break;
                case "-c":
                case "--config":
                    isConfig = true;
                    if (i < args.length - 1 && !args[i + 1].startsWith("-")) {
                        specifiedSourcePath = args[i + 1];
                    } else {
                        needHelp = true;
                    }
                    break;
            }
        }

        if (needHelp || (isConfig && specifiedSourcePath.equals(""))) {
            System.out.println(helpInfo);
        } else {


            if (isInteractive) {//interactive mode
                myAudit.setAuditType("User");
                fileManager.readWelcome(welcomePath);

                //asking for the data collection agreement
                System.out.println(consentInfo);
                agreement = notYetChoice;
                while (!agreement.equals("yes") && !agreement.equals("no")) {
                    agreement = keyboard.nextLine().toLowerCase();
                    try {
                        checkConsent(agreement);
                    } catch (InvalidInputException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (isConfig) {// has import file
                    ArrayList<Scenario> scenarios = fileManager.inputCSV(specifiedSourcePath);
                    Scenario[] scenariosPackage = scenarios.toArray(new Scenario[0]);
                    int readerIndex = 0;
                    while (readerIndex < scenariosPackage.length) {
                        System.out.println(scenariosPackage[readerIndex]);
                        System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
                        decision = keyboard.nextLine();
                        if (decision.equals("passenger") || decision.equals("passengers") || decision.equals("1")) {
                            confirmDecision = Decision.PASSENGERS;
                        } else if (decision.equals("pedestrian") || decision.equals("pedestrians") || decision.equals("2")) {
                            confirmDecision = Decision.PEDESTRIANS;
                        } else {
                            System.out.println("Please enter passenger/passengers/1 or pedestrian/pedestrians/2");
                        }
                        myAudit.calculate(scenariosPackage[readerIndex], confirmDecision);
                        myAudit.sortAttribute();

                        // for each three runs, show statistics
                        if ((readerIndex + 1) % 3 == 0) {
                            myAudit.printStatistic();
                            if (scenariosPackage.length - (readerIndex + 1) > 0) { //has at least one inputs for next run
                                //ask for another run
                                isContinue = notYetChoice;
                                while (!isContinue.equals("yes") && !isContinue.equals("no")) {
                                    System.out.println("Would you like to continue? (yes/no)");
                                    isContinue = keyboard.nextLine();
                                    if (isContinue.equals("no")) {
                                        readerIndex = scenariosPackage.length;
                                    }
                                }
                            }
                        }
                        readerIndex += 1;
                    }

                    if (agreement.equals("yes")) { //save output
                        if (specifiedSavePath.equals("")) {
                            myAudit.printToFile(userDefaultSavePath);
                        } else {
                            myAudit.printToFile(specifiedSavePath);
                        }
                    }

                    //preparing to finish program
                    System.out.println("That's all. Press Enter to quit.");
                    if (keyboard.nextLine() != null) {
                        System.exit(0);
                    }
                } else {//random scenario each by each (3 in 1 set)
                    while (true) {
                        isContinue = notYetChoice;
                        for (int i = 0; i < 3; i++) {
                            Scenario scenario = scenarioGenerator.generate();
                            System.out.println(scenario);
                            System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
                            decision = notYetChoice;
                            while (!decision.equals("passenger") && !decision.equals("passengers") && !decision.equals("1")
                                    && !decision.equals("pedestrian") && !decision.equals("pedestrians") && !decision.equals("2")) {
                                decision = keyboard.nextLine();
                                if (decision.equals("passenger") || decision.equals("passengers") || decision.equals("1")) {
                                    confirmDecision = Decision.PASSENGERS;
                                } else if (decision.equals("pedestrian") || decision.equals("pedestrians") || decision.equals("2")) {
                                    confirmDecision = Decision.PEDESTRIANS;
                                } else {
                                    System.out.println("Please enter passenger/passengers/1 or pedestrian/pedestrians/2");
                                }
                            }
                            myAudit.calculate(scenario, confirmDecision);
                            myAudit.sortAttribute();
                        }

                        myAudit.printStatistic();

                        //ask for another run
                        while (!isContinue.equals("yes") && !isContinue.equals("no")) {
                            System.out.println("Would you like to continue? (yes/no)");
                            isContinue = keyboard.nextLine();
                            if (isContinue.equals("no")) {
                                System.out.println("That's all. Press Enter to quit.");
                                if (agreement.equals("yes")) { //save output
                                    if (specifiedSavePath.equals("")) {
                                        myAudit.printToFile(userDefaultSavePath);
                                    } else {
                                        myAudit.printToFile(specifiedSavePath);
                                    }
                                }
                                if (keyboard.nextLine() != null) {
                                    System.exit(0);
                                }
                            }
                        }
                    }
                }
            } else {//not interactive mode
                if (isConfig) { //-c with path
                    ArrayList<Scenario> scenarios = fileManager.inputCSV(specifiedSourcePath);
                    Scenario[] scenariosToAudit = scenarios.toArray(new Scenario[0]);
                    myAudit = new Audit(scenariosToAudit);
                    myAudit.setAuditType("Algorithm");
                    myAudit.run();
                } else { // -r or no flag command
                    myAudit.setAuditType("Algorithm");
                    myAudit.run(100);
                }
                myAudit.printStatistic();
                if (specifiedSavePath.equals("")) {
                    myAudit.printToFile(defaultSavePath);
                } else {
                    myAudit.printToFile(specifiedSavePath);
                }
            }
        }
    }

    public static void checkConsent(String agreement) throws InvalidInputException{
        agreement = agreement.toLowerCase();
        if(!agreement.equals("yes") && !agreement.equals("no")) {
            throw new InvalidInputException();
        }
    }
}













