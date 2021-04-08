/*
 *  File: AttributeSorter.java
 *  Date: 2020.10.30
 *  Name: HongWei Hsu (1201945)
 */
import java.util.Comparator;

/**
 * Sort attributes by survival ratio in descending order
 * if tie, then by alphabetical order in ascending order
 * @author HongWei-HSU
 */
public class AttributeSorter implements Comparator<Attribute> {

    public int compare(Attribute att1, Attribute att2) {
        int result = 0;

        // sort by final ratio first.
        result = att2.getFinalRatio().compareTo(att1.getFinalRatio());

        // then sort by attribute name.
        if (result == 0){
            result = att1.getName().compareToIgnoreCase(att2.getName());
        }
        return result;
    }

}
