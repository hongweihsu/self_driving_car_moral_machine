/*
 *  File: Human.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */

package ethicalengine;

/**
 * The Human represent people of pedestrians and passengers.
 * This class is a child class of Persona.
 * Human has more instances: profession, age category, is pregnant and is you.
 * @author HongWei-HSU
 */
public class Human extends Persona{

    public enum Profession{
        DOCTOR,
        CEO,
        CRIMINAL,
        HOMELESS,
        UNEMPLOYED,
        STUDENT,
        PROGRAMMER,
        NONE
    }

    public enum AgeCategory{
        BABY,
        CHILD,
        ADULT,
        SENIOR
    }

    private Profession profession;
    private boolean isPregnant;
    private boolean isYou = false;

    //constructor
    public Human(){
        super();
    }

    public Human(int age, Gender gender, BodyType bodyType){
        super(age, gender, bodyType);
    }
    public Human(int age, Profession profession, Gender gender, BodyType bodyType, boolean isPregnant){
        super(age, gender, bodyType);

        if (age < 17 || age > 68) {
            this.profession = Profession.NONE;
        }else {
            this.profession = profession;
        }

        if(gender != Gender.FEMALE){
            this.isPregnant = false;
        }else {
            this.isPregnant = isPregnant;
        }
    }

    //copy constructor
    public Human(Human otherHuman){
        super();
        this.profession = otherHuman.profession;
        this.isPregnant = otherHuman.isPregnant;
    }

    //getter
    /**
     * generate age category according to the age.
     * @return AgeCategory: age category of a specific age range.
     */
    public AgeCategory getAgeCategory() {
        AgeCategory ageCategory;
        if (super.getAge() <= 4){
            ageCategory = AgeCategory.BABY;
        }else if(super.getAge() <= 16){
            ageCategory = AgeCategory.CHILD;
        }else if(super.getAge() <= 68){
            ageCategory = AgeCategory.ADULT;
        }else {
            ageCategory = AgeCategory.SENIOR;
        }

        return ageCategory;
    }

    /**
     * get the profession type of the human.
     * @return Profession: which profession the human belong.
     */
    public Profession getProfession(){
        if (this.getAgeCategory() != AgeCategory.ADULT){
            this.profession = Profession.NONE;
        }
        return this.profession;
    }

    /**
     * check whether a human is pregnant or not.
     * @return Boolean: is pregnant or not.
     */
    public boolean isPregnant() {
        return this.isPregnant;
    }

    /**
     * check whether a human is the program user.
     * @return Boolean: is user or not.
     */
    public boolean isYou(){
        return this.isYou;
    }

    //setter

    /**
     * set the pregnant status, if the human is not female, it always not pregnant.
     * @param pregnant is (or not) pregnant.
     */
    public void setPregnant(boolean pregnant) {
        if (super.getGender() != Gender.FEMALE) {
            pregnant = false;
        }
        this.isPregnant = pregnant;
    }

    /**
     * set the role as user (or not).
     * @param isYou: is you or not.
     */
    public void setAsYou(boolean isYou){
        this.isYou = isYou;
    }

    //toString methods
    /**
     * output all information of the human.
     * @return String output string of the human data.
     */
    public String toString(){
        String output;
        output = "";

        if(this.isYou()){
            output += "you ";
        }

        output += super.getBodyType();
        output += " "+ this.getAgeCategory();

        if(this.getProfession() != Profession.NONE){
            output += " "+ this.getProfession();
        }

        output += " " +super.getGender();

        if(this.isPregnant()){
            output += " pregnant";
        }
        return output.toLowerCase();

    }

}
