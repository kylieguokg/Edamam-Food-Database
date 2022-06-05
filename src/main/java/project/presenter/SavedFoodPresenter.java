package project.presenter;

import project.model.Food;
import project.model.Observer;
import project.model.SavedFoodState;
import project.view.SavedFoodsView;

import java.text.DecimalFormat;

import java.util.List;

/***
 * Responsible for mutating the Saved Foods View and Saved Foods state
 */
public class SavedFoodPresenter implements Presenter, Observer {

    /***
     * Manages the storing of saved foods of user
     */
    private SavedFoodState savedFoodState;

    /***
     * View displaying the list of saved foods for user
     */
    private SavedFoodsView savedFoodsView;


    private DecimalFormat decimalFormat;

    public SavedFoodPresenter(SavedFoodState savedFoodState, SavedFoodsView currentView) {

        this.savedFoodState = savedFoodState;
        this.savedFoodsView = currentView;

        String pattern = "##.##";
        decimalFormat = new DecimalFormat(pattern);

    }

    @Override
    public void update(){

    }

    @Override
    public void unsubscribePresenter(){
        savedFoodState.unsubscribe(this);
    }

    public List<Food> getSavedFoods(){
        return savedFoodState.getSavedFoods();
    }

    /***
     * Formats the quantity of fibre with 2 decimal place and units (g)
     * @param food food selected
     * @return quantity of fibre with grams
     */
    public String getFibreFormat(Food food){
        return decimalFormat.format(food.getNutrients().getFIBTG()) + " g  ";
    }


    /***
     * Formats the quantity of carbohydrates (by difference) with 2 decimal place and units (g)
     * @param food food selected
     * @return quantity of carbohydrates (by difference) with grams
     */
    public String getCarbohydrateFormat(Food food){
        return decimalFormat.format(food.getCHOCDF()) + " g  ";
    }

    /***
     * Formats the quantity of fat with 2 decimal place and units (g)
     * @param food food selected
     * @return quantity of fat with grams
     */
    public String getFatFormat(Food food){
        return decimalFormat.format(food.getFAT()) + " g  ";
    }

    /***
     * Formats the quantity of energy with 2 decimal place and units (kcal)
     * @param food food selected
     * @return quantity of energy with kcal
     */
    public String getEnergyFormat(Food food){
        return decimalFormat.format(food.getENERC_KCAL()) + " kcal  ";
    }

    /***
     * Formats the quantity of protein with 2 decimal place and units (g)
     * @param food food selected
     * @return quantity of protein (by difference) with grams
     */
    public String getProteinFormat(Food food){
        return decimalFormat.format(food.getPROCNT()) + " g  ";
    }

}
