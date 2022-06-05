package project.presenter;

import java.text.DecimalFormat;

import project.model.*;
import project.view.MaximumNutrientsView;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;


/***
 * Responsible for mutating the maximum nutrients view and the maximum nutrients state
 */
public class MaximumNutrientsPresenter implements Presenter, Observer, Subject {

    /***
     * View for viewing the total nutritional value of saved foods
     */
    private MaximumNutrientsView maximumNutrientsView;


    /***
     * Model logic for handling the maximum nutritional value for a food
     */
    private MaximumNutrientsState maximumNutrientsState;

    /***
     * List of observers who need to be updated if the saved foods state is changed
     */
    private List<Observer> observerList;

    /***
     * If the colours of the page need to be set again
     */
    public boolean needColourUpdate;

    private DecimalFormat decimalFormat;

    /***
     * List of types of fats
     */
    private List<String> fatStrings;

    /***
     * List of types of carbohydrates
     */
    private List<String> carbStrings;

    /***
     * Stores the max quantity numbers used for the bar chart
     */
    private Map<String, Double> maxQuantityPercents;

    /***
     * Stores the quantity numbers used for the bar chart
     */
    private Map<String, Double> quantityPercents;




    public MaximumNutrientsPresenter(MaximumNutrientsState maximumNutrientsState,
                                     MaximumNutrientsView currentView) {

        this.maximumNutrientsView = currentView;
        this.maximumNutrientsState = maximumNutrientsState;
        observerList = new CopyOnWriteArrayList<Observer>();

        maximumNutrientsState.subscribe(this);

        String pattern = "##.##";
        decimalFormat = new DecimalFormat(pattern);

        fatStrings = new ArrayList<String>();
        fatStrings.add("FAT");
        fatStrings.add("FASAT");
        fatStrings.add("FATRN");
        fatStrings.add("FAMS");
        fatStrings.add("FAPU");

        carbStrings = new ArrayList<String>();
        carbStrings.add("CHOCDF");
        carbStrings.add("CHOCDF.net");
        carbStrings.add("FIBTG");
        carbStrings.add("SUGAR");
        carbStrings.add("SUGAR.added");
    }


    @Override
    public void unsubscribePresenter(){
        maximumNutrientsState.unsubscribe(this);
    }


    @Override
    public void notifyObservers() {

        for (Observer observer : observerList){
            observer.update();
        }

        // reset state
        needColourUpdate = false;
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


    public boolean isNeedColourUpdate() {
        return needColourUpdate;
    }


    @Override
    public void update(){

        // If the max nutrients have been updated
        if (maximumNutrientsState.getMaximumTotalNutrientsState() != null){

            // show the updated stacked bar chart
            maximumNutrientsView.showStackedBarChart();
            needColourUpdate = true;
            notifyObservers();
        }

    }


    /***
     * Retrieves and formats the name and units of this nutrient
     * @param nutrient
     * @return Formatted nutrients
     */
    public String formatNutrientSTR(Nutrient nutrient) {
        if (nutrient.getQuantity() != null) {
            return nutrient.getLabel() + " (" + nutrient.getUnit() + ")";
        }

        return nutrient.getLabel();
    }



    /***
     * Retrieves and formats the running total amount of this nutrient
     * @param nutrient
     * @return Formatted nutrients
     */
    public String formatRunningTotalSTR(Nutrient nutrient) {

        if (maximumNutrientsState.getRunningTotalQuantity(nutrient.getNutrient()) != null) {
            Double quantity = maximumNutrientsState.getRunningTotalQuantity(nutrient.getNutrient());
            return decimalFormat.format(quantity) + " " + nutrient.getUnit() + "  ";
        } else {
            return "";
        }
    }


    /***
     * Retrieves and formats the total daily amount of this nutrient
     * @param nutrient
     * @return Formatted nutrients
     */
    public String formatRunningTotalDailySTR(Nutrient nutrient) {

        if (maximumNutrientsState.getRunningTotalDailyQuantity(nutrient.getNutrient()) != null) {
            Double quantity = maximumNutrientsState.getRunningTotalDailyQuantity(nutrient.getNutrient());
            return decimalFormat.format(quantity) + " %  ";
        } else {
            return "";
        }

    }


    /***
     * Retrieves and formats the maximum amount of this nutrient
     * @param nutrient
     * @return Formatted nutrients
     */
    public String formatMaximumSTR(Nutrient nutrient) {

        if (maximumNutrientsState.getMaxNutrientQuantity(nutrient.getNutrient()) != null) {
            Double quantity = maximumNutrientsState.getMaxNutrientQuantity(nutrient.getNutrient());
            return decimalFormat.format(quantity) + " " + nutrient.getUnit() + "  ";
        } else {
            return "";
        }
    }



    /***
     * Handles the changing of the maximum quantity in the table
     * @param nutrient nutrient selected
     * @param enteredQuantity quantity entered
     */
    public void changeQuantity(Nutrient nutrient, String enteredQuantity){

        Double quantity;

        // just take the first word
        if (enteredQuantity.contains(" ")){
            enteredQuantity = enteredQuantity.substring(0, enteredQuantity.indexOf(' '));
        }

        // has to be a double value
        try {
            quantity = Double.parseDouble(enteredQuantity);
        } catch (NumberFormatException nfe){
            // clear change
            maximumNutrientsView.refreshTable();
            return;
        } catch (IllegalArgumentException i){
            // clear change
            maximumNutrientsView.refreshTable();
            return;
        }

        // set the new max quantity
        if (nutrient.setQuantity(quantity)){
            maximumNutrientsState.setMaximumTotalNutrientValue(nutrient.getNutrient(), quantity);
        } else {
            maximumNutrientsView.refreshTable();
        }

    }

    /***
     * Gets data to represent max quantity percentage for
     * each nutrient in the stacked bar chart
     */
    public Map<String, Double> getMaxQuantityPercents(){
        return maxQuantityPercents;
    }

    /***
     * Gets data to represent the total quantity percentage for
     * each nutrient in the stacked bar chart
     */
    public Map<String, Double> getQuantityPercents(){
        return quantityPercents;
    }


    /***
     * Sets up data for stacked bar chart
     * Note: These percentages aren't mathematically correct,
     * they're just for the stacked bar chart,
     * so I've put them in the presenter rather than model
     */
    public void setUpStackedBarChart(){

        quantityPercents = new LinkedHashMap<String, Double>();
        maxQuantityPercents = new LinkedHashMap<String, Double>();

        // Retrieves the running total and maximum total for each nutrient
        // and adds it as % to the chart's data
        for (Nutrient nutrient : maximumNutrientsState.getRunningTotalNutrientsList().values()){

            Double maxQuantity = maximumNutrientsState.getMaxNutrientQuantity(nutrient.getNutrient());

            if (nutrient.getQuantity() != null && maxQuantity != null){

                Double percent;
                Double remaining;

                if (maxQuantity == 0){
                    percent = nutrient.getQuantity();
                    remaining = 100 - percent;
                } else {
                    percent = (nutrient.getQuantity()/maxQuantity)*100;
                    remaining = 100 - percent;
                    if (remaining < 0){
                        remaining = 0.0;
                    }
                }

                quantityPercents.put(nutrient.getLabel(), percent);
                maxQuantityPercents.put(nutrient.getLabel(), remaining);

            }

        }


    }

    /***
     * Gets list of types of fats
     * @return list of types of fats
     */
    public List<String> getFatStrings(){
        return fatStrings;
    }

    /***
     * Gets list of types of carbohydrates
     * @return list of types of carbohydrates
     */
    public List<String> getCarbStrings(){
        return carbStrings;
    }

    /***
     * Gets map of nutrient name to nutrient object storing maximum quantity of the nutrient
     * @return map of nutrient name to nutrient object
     */
    public Map<String, Nutrient> getMaxTotalNutrientsList() {
        return maximumNutrientsState.getMaxTotalNutrientsList();
    }

}
