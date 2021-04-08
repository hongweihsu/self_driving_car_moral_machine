/*
 *  File: Persona.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */

package ethicalengine;

/**
 * The Persona is a class covering any roles (Human and animal) in the program.
 * This class is parent class of Human and Animal.
 * Persona hold three main instance: age, gender and body type.
 * @author HongWei-HSU
 */
public abstract class Persona {

    public enum Gender{
        FEMALE,
        MALE,
        UNKNOWN
    }

    public enum BodyType{
        AVERAGE,
        ATHLETIC,
        OVERWEIGHT,
        UNSPECIFIED
    }

    private int age;
    private Gender gender;
    private BodyType bodyType;

    //constructor
    public Persona(){
        this.age = 1;
        this.gender = Gender.UNKNOWN;
        this.bodyType = BodyType.UNSPECIFIED;
    }

    public Persona(int age, Gender gender, BodyType bodytype){
        if(age < 1){
            age = 1;
        }
        this.age = age;
        this.gender = gender;
        this.bodyType = bodytype;
    }

    //constructor
    public Persona(Persona otherPersona){
        this.age = otherPersona.age;
        this.gender = otherPersona.gender;
        this.bodyType = otherPersona.bodyType;
    }

    //getter

    /**
     * get age information from the role.
     * @return age: age of the role.
     */
    public int getAge(){
        return this.age;
    }

    /**
     * get gender information from the role.
     * @return gender: gender of the role.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * get bidy type from the role.
     * @return bodyType: bodyType of the role.
     */
    public BodyType getBodyType() {
        return bodyType;
    }

    //setter

    /**
     * set age.
     * @param age: age for the role.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * set gender.
     * @param gender: gender for the role.
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * set body type.
     * @param bodytype: body type for the role.
     */
    public void setBodyType(BodyType bodytype) {
        this.bodyType = bodytype;
    }

    public abstract String toString();
}

