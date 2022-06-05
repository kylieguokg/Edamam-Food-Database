package project.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * Manages the saved foods of a user
 */
public class SavedFoodState implements Subject{

    /**
     * Currently logged in user
     */
    private User currentUser;

    /***
     * Stores the user's saved foods
     */
    private FoodDB foodDB;

    /***
     * List of observers who need to be updated if the saved foods state is changed
     */
    private List<Observer> observerList;

    /***
     * Current food being saved
     */
    private Food curSavedFood;

    /***
     * State of an attempt to change the saved food state
     */
    private String savedFoodState;

    /***
     * Holds the state of total nutritional information for a user's saved foods
     */
    private MaximumNutrientsState maximumNutrientsState;

    public SavedFoodState(FoodDB foodDB, User currentUser){
        this.foodDB = foodDB;
        this.currentUser = currentUser;
        observerList = new CopyOnWriteArrayList<Observer>();

        maximumNutrientsState = new MaximumNutrientsState(foodDB, currentUser);


        this.savedFoodState = null;

    }

    public List<Food> getSavedFoods() {
        return foodDB.getSavedFoods();
    }

    /***
     * Saves user's selected food to its database of saved foods
     * @param selectedFood
     */
    public void saveFood(Food selectedFood){

        curSavedFood = selectedFood;

        if (selectedFood == null){
            return;
        }

        maximumNutrientsState.saveFood(selectedFood);

        savedFoodState = foodDB.saveFood(selectedFood);

        notifyObservers();
    }



    @Override
    public void notifyObservers() {

        for (Observer observer : observerList){
            observer.update();
        }

        // reset state
        savedFoodState = null;
    }

    @Override
    public void subscribe(Observer observer){
        observerList.add(observer);
    }

    public Food getCurSavedFood() {
        return curSavedFood;
    }

    public String getSavedFoodState(){
        return savedFoodState;
    }


    /***
     * Formulates the output text containing the list of currently selected ingredients (name, measure, value)
     * along with the total nutritional value.
     * @return the output text
     */
    public String getOutputText(){
        String out = "\n### List of Selected Ingredients ###\n\n";

        for (Food food : getSavedFoods()){
            out += food.toString();
        }

        out += "\n### Total Nutritional Value ###\n\n";
        for (Nutrient nutrient : maximumNutrientsState.getRunningTotalNutrientsList().values()){
            out += nutrient.toString();
        }

        out += "\n### Total Daily Nutritional Value ###\n\n";

        for (Nutrient nutrient : maximumNutrientsState.getRunningTotalDailyList().values()){
            out += nutrient.toString();
        }

        return out;
    }

    public MaximumNutrientsState getMaximumNutrientsState(){
        return maximumNutrientsState;
    }

    /***
     * Retrieves the running total of total nutrients for user's list of saved foods
     * @return running total of total nutrients for user's list of saved foods
     */
    public TotalNutrients getRunningTotalsTotalNutrients() {
        return maximumNutrientsState.getRunningTotalsTotalNutrients();
    }

    /***
     * Retrieves the running total of total nutrients (daily) for user's list of saved foods
     * @return running total of total nutrients (daily) for user's list of saved foods
     */
    public TotalNutrients getRunningTotalsTotalDaily() {
        return maximumNutrientsState.getRunningTotalsTotalDaily();
    }

    /***
     * Retrieves the user's maximum values for nutrients
     * @return  user's maximum values for nutrients
     */
    public TotalNutrients getMaximumTotalNutrients() {
        return maximumNutrientsState.getMaximumTotalNutrients();
    }

    @Override
    public void unsubscribe(Observer observer){
        if (observerList.contains(observer)){
            observerList.remove(observer);
        }
    }
}
