package project.model;

/***
 * Manages the retrieval of database for the food
 */
public class Model implements Observer {


    /***
     * Stores all the data about the food
     */
    static FoodDB foodDB;

    /***
     * Output long report is to be sent to
     */
    static Output output;

    /***
     * Manages logging in and creating users
     */
    private LoginState loginState;

    /***
     * Manages the process of retrieving the total
     * nutritional information for a selected food
     */
    private SelectedFoodState selectedFoodState;

    /***
     * Manages the process of searching for a food
     */
    private SearchState searchState;

    /***
     * Manages the process of saving a colour to a user's preferences
     */
    private SavedColoursState savedColoursState;

    /***
     * Manages the process of saving a food to a user's list of saved foods
     */
    private SavedFoodState savedFoodState;


    public Model(FoodDB foodDB, Output output){

        this.foodDB = foodDB;
        this.output = output;

        loginState = new LoginState(foodDB);
        loginState.subscribe(this);

        selectedFoodState = new SelectedFoodState(foodDB);
        searchState = new SearchState(foodDB);

    }

    @Override
    public void update(){

        // once user has logged in, can fetch the saved colours and saved foods of the user
        if (loginState.getLoggedIn()){
            savedColoursState = new SavedColoursState(foodDB,  loginState.getUser());
            savedFoodState = new SavedFoodState(foodDB,  loginState.getUser());
        }


    }

    /***
     * Saves the food to the user's list of saved foods
     * Adjusts the running total of nutrients accordingly
     */
    public void saveFood(){
        savedFoodState.saveFood(selectedFoodState.getSelectedFood());
    }

    /***
     * Gets the login state, manages logging in and creating users
     * @return the login state
     */
    public LoginState getLoginState() {
        return loginState;
    }

    /***
     * Used for retrieving specific nutrition information
     * about a food (with a measure) from the database
     * @param quantity quantity of food
     * @param measure measurement of food (e.g. kg)
     * @param food food more data is being retrieved for
     * @return search result (if search was successful or if there were any errors)
     */
    public void selectAFood(int quantity, Measure measure, Food food,
                                boolean cachedVersion){

        selectedFoodState.selectAFood(quantity, measure, food, cachedVersion,
                                    savedFoodState.getRunningTotalsTotalNutrients(),
                                    savedFoodState.getRunningTotalsTotalDaily() );

    }

    /***
     * Gets the selected food state, manages the user selecting a food for more info
     *  @return  the selected food state
     */
    public SelectedFoodState getSelectedFoodState(){
        return selectedFoodState;
    }

    /***
     * Sends a long report of the list of currently selected ingredients (name, measure, value)
     * along with the total nutritional value.
     * @return if operation was successful, or any errors
     */
    public void output(){
        output.output(savedFoodState.getOutputText());
    }

    /***
     *  Posts to Reddit the list of saved foods output text
     */
    public void postToReddit(){
        output.postToReddit(savedFoodState.getOutputText());
    }

    /***
     * Clears cache of selected foods
     */
    public void clearCache() {
        foodDB.clearCache();
    }

    /***
     * Gets the search state, Manages the user searching for ingredients
     *  @return  the search state
     */
    public SearchState getSearchState() {
        return searchState;
    }

    /***
     * Gets the saved colours state which manages the user's personalisation settings
     *  @return  the saved colours state
     */
    public SavedColoursState getSavedColoursState(){
        return savedColoursState;
    }

    /***
     * Gets the maximum nutrients state, which manages the total quantities of nutrient
     *  @return  the maximum nutrients state
     */
    public MaximumNutrientsState getMaximumNutrientsState(){
        return savedFoodState.getMaximumNutrientsState();
    }


    /***
     * Gets the saved food state which manages the user's list of saved food
     *  @return  the saved food state
     */
    public SavedFoodState getSavedFoodState(){
        return savedFoodState;
    }


    /***
     * Gets the output state, which manages the sending of the report
     *  @return  the output state
     */
    public Output getOutput() {
        return output;
    }
}
