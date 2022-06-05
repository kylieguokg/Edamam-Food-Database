package project.model;

/***
 * Nutrient for Edanam Food
 */
public class Nutrient {

    /***
     * Long version of nutrient name
     */
    private String label;

    /***
     * Short version of nutrient name
     */
    private String nutrient;
    private Double quantity;
    private String unit;


    public Nutrient(String nutrient, String label, Double quantity, String unit){
        this.nutrient = nutrient;
        this.label = label;
        this.quantity = quantity;
        this.unit = unit;
    }


    public Nutrient(String nutrient, String label){
        this.nutrient = nutrient;
        this.label = label;
    }

    public String getNutrient() {
        return nutrient;
    }

    public void setNutrient(String nutrient) {
        this.nutrient = nutrient;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getQuantity() {
        return quantity;
    }

    public boolean setQuantity(Double quantity){
        if (quantity >= 0){
            this.quantity = quantity;
            return true;
        }
        return false;
    }

    public void addToQuantity(Double quantity) {
        this.quantity += quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString(){
        return "Nutrient: " + nutrient + " | Label: " + label
                +  " | Quantity: " + quantity + " | Unit: " + unit + "\n";
    }
}
