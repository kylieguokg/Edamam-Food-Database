package project.presenter;

import javafx.application.Platform;
import javafx.concurrent.Task;

import project.model.*;
import project.view.NutrientsView;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.text.DecimalFormat;

/***
 * Responsible for mutating the nutrients view and the nutrients state
 */
public class NutrientsPresenter implements Presenter, Observer, Subject {

    /***
     * Stores domain logic
     */
    private final Model model;


    /***
     * View displaying the nutritional value of food
     */
    private NutrientsView nutrientsView;

    /***
     * Manages the storing of saved foods of user
     */
    private final SavedFoodState savedFoodState;

    /***
     * Manages the selection of a food by a user to view more nutritional info about
     */
    private SelectedFoodState selectedFoodState;

    /***
     * Selected quantity of currently selected food
     */
    private int selectedQuantity;

    /***
     * Selected measure of currently selected food
     */
    private Measure selectedMeasure;

    /***
     * Currently selected food
     */
    private Food selectedFood;

    /***
     * If the colours of the page need to be set again
     */
    public boolean needColourUpdate;

    private final ExecutorService pool = Executors.newFixedThreadPool(2, runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    });

    /***
     * List of types of fats
     */
    private List<String> fatStrings;

    /***
     * List of types of carbohydrates
     */
    private List<String> carbStrings;

    /***
     * Model logic for handling the maximum nutritional value for a food
     */
    private MaximumNutrientsState maximumNutrientsState;

    /***
     * List of observers who need to be updated if the saved foods state is changed
     */
    private List<Observer> observerList;

    private DecimalFormat decimalFormat;

    public NutrientsPresenter(Model model, NutrientsView nutrientsView) {
        this.model = model;
        this.selectedFoodState = model.getSelectedFoodState();
        this.savedFoodState = model.getSavedFoodState();
        this.maximumNutrientsState = savedFoodState.getMaximumNutrientsState();
        this.nutrientsView = nutrientsView;

        observerList = new CopyOnWriteArrayList<Observer>();

        nutrientsView.setUp(this);

        // changes in the selection of food and saving of food affects this Presenter
        selectedFoodState.subscribe(this);
        savedFoodState.subscribe(this);

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
    public void unsubscribePresenter() {
        selectedFoodState.unsubscribe(this);
        savedFoodState.unsubscribe(this);
    }

    public boolean isNeedColourUpdate() {
        return needColourUpdate;
    }

    @Override
    public void update() {

        // if result of attempt to save food is received, display it
        if (savedFoodState.getSavedFoodState() != null) {

            if (savedFoodState.getSavedFoodState().equals("ok")) {
                nutrientsView.displaySuccessMSG("SUCCESS: Please check the table for updated totals");
                nutrientsView.refreshTable();
            } else {
                nutrientsView.displayErrorMSG(savedFoodState.getSavedFoodState());
            }


        }

        // if result of attempt to get cached version of food is received, display it
        if (selectedFoodState.getCachedState() != null) {
            selectAFood(selectedFoodState.isCachedVersionAvailable());
        }

        // if result of attempt to select food is received, display it
        if (selectedFoodState.getSelectedFoodState() != null) {
            selectedFood = selectedFoodState.getSelectedFood();
            setNutrientsResults(selectedFoodState.getSelectedFoodState(), selectedFoodState.getSelectedFood());
        }


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

    public String getSelectedFoodLabel() {
        return selectedFoodState.getSelectedFoodLabel();
    }

    public Food getSelectedFood() {
        return selectedFoodState.getSelectedFood();
    }

    /***
     * See the nutritional information for this selcted food, quantity and measure
     * @param selectedMeasure
     * @param selectedQuantitySTR
     */
    public void seeNutritionalInformation(Measure selectedMeasure, String selectedQuantitySTR) {

        if (checkSelectInfo(selectedMeasure, selectedQuantitySTR)) {

            // have to check if there is a cached version first
            selectedFoodState.checkIfCachedVersion(selectedMeasure, selectedFood, selectedQuantity);
        }

    }


    /***
     * Checks that a valid measure and quantity have been selected before
     * retrieving the selection information for a food
     * @return error message if inputs are not valid, otherwise message from database
     */
    public boolean checkSelectInfo(Measure selectedMeasure, String selectedQuantitySTR) {

        selectedFood = selectedFoodState.getSelectedFood();

        // must select a measure

        if (selectedMeasure == null) {
            nutrientsView.displayErrorMSG("ERROR: Please select a measure");
            return false;
        }

        // quantity must be an integer

        try {
            selectedQuantity = Integer.parseInt(selectedQuantitySTR);
        } catch (NumberFormatException nfe) {
            nutrientsView.displayErrorMSG("ERROR: Please enter an integer");
            return false;
        }

        this.selectedMeasure = selectedMeasure;
        this.selectedQuantity = selectedQuantity;

        return true;
    }


    /***
     * Displays the results of an attempt to select food
     * @param result results of an attempt to select food
     * @param selectedFood food selected
     */
    public void setNutrientsResults(String result, Food selectedFood) {

        Platform.runLater(() -> {

            if (result == null) {
                return;
            } else if (result.startsWith("ERROR")) { // show error msg
                nutrientsView.displayErrorMSG(result);
            } else {
                nutrientsView.removeMSG();
                nutrientsView.showNutrientsInformation(selectedFood);
                needColourUpdate = true;
                notifyObservers();

            }
        });

    }



    /***
     * If there is a cached version, asks user if they want to use it, else, just selects the food
     * @param cachedVersionAvailable if there is a cahced version available or not
     */
    public void selectAFood(boolean cachedVersionAvailable) {

        if (cachedVersionAvailable) {
            nutrientsView.setUpCacheHitAlert();
        } else {
            // HTTP CALL
            modelSelectAFood(false);
        }

    }

    /***
     * Asks the model to retrieve information about the currently selected food
     * @param useCachedVersion use the cached version or not
     */
    public void modelSelectAFood(boolean useCachedVersion) {

        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                model.selectAFood(selectedQuantity, selectedMeasure, selectedFood, useCachedVersion);
                return null;
            }
        };

        pool.execute(task);

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
     * Retrieves and formats the amount of this nutrient
     * @param nutrient
     * @return Formatted nutrients
     */
    public String formatQuantitySTR(Nutrient nutrient) {
        if (nutrient.getUnit() != null && nutrient.getNutrient() != null) {

            if (selectedFood.getNutrientQuantity(nutrient.getNutrient()) != null) {
                Double quantity = selectedFood.getNutrientQuantity(nutrient.getNutrient());
                return decimalFormat.format(quantity) + " " + nutrient.getUnit() + "  ";
            }
        }

        return "";
    }

    /***
     * Retrieves and formats the resulting running total amount of this nutrient
     * @param nutrient
     * @return Formatted nutrients
     */
    public String formatResultingRunningTotalSTR(Nutrient nutrient) {

        if (nutrient.getUnit() != null && nutrient.getNutrient() != null && nutrient.getQuantity() != null) {

            if (selectedFood.getNutrientQuantity(nutrient.getNutrient()) != null) {
                Double quantity = selectedFood.getNutrientQuantity(nutrient.getNutrient());
                return decimalFormat.format(quantity + nutrient.getQuantity()) + " " + nutrient.getUnit() + "  ";
            }
        }

        return "";
    }


    /***
     * Retrieves and formats the running total amount of this nutrient
     * @param nutrient
     * @return Formatted nutrients
     */
    public String formatRunningTotalSTR(Nutrient nutrient) {

        if (nutrient.getQuantity() != null && nutrient.getUnit() != null) {
            return decimalFormat.format(nutrient.getQuantity()) + " " + nutrient.getUnit() + "  ";
        }

        return "";
    }


    /***
     * Retrieves and formats the total daily amount of this nutrient
     * @param nutrient
     * @return Formatted nutrients
     */
    public String formatTotalDailySTR(Nutrient nutrient) {

        if (nutrient.getUnit() != null && nutrient.getNutrient() != null && nutrient.getQuantity() != null) {

            if (selectedFood.getDailyNutrientQuantity(nutrient.getNutrient()) != null) {
                Double quantity = selectedFood.getDailyNutrientQuantity(nutrient.getNutrient());
                return decimalFormat.format(quantity) + " %  ";
            }
        }

        return "";

    }


    /***
     * Retrieves and formats the maximum amount of this nutrient
     * @param nutrient
     * @return Formatted nutrients
     */
    public String formatMaximumSTR(Nutrient nutrient) {

        if (nutrient.getUnit() != null && nutrient.getNutrient() != null) {

            if (maximumNutrientsState.getMaxNutrientQuantity(nutrient.getNutrient()) != null) {
                Double quantity = maximumNutrientsState.getMaxNutrientQuantity(nutrient.getNutrient());
                return decimalFormat.format(quantity) + " " + nutrient.getUnit() + "  ";
            }
        }


        return "";

    }


    /***
     * Gets map of nutrient name to nutrient object with running total quantity
     * @return map of nutrient name to nutrient object with running total quantity
     */
    public Map<String, Nutrient> getRunningTotalNutrientsList() {
        return maximumNutrientsState.getRunningTotalNutrientsList();
    }


    /***
     * Requests model to save the currently selected food, measure and quantity
     * @param selectedMeasure
     * @param selectedQuantitySTR
     */
    public void saveFood(Measure selectedMeasure, String selectedQuantitySTR) {

        if (checkSelectInfo(selectedMeasure, selectedQuantitySTR)) {
            model.saveFood();
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


}
