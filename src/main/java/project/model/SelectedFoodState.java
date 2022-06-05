package project.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * Manages the process of retrieving the total
 * nutritional information for a selected food
 */
public class SelectedFoodState implements Subject{

    /**
     * Currently logged in user
     */
    private User currentUser;

    /***
     * Stores the user's saved foods
     */
    private FoodDB foodDB;

    /***
     * Currently selected food
     */
    private Food selectedFood;

    /***
     * State of an attempt to select food
     */
    private String selectedFoodState;

    /***
     * Currently selected measure for selected food
     */
    private Measure selectedMeasure;


    /***
     * Returns whether or not a cached version is available
     */
    private boolean cachedVersionAvailable;


    /***
     * List of observers who need to be updated if the selected foods state is changed
     */
    private List<Observer> observerList;

    /**
     * State of attempt to check if there's a cached version
     */
    private String cachedState;

    /**
     * If the food selected has been set
     */
    private boolean foodSelectedSet;

    public SelectedFoodState(FoodDB foodDB){
        this.foodDB = foodDB;
        observerList = new CopyOnWriteArrayList<Observer>();
    }

    public Food getSelectedFood(){
        return selectedFood;
    }

    public String getSelectedFoodLabel(){
        return selectedFood.getLabel();
    }

    public void selectAFood(int quantity, Measure measure, Food food,
                            boolean cachedVersion,
                            TotalNutrients runningTotalsTotalNutrients,
                            TotalNutrients runningTotalsTotalDaily){

        String out = foodDB.selectAFood(quantity, measure, food, cachedVersion);

        // show result of select
        selectedFoodState = out;
        selectedFood = foodDB.getSelectedFood();

        notifyObservers();
    }



    @Override
    public void notifyObservers() {

        for (Observer observer : observerList){
            observer.update();
        }


        // reset states
        cachedState = null;
        selectedFoodState = null;
        foodSelectedSet = false;
    }

    /***
     * Checks if there is a cached version of the data for this food, measure and quantity
     * @param measure
     * @param food
     * @param quantity
     */
    public void checkIfCachedVersion(Measure measure, Food food, int quantity){
        cachedVersionAvailable = foodDB.checkIfCachedVersion(measure, food, quantity);
        cachedState = "ok";
        notifyObservers();
    }

    /***
     * Returns whether there is a cached version available of the food
     * @return
     */
    public boolean isCachedVersionAvailable(){
        return cachedVersionAvailable;
    }

    @Override
    public void subscribe(Observer observer){
        observerList.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer){
        if (observerList.contains(observer)){
            observerList.remove(observer);
        }
    }

    /***
     * State of an attempt to select food
     * @return State of an attempt to select food
     */
    public String getSelectedFoodState(){
        return selectedFoodState;
    }

    public boolean isFoodSelectedSet(){
        return foodSelectedSet;
    }


    /***
     * Sets the currently selected food by the user
     * @param food
     */
    public void setSelectedFood(Food food) {
        this.selectedFood = food;
        foodSelectedSet = true;
        notifyObservers();
    }

    /**
     * State of attempt to check if there's a cached version
     * @return state of attempt to check if there's a cached version
     */
    public String getCachedState(){
        return cachedState;
    }

}
