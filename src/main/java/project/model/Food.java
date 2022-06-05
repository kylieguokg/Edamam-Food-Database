package project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
    Food item in Edanam Database
 */
public class Food {

    private String foodId;
    private String label;
    private Nutrients nutrients;
    private String category;
    private String categoryLabel;
    private String image;

    /***
     * Possible measures for this food
     */
    private List<Measure> measures = new ArrayList<Measure>();

    /***
     * Information about each nutrient for food
     */
    private TotalNutrients totalNutrients;

    /***
     * Information about each nutrient for food, as a daily percentage
     */
    private TotalNutrients totalDaily;
    private Double calories;
    private List<String> dietLabels;
    private List<String> healthLabels;
    private List<String> cautions;
    private Double totalWeight;
    private Measure selectedMeasure;

    public Food(String foodId, String label, Nutrients nutrients, String category, String categoryLabel, String image, List<Measure> measures) {
        this.foodId = foodId;
        this.label = label;
        this.nutrients = nutrients;
        this.category = category;
        this.categoryLabel = categoryLabel;
        this.image = image;
        this.measures = measures;
    }

    public Food(String foodId, String label, Nutrients nutrients, String category, String categoryLabel) {
        this.foodId = foodId;
        this.label = label;
        this.nutrients = nutrients;
        this.category = category;
        this.categoryLabel = categoryLabel;
        this.image = image;
        this.measures = measures;
    }

    public Food(String foodId, String label, String category, String categoryLabel) {
        this.foodId = foodId;
        this.label = label;
        this.category = category;
        this.categoryLabel = categoryLabel;
    }


    public void setCategory(String category){
        this.category = category;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Nutrients getNutrients() {
        return nutrients;
    }

    public void setNutrient(Nutrients nutrients) {
        this.nutrients = nutrients;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void addMeasure(Measure measure){
        if (measures == null){
            measures = new ArrayList<Measure>();
        }
        measures.add(measure);
    }

    public List<Measure> getMeasures(){
        return measures;
    }


    @Override
    public String toString(){
        String out =  "FoodId: " + foodId + " | Label: " + label + "\n" + nutrients.toString() +
                "\nCategory: " + category + " | Category Label: " + categoryLabel
                + "\nImage: " + image
                + "\nSelected Measure (Quantity): " + selectedMeasure.toString()
                + "\n";

        return out;
    }

    public TotalNutrients getTotalNutrients() {
        return totalNutrients;
    }

    public void setTotalNutrients(TotalNutrients totalNutrients) {
        this.totalNutrients = totalNutrients;
    }

    public void setNutrients(Nutrients nutrients) {
        this.nutrients = nutrients;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public List<String> getDietLabels() {
        return dietLabels;
    }

    public void setDietLabels(List<String> dietLabels) {
        this.dietLabels = dietLabels;
    }

    public List<String> getHealthLabels() {
        return healthLabels;
    }

    public void setHealthLabels(List<String> healthLabels) {
        this.healthLabels = healthLabels;
    }

    public List<String> getCautions() {
        return cautions;
    }

    public void setCautions(List<String> cautions) {
        this.cautions = cautions;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public TotalNutrients getTotalDaily() {
        return totalDaily;
    }

    public void setTotalDaily(TotalNutrients totalDaily) {
        this.totalDaily = totalDaily;
    }

    public Measure getSelectedMeasure() {
        return selectedMeasure;
    }

    public void setSelectedMeasure(Measure selectedMeasure) {
        this.selectedMeasure = selectedMeasure;
    }

    /***
     *   @return Calories
     */
    public Double getENERC_KCAL(){
        return nutrients.getENERC_KCAL();
    }

    /***
     *   @return Protein
     */
    public Double getPROCNT() {
        return nutrients.getPROCNT();
    }

    /***
     *   @return Fat
     */
    public Double getFAT() {
        return nutrients.getFAT();
    }

    /***
     *   @return Fibre
     */
    public Double getFIBTG() {
        return nutrients.getFIBTG();
    }

    /***
     *   @return Carbohydrates (by difference)
     */
    public Double getCHOCDF() {
        return nutrients.getCHOCDF();
    }


    public Map<String, Nutrient> getNutrientsList(){
        return totalNutrients.getNutrientList();
    }

    public Map<String, Nutrient> getDailyNutrientsList(){
        return totalDaily.getNutrientList();
    }

    public Double getNutrientQuantity(String nutrient){
        if (totalNutrients == null){
            return 0.0;
        }
        return totalNutrients.getQuantity(nutrient);
    }

    public Double getDailyNutrientQuantity(String nutrient){
        if (totalDaily == null){
            return 0.0;
        }
        return totalDaily.getQuantity(nutrient);
    }




}
