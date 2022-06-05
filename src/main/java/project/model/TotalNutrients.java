package project.model;

import java.util.*;


/*
    Stores the long form of the specific nutrients of a food
 */
public class TotalNutrients {

    private Map<String, Nutrient> nutrients = new LinkedHashMap<String, Nutrient>();
    private boolean arePercentages = false;

    // Each of the 35 nutrients from EDANAM
    public TotalNutrients(){
        nutrients.put("ENERC_KCAL", new Nutrient("ENERC_KCAL", "Energy", 0.0, "kcal" ));
        nutrients.put("FAT", new Nutrient("FAT", "Fat", 0.0, "g"));
        nutrients.put("FASAT", new Nutrient("FASAT","Saturated" , 0.0, "g"));
        nutrients.put("FATRN", new Nutrient("FATRN", "Trans", 0.0, "g"));
        nutrients.put("FAMS", new Nutrient("FAMS", "Monounsaturated", 0.0, "g"));
        nutrients.put("FAPU", new Nutrient("FAPU","Polyunsaturated" , 0.0, "g"));
        nutrients.put("CHOCDF", new Nutrient("CHOCDF", "Carbohydrate, by difference", 0.0, "g"));
        nutrients.put("FIBTG", new Nutrient("FIBTG", "Fibre", 0.0, "g"));
        nutrients.put("SUGAR", new Nutrient("SUGAR", "Sugars", 0.0, "g"));
        nutrients.put("PROCNT", new Nutrient("PROCNT", "Protein", 0.0, "g"));
        nutrients.put("CHOLE", new Nutrient("CHOLE", "Cholesterol", 0.0, "mg"));
        nutrients.put("NA", new Nutrient("NA", "Sodium", 0.0, "mg"));
        nutrients.put("CA", new Nutrient("CA", "Calcium", 0.0, "mg"));
        nutrients.put("MG", new Nutrient("MG", "Magnesium", 0.0, "mg"));
        nutrients.put("K", new Nutrient("K", "Potassium", 0.0, "mg"));
        nutrients.put("FE", new Nutrient("FE", "Iron", 0.0, "mg"));
        nutrients.put("ZN", new Nutrient("ZN", "Zinc", 0.0, "mg"));
        nutrients.put("P", new Nutrient("P", "Phosphorus", 0.0, "mg"));
        nutrients.put("VITA_RAE", new Nutrient("VITA_RAE", "Vitamin A", 0.0, "µg"));
        nutrients.put("VITC", new Nutrient("VITC", "Vitamin C",0.0,"µg" ));
        nutrients.put("THIA", new Nutrient("THIA","Thiamin" , 0.0, "mg"));
        nutrients.put("RIBF", new Nutrient("RIBF","Riboflavin" , 0.0, "mg"));
        nutrients.put("NIA", new Nutrient("NIA", "Niacin", 0.0, "mg"));
        nutrients.put("VITB6A", new Nutrient("VITB6A", "Vitamin B-6", 0.0, "mg"));
        nutrients.put("FOLDFE", new Nutrient("FOLDFE", "Folate, DFE", 0.0, "µg"));
        nutrients.put("FOLFD", new Nutrient("FOLFD",  "Folate, food", 0.0, "µg"));
        nutrients.put("FOLAC", new Nutrient("FOLAC", "Folic acid", 0.0, "µg"));
        nutrients.put("VITB12", new Nutrient("VITB12", "Vitamin B-12", 0.0, "µg"));
        nutrients.put("VITD", new Nutrient("VITD","Vitamin D (D2 + D3)", 0.0, "µg"));
        nutrients.put("TOCPHA", new Nutrient("TOCPHA", "Vitamin E (alpha-tocopherol)", 0.0, "mg"));
        nutrients.put("VITK1", new Nutrient("VITK1","Vitamin K (phylloquinone)" , 0.0, "µg"));
        nutrients.put("WATER", new Nutrient("WATER", "Water", 0.0, "g"));
        nutrients.put("SUGAR.added", new Nutrient("SUGAR.added", "Sugar added", 0.0, "g" ));
        nutrients.put("CHOCDF.net", new Nutrient("CHOCDF.net", "Carbohydrate (net)" , 0.0, "g"));
        nutrients.put("Sugar.alcohol", new Nutrient("Sugar.alcohol", "Sugar alcohols", 0.0, "g"));

    }

    /*
        Sets the quantities of all the nutrients to the same value
        @param quantity    quantity to be set for each nutrient
     */
    public void setQuantities(Double quantity){

        for (Nutrient nutrient: nutrients.values()){
            nutrient.setQuantity(quantity);
        }
    }

    /*
    * Sets the units of all the nutrients to the same value
    *  @param unit    unit to be set for each nutrient
    */
    public void setUnits(String unit){

        if (unit.equals("%")){
            arePercentages = true;
        }

        for (Nutrient nutrient: nutrients.values()){
            nutrient.setUnit(unit);
        }
    }

    public String arePercentages(){
        if (arePercentages){
            return "TRUE";
        }
        return "FALSE";
    }

    public List<Nutrient> getNutrients(){
        return nutrients.values().stream().toList();
    }

    /***
     * Return list of nutrient names
     * @return list of nutrient names
     */
    public List<String> getNutrientStrings(){
        return nutrients.keySet().stream().toList();
    }

    public Map<String, Nutrient> getNutrientList() {
        return nutrients;
    }

    /***
     * Add a nutrient to map of nutrient names to Nutrient objects
     * @param nutrientString nutrient name
     * @param nutrient nutrient object to add to map
     */
    public void addNutrient(String nutrientString, Nutrient nutrient) {
        nutrients.put(nutrientString, nutrient);
    }


    /***
     * Copies each of the nutrient in a list and returns a copy of the totalnutrients
     * @return copy of total nutrients
     */
    public TotalNutrients copy(){

        TotalNutrients copy = new TotalNutrients();

        for (String nutrientStr: nutrients.keySet()){
            Nutrient srcNutrient = nutrients.get(nutrientStr);
            copy.getNutrientList().get(nutrientStr).setQuantity(srcNutrient.getQuantity());
            copy.getNutrientList().get(nutrientStr).setUnit(srcNutrient.getUnit());
        }

        return copy;
    }

    public void setQuantity(String nutrient, double quantity){
        nutrients.get(nutrient).setQuantity(quantity);
    }

    public void addToQuantity(String nutrient, double quantity){
        nutrients.get(nutrient).addToQuantity(quantity);
    }


    /***
     * Gets quantity of a nutrient
     * @param nutrient nutrient to get quantity for
     * @return quantity of nutrient
     */
    public Double getQuantity(String nutrient){
        if (nutrients.get(nutrient) == null){
            return null;
        }
        return nutrients.get(nutrient).getQuantity();
    }

}

