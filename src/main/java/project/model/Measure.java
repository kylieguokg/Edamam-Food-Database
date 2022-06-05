package project.model;

import java.util.ArrayList;
import java.util.List;


/*
*   A type of measurement for a food
*/
public class Measure {

    /*
    * Link to Edanam's definition of measurement
    */
    private String uri;
    private String label;
    private Double weight;

    public Measure(String uri, String label, Double weight) {
        this.uri = uri;
        this.label = label;
        this.weight = weight;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String toString(){
        return label + " ("  + weight + "g)\n";
    }

}
