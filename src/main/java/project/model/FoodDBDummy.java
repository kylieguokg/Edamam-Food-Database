package project.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * Stores dummy data for food database
 */
public class FoodDBDummy implements FoodDB {

    private List<Food> searchResults = new ArrayList<Food>();
    private Food selectedFood;
    private List<Food> savedFoods = new ArrayList<>();

    /***
     * Stores the nutrients for the running total nutrients
     */
    private TotalNutrients runningTotalsTotalNutrients;

    /***
     * Stores the nutrients for the maximum total nutrients
     */
    private TotalNutrients maximumTotalNutrients;

    /***
     *   Returns the resulting total nutrients for the saved list of foods
     *   if a food was added
     */
    private TotalNutrients tempTotalNutrients;

    /***
     *   Returns the resulting total nutrients (as daily %) for the saved list of foods
     *   if a food was added
     */
    private TotalNutrients tempTotalDaily;

    /***
     * Stores the nutrients for the running total nutrients (as daily %)
     */
    private TotalNutrients runningTotalsTotalDaily;

    private Measure kilogram;

    public FoodDBDummy(){

        kilogram = new Measure("http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram","Kilogram", 1000.0 );

        Nutrients chickenNuggetNutrients = new Nutrients(261, 14.36, 15.42, 16.24, 0.7);
        searchResults.add(new Food("food_bdxovpmabzq0y1b37dux7abl5muq",
                                    "Chicken Nugget",
                                            chickenNuggetNutrients,
                                    "Generic Goods",
                                "food",
                                    "https://www.edamam.com/food-img/853/853b7c281a7108739b5b987fe290e60e.jpg",
                                    Arrays.asList(kilogram)));


        Nutrients friesNutrients = new Nutrients(148.66307203479482, 3.936514854062405, 8.403066008379524, 14.908645877377431, 1.6772995925974905);
        searchResults.add(new Food("food_b7vp79wa0fu0mhblo2wbsabxfeuk",
                "Fries",
                friesNutrients,
                "Generic meals",
                "meal",
                null ,
                Arrays.asList(kilogram)));

        Nutrients milkshakeNutrients = new Nutrients(111.19183306951875, 2.6275819949275006, 6.470881939898806, 10.844772620634227, 0.6272453613911275);
        searchResults.add(new Food("food_amf2riyabwez2qa51ytu4bkog0eh",
                "Milkshakes",
                milkshakeNutrients,
                "Generic meals",
                "meal",
                null,
                 Arrays.asList(kilogram)));

        savedFoods = new ArrayList<Food>();
    }

    @Override
    public String enterAnIngredient(String ingredient) {
        return "ok";
    }

    @Override
    public String selectAFood(int quantity, Measure measure, Food food, boolean cachedVersion) {

        selectedFood = food;

        selectedFood.setSelectedMeasure(kilogram);

        selectedFood.setCalories(100.0);
        selectedFood.setTotalWeight(100.0);
        selectedFood.setDietLabels(Arrays.asList("YUM"));
        selectedFood.setHealthLabels(Arrays.asList("FUN"));
        selectedFood.setCautions(Arrays.asList("HOT"));

        TotalNutrients totalNutrients = new TotalNutrients();
        totalNutrients.setQuantities(28.0);
        selectedFood.setTotalNutrients(totalNutrients);

        TotalNutrients totalDaily = new TotalNutrients();
        totalDaily.setQuantities(50.0);
        totalDaily.setUnits("%");
        selectedFood.setTotalDaily(totalDaily);

        return "ok";
    }

    @Override
    public List<Food> getSearchResults() {
        return searchResults;
    }

    @Override
    public Food getSelectedFood() {
        return selectedFood;
    }

    @Override
    public void setSelectedFood(Food food) {
        this.selectedFood = food;
    }


    @Override
    public boolean checkIfCachedVersion(Measure measure, Food food, int quantity){
        return false;
    }

    @Override
    public boolean login(String username, String password){
        return true;
    }

    @Override
    public boolean createUser(String username, String password){
        return true;
    }

    @Override
    public User getUser(){
        return new User("user");
    }

    @Override
    public void saveColours(){
        // no database for dummy so do nothing
    }

    @Override
    public void saveTotalNutrients(){
        // no database for dummy so do nothing
    }



    @Override
    public String saveFood(Food food){
        savedFoods.add(food);
        return "ok";
    }

    @Override
    public List<Food> getSavedFoods(){
        return savedFoods;
    }

    @Override
    public void clearCache(){
        // no database for dummy so do nothing
    }




}
