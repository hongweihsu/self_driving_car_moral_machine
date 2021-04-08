/*
 *  File: Animal.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */

package ethicalengine;
/**
 * The Animal represent all kind animals of pedestrians and passengers.
 * This class is a child class of Persona.
 * Animal has more instances: species and is pet.
 * @author HongWei-HSU
 */
public class Animal extends Persona{

    private String species;
    private boolean isPet;
    private static final String[] speciesList = {"cat", "dog", "bird", "ferret", "unicorn"};


    //constructor
    public Animal(){
        super();
    }

    public Animal(String species){
        super();
        this.species = species;
    }

    //copy constructor
    public Animal(Animal otherAnimal){
        super();
        this.species = otherAnimal.species;
    }

    //getter and setter

    /**
     * get species info.
     * @return String: species of the animal.
     */
    public String getSpecies(){
        return this.species;
    }

    /**
     * check the animal is pet or not.
     * @return Boolean: is pet or not.
     */
    public boolean isPet(){
        return this.isPet;
    }

    /**
     * get all kind of species of this program.
     * @return ArrayList<String>: a list contains all species.
     */
    public static String[] getSpeciesList(){
        return Animal.speciesList;
    }

    /**
     * define species of the animal
     * @param species: species of the animal
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * set the animal to a pet (or not)
     * @param isPet: is pet (or not)
     */
    public void setPet(boolean isPet){
        this.isPet = isPet;
    }

    //toString methods

    /**
     * print information of the animal.
     * @return String: animal details.
     */
    public String toString(){
        String output;
        output = this.getSpecies();
        if(this.isPet()){
            output += " is pet";
        }
        return output.toLowerCase();
    }

}
