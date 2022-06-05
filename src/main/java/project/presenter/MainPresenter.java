package project.presenter;

import javafx.application.Platform;
import javafx.concurrent.Task;

import project.model.Model;
import project.model.Observer;
import project.model.Output;
import project.model.SavedColoursState;
import project.model.SelectedFoodState;
import project.model.LoginState;
import project.view.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/***
 *  Manages all the presenters and switches between them as necessary
 */
public class MainPresenter implements Observer {

    /***
     * Stores the main view which is a container for the current view and the navigation and settings
     */
    private final MainView mainView;

    /***
     * Stores domain logic
     */
    private Model model;

    /***
     * Displays the main visual elements of model logic
     */
    private View currentView;

    /***
     * Responsible for mutating the current view and the model logic
     */
    private Presenter currentPresenter;

    /***
     * Responsible for mutating the login view and login state
     */
    private LoginPresenter loginPresenter;

    /***
     * Responsible for mutating the maximum nutrients view and the maximum nutrients state
     */
    private MaximumNutrientsPresenter maximumNutrientsPresenter;

    /***
     * Responsible for mutating the nutrients view and the nutrients state
     */
    private NutrientsPresenter nutrientsPresenter;

    /***
     * Responsible for mutating the Saved Foods View and Saved Foods state
     */
    private SavedFoodPresenter savedFoodsPresenter;

    /***
     * Responsible for mutating the search view and the search state
     */
    private SearchPresenter searchPresenter;

    /***
     *   Displays the login and registration page
     */
    private LoginView loginView;

    /***
     *  Manages the views and navigation
     */
    private MaximumNutrientsView maximumNutrientsView;

    /***
     *   Displays the nutrition information for a food
     */
    private NutrientsView nutrientsView;

    /***
     *  Displays the list of saved foods for a user
     */
    private SavedFoodsView savedFoodsView;

    /***
     *   Displays the search interface for searching for a food in the database
     */
    private SearchView searchView;

    /***
     * Manages the saved colours of a user
     */
    private SavedColoursState savedColoursState;

    /***
     * Manages the sending of output in the app
     */
    private Output output;

    /***
     * Manages the logging in and registration of new users
     */
    private LoginState loginState;

    /***
     * Manages the process of retrieving the total
     * nutritional information for a selected food
     */
    private SelectedFoodState selectedFoodState;

    private final ExecutorService pool = Executors.newFixedThreadPool(2, runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true); // background thread
        return thread ;
    });

    public MainPresenter(Model model, MainView mainView){
        this.model = model;
        this.mainView = mainView;
        this.loginState = model.getLoginState();

        loginState.subscribe(this);
    }

    /*
       First go to login view
     */
    public void setUp(){
        goToLoginView();
    }

    @Override
    public void update(){

        // if the user was logged in
        if (loginState.getLoggedIn()) {

            // move to next page
            mainSetup();
        }

        if (output == null || savedColoursState == null || selectedFoodState == null){
            return;
        }

        // if a food has been selected from the search page
        if (selectedFoodState.isFoodSelectedSet()){
            // continue to nutrients scene,
            // where user chooses quantity and measure to
            // see more specific nutritional information
            goToNutrientsScene();

        }

        // if the nutrients presenter needs the colours to be set again
        if (nutrientsPresenter != null){
            if (nutrientsPresenter.isNeedColourUpdate()){
                setColours();
            }
        }

        // if the nutrients presenter needs the colours to be set again
        if (maximumNutrientsPresenter != null){
            if (maximumNutrientsPresenter.isNeedColourUpdate()){
                setColours();
            }
        }

        // if error MSG was received, display it
        if (output.getErrorMSG() != null){
            setOutputTXT(output.getErrorMSG());
        }

        // if result from an attempt to post to Pastebin was received, display result
        if (output.getOutputLink() != null){
            setOutputTXT(output.getOutputLink());
        }

        // if an attempt to login was successful, now attempt to post to Reddit
        if (output.getRedditLoggedUserIn() != null) {
            postToReddit();
        }

        // if result from an attempt to post to Reddit was received, display result
        if (output.getRedditOutputLink() != null){
            setOutputTXT(output.getRedditOutputLink());
        }

        // if colours have been updated, set the colours of the view
        if (savedColoursState.isColoursUpdated()){
            setColours();
        }

        // if dark mode has been updated, set the view respectively
        if (savedColoursState.darkModeUpdated()){

            if (savedColoursState.isDarkMode()) {
                mainView.setDarkMode();
            } else {
                mainView.setLightMode();
            }

            setColours();
        }






    }


    /***
     * Sets colours of the settings as well as the current view, as per user's preferences
     */
    public void setColours(){

        mainView.setColours(savedColoursState.isDarkMode(),
                            savedColoursState.getBackgroundColour(),
                            savedColoursState.getButtonColour(),
                            savedColoursState.getTextColour(),
                            savedColoursState.getTableColour(),
                            savedColoursState.getQuantityColour(),
                            savedColoursState.getMaxQuantityColour());

        currentView.setColours(savedColoursState.isDarkMode(),
                savedColoursState.getBackgroundColour(),
                savedColoursState.getButtonColour(),
                savedColoursState.getTextColour(),
                savedColoursState.getTableColour(),
                savedColoursState.getQuantityColour(),
                savedColoursState.getMaxQuantityColour());
    }


    /***
     * Goes to the login view - first page
     */
    public void goToLoginView(){

        LoginView loginView = new LoginView();
        LoginPresenter loginPresenter = new LoginPresenter(model.getLoginState(), loginView);
        loginView.display(loginPresenter);
        mainView.goToView(loginView);
    }

    /***
     * Main set up that is completed after user has logged in
     */
    public void mainSetup(){
        this.savedColoursState = model.getSavedColoursState();
        this.output = model.getOutput();
        this.selectedFoodState = model.getSelectedFoodState();

        // main presenter manages the colours and sending of output
        savedColoursState.subscribe(this);
        output.subscribe(this);
        selectedFoodState.subscribe(this);


        // set up navigation bar
        mainView.setUpTopBox();

        // first page is search view
        goToSearchView();

    }

    /***
     *   Goes to the search view
     */
    public void goToSearchView(){

        if (currentPresenter != null){
            currentPresenter.unsubscribePresenter();
        }

        this.searchView = new SearchView(mainView);
        this.searchPresenter = new SearchPresenter(model.getSearchState(),
                                                    model.getSelectedFoodState(),
                                                    searchView);

        this.currentView = searchView;
        this.currentPresenter = searchPresenter;

        searchView.setUp(searchPresenter);
        mainView.goToView(currentView);
        setColours();

    }


    /***
     *   Goes to the nutrition information for a food view
     */
    public void goToNutrientsScene(){

        if (currentPresenter != null){
            currentPresenter.unsubscribePresenter();
        }

        this.nutrientsView = new NutrientsView(mainView);
        this.nutrientsPresenter = new NutrientsPresenter(model, nutrientsView);

        this.currentView = nutrientsView;
        this.currentPresenter = nutrientsPresenter;

        nutrientsView.setUp(nutrientsPresenter);
        mainView.goToView(currentView);
        setColours();
        nutrientsPresenter.subscribe(this);
    }


    /***
     *   Goes to the running total nutrition
     *   information for a user's saved list of foods
     */
    public void goToMaxNutrients(){

        if (currentPresenter != null){
            currentPresenter.unsubscribePresenter();
        }

        this.maximumNutrientsView = new MaximumNutrientsView(mainView);
        this.maximumNutrientsPresenter = new MaximumNutrientsPresenter(model.getMaximumNutrientsState(),
                                                                     maximumNutrientsView);
        this.currentView = maximumNutrientsView;
        this.currentPresenter = maximumNutrientsPresenter;

        maximumNutrientsView.setUp(maximumNutrientsPresenter);
        mainView.goToView(currentView);
        maximumNutrientsPresenter.subscribe(this);
        setColours();

    }


    /***
     *   Goes to the list of saved foods for a user
     */
    public void goToSavedFoods(){

        if (currentPresenter != null){
            currentPresenter.unsubscribePresenter();
        }

        this.savedFoodsView = new SavedFoodsView(mainView);
        this.savedFoodsPresenter  = new SavedFoodPresenter(model.getSavedFoodState(), savedFoodsView);

        this.currentView = savedFoodsView;
        this.currentPresenter = savedFoodsPresenter;

        savedFoodsView.setUp(savedFoodsPresenter);
        mainView.goToView(currentView);
        setColours();

    }


    /***
     * Sends output to Pastebin
     */
    public void output(){
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                model.output();
                return null;
            }
        };

        pool.execute(task);

    }


    /***
     * Clears user's cache of search results
     */
    public void clearCache(){
        model.clearCache();
        mainView.cacheClearedAlert();
    }

    /***
     * Saves colours to user
     * @param darkMode
     * @param backgroundColour
     * @param buttonColour
     * @param textColour
     * @param tableColour
     * @param quantityColour
     * @param maxQuantityColour
     */
    public void saveColours(boolean darkMode, String backgroundColour, String buttonColour, String textColour,
                            String tableColour, String quantityColour, String maxQuantityColour) {

        savedColoursState.saveColours(darkMode, backgroundColour, buttonColour, textColour, tableColour, quantityColour, maxQuantityColour);

    }

    /***
     * Sets the output alert box to display message
     * @param out message
     */
    public void setOutputTXT(String out){

        Platform.runLater(() -> {
           mainView.setOutputTXT(out);
        });
    }


    /***
     * Saves dark mode status to user
     */
    public void setDarkMode() {
        savedColoursState.setDarkMode();
    }

    /***
     * Posts to Reddit
     */
    public void postToReddit(){

        if (output.isUserLoggedInReddit()){

            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    model.postToReddit();
                    return null;
                }
            };

            pool.execute(task);

        } else {
            mainView.setUpRedditLogin();
        }


    }

    /***
     * Logs user in to Reddit
     * @param username Reddit username
     * @param password Reddit password
     */
    public void loginReddit(String username, String password){
        output.getAccessToken(username, password);
    }




}
