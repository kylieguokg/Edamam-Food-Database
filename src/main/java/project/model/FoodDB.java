package project.model;

import java.util.List;


/***
 * Stores all the data about the food
 */
public interface FoodDB {

    /***
     * Used for searching for an ingredient in the database
     * @param ingredient ingredient to be searched
     * @return search result (if search was successful or if there were any errors)
     */
    public String enterAnIngredient(String ingredient);

    /***
     * Used for retrieving specific nutrition information
     * about a food (with a measure) from the database
     * @param quantity quantity of food
     * @param measure measurement of food (e.g. kg)
     * @param food food more data is being retrieved for
     * @return search result (if search was successful or if there were any errors)
     */
    public String selectAFood(int quantity, Measure measure, Food food, boolean cachedVersion);

    /***
     * Checks if there is a cached version of the search for this measure, food and quantity
     * @param measure measure of food
     * @param food food selected
     * @param quantity quantity of food
     * @return true, if there is a cached version
     */
    public boolean checkIfCachedVersion(Measure measure, Food food, int quantity);

    /***
     * Returns the search results for a search for a food
     * @return search results of food
     */
    public List<Food> getSearchResults();

    public Food getSelectedFood();

    public void setSelectedFood(Food food);

    /***
     * Logs user in, which holds user's personalisation settings and saved foods
     */
    public boolean login(String username, String password);

    /***
     * Creates user, which holds user's personalisation settings and saved foods
     */
    public boolean createUser(String username, String password);

    /***
     * Gets currently logged in user
     */
    public User getUser();

    /***
     * Saves the colours a user has set to the database
     */
    public void saveColours();

    /***
     * Saves food to user's list of foods
     * @return result of attempt to save
     */
    public String saveFood(Food selectedFood);

    /***
     * Gets the user's list of saved foods
     * @return list of saved foods
     */
    public List<Food> getSavedFoods();

    /***
     * Clears cache of previously selected foods (with a quantity and measure)
     */
    public void clearCache();

    /***
     * Saves the quantities of nutrients a user has set to the database
     */
    public void saveTotalNutrients();

}

