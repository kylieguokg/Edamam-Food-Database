package project.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * Manages the maximum and running total nutrients
 */
public class MaximumNutrientsState implements Subject{

    /***
     * Currently logged in user
     */
    private User currentUser;

    /***
     * Database storing saved foods and users
     */
    private FoodDB foodDB;

    /***
     * List of observers who need to be updated if the maximum nutrients state is changed
     */
    private List<Observer> observerList;


    /***
     * Stores the running total quantities for each nutrient
     */
    private Map<String, Double> runningTotals;



    /***
     * Result of a change in maximum total nutrients
     */
    private String maximumTotalNutrientState;


    public MaximumNutrientsState(FoodDB foodDB, User currentUser){
        this.foodDB = foodDB;
        this.currentUser = currentUser;
        observerList = new CopyOnWriteArrayList<Observer>();

    }

    /***
     * Saved foods from database
     * @return list of saved foods from database
     */
    public List<Food> getSavedFoods() {
        return foodDB.getSavedFoods();
    }

    /***
     * Update the running total nutrients if this food was added
     * @param selectedFood food that was added
     */
    public void saveFood(Food selectedFood){
        if (selectedFood == null){
            return;
        }

        Map<String, Nutrient> nutrients = selectedFood.getNutrientsList();

        for (String curNutrientString : nutrients.keySet()){

            Nutrient nutrient = nutrients.get(curNutrientString);

            currentUser.addToRunningTotalNutrientsValue(curNutrientString, nutrient.getQuantity());

        }

        Map<String, Nutrient> nutrientsDaily = selectedFood.getDailyNutrientsList();

        for (String curNutrientString : nutrientsDaily.keySet()){

            Nutrient nutrient = nutrientsDaily.get(curNutrientString);

            currentUser.addToRunningTotalDailyNutrientsValue(curNutrientString, nutrient.getQuantity());

        }

        foodDB.saveTotalNutrients();
    }



    @Override
    public void notifyObservers() {

        for (Observer observer : observerList){
            observer.update();
        }

        // reset
        maximumTotalNutrientState = null;
    }

    @Override
    public void subscribe(Observer observer){
        observerList.add(observer);
    }

    /***
     * Stores the nutrients for the running total nutrients
     * @return  all the nutrients for the running total nutrients
     */
    public TotalNutrients getRunningTotalsTotalNutrients(){
        return currentUser.getRunningTotalsTotalNutrients();
    }

    /***
     * Stores the nutrients for the running total nutrients (as daily %)
     * @return  all the nutrients for the running total nutrients (as daily %)
     */
    public TotalNutrients getRunningTotalsTotalDaily(){
        return currentUser.getRunningTotalsTotalDaily();}

    /***
     * Stores the maximum total quantities for each nutrient
     * @return all the nutrients for the maximum total nutrients
     */
    public TotalNutrients getMaximumTotalNutrients(){
        return currentUser.getMaximumTotalNutrients();
    }

    /***
     * Change the maximum amount of a nutrient for a user (and saves it to the database)
     */
    public void setMaximumTotalNutrientValue(String nutrient, Double value){

        if (value < 0){
            throw new IllegalArgumentException("ERROR: Cannot have a max quantity less than 0");
        }

        currentUser.setMaximumTotalNutrientValue(nutrient, value);
        foodDB.saveTotalNutrients();
        maximumTotalNutrientState = "ok";
        notifyObservers();
    }

    /***
     * Result of a change in maximum total nutrients
     * @return max total nutrient state change
     */
    public String getMaximumTotalNutrientsState(){
        return maximumTotalNutrientState;
    }


    @Override
    public void unsubscribe(Observer observer){
        if (observerList.contains(observer)){
            observerList.remove(observer);
        }
    }

    /***
     * Gets map of nutrient name to nutrient object with running total quantity
     * @return map of nutrient name to nutrient object with running total quantity
     */
    public Map<String, Nutrient> getRunningTotalNutrientsList(){
        return currentUser.getRunningTotalNutrientsList();
    }


    /***
     * Gets map of nutrient name to nutrient object with running total daily percentage
     * @return map of nutrient name to nutrient object with running total daily percentage
     */
    public Map<String, Nutrient> getRunningTotalDailyList(){
        return currentUser.getRunningTotalDailyList();
    }

    /***
     * Gets map of nutrient name to nutrient object storing maximum quantity of the nutrient
     * @return map of nutrient name to nutrient object
     */
    public Map<String, Nutrient> getMaxTotalNutrientsList(){
        return currentUser.getMaxTotalNutrientsList();
    }


    public Double getMaxNutrientQuantity(String nutrient) {
        return currentUser.getMaxNutrientQuantity(nutrient);
    }

    public Double getRunningTotalDailyQuantity(String nutrient){
       return currentUser.getRunningTotalDailyQuantity(nutrient);
    }

    public Double getRunningTotalQuantity(String nutrient){
        return currentUser.getRunningTotalQuantity(nutrient);
    }
}



