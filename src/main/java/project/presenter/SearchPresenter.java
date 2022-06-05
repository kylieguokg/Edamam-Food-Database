package project.presenter;

import javafx.application.Platform;
import javafx.concurrent.Task;
import project.model.Food;
import project.model.Model;
import project.model.Observer;
import project.model.SearchState;
import project.model.SelectedFoodState;
import project.view.SearchView;

import java.text.DecimalFormat;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * Responsible for mutating the search view and the search state
 */
public class SearchPresenter implements Observer, Presenter {

    /***
     * Displays the search results
     */
    private final SearchView searchView;

    /***
     * Handles the process of searching for food in Edanam
     */
    private final SearchState searchState;

    /***
     * Handles the process of selecting a food
     * and procuring additional nutritional information in Edanam
     */
    private final SelectedFoodState selectedFoodState;

    private DecimalFormat decimalFormat;

    private final ExecutorService pool = Executors.newFixedThreadPool(2, runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread ;
    });

    public SearchPresenter(SearchState searchState,
                           SelectedFoodState selectedFoodState,
                           SearchView searchView){

        this.searchView = searchView;
        this.searchState = searchState;
        this.selectedFoodState = selectedFoodState;

        searchState.subscribe(this);

        String pattern = "##.##";
        decimalFormat = new DecimalFormat(pattern);
    }


    @Override
    public void update(){

        if (searchState.getSearchState()  != null){
            setSearchResults(searchState.getSearchState());
        }
    }

    @Override
    public void unsubscribePresenter(){
        searchState.unsubscribe(this);
    }


    /***
     * Attempts to select food from search results
     * @param obj food from search results
     * @param search current search
     */
    public void selectedFood(Object obj, String search){

        if (obj != null){

            selectedFoodState.setSelectedFood((Food) obj);
        } else {

            if (search.equals("")){
                searchView.displayErrorMSG("ERROR: Please enter an ingredient first");
            } else {
                searchView.displayErrorMSG("ERROR: Please select an ingredient from above first");

            }

        }

    }



    /***
     * Populate the table with search results
     * and show any error messages
     * @param results Result of the call to the API
     */
    public void setSearchResults(String results){

        Platform.runLater(() -> {
            if (results.equals("ok")){
                searchView.removeMSG();
                searchView.showSearchResults();


            } else{
                searchView.displayErrorMSG(results);
                searchView.clearTable();
            }
        });

    }

    /***
     * Retrieves results for search, run in the background
     */
    public void search(String search){
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                searchState.enterAnIngredient(search);
                return null;
            }
        };

        pool.execute(task);
    }

    public List<Food> getSearchResults(){
        return searchState.getSearchResults();
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

