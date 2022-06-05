package project.model;

import com.google.gson.JsonElement;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


/***
 * Manages retrieval and storing of data that requires the internet and database
 */
public class FoodDBOnline implements FoodDB{

    /***
     * Manages retrieving data from Edanam Food Database
     */
    private FoodDBHTTP foodDBHTTP;

    /***
     * Helper class to convert Json results from
     * HTTP and the database into POJOs
     */
    private FoodDBJsonParser foodDBJsonParser;

    /***
     * Manages the retrieval and storing of data in SQLite database
     */
    private FoodDBOnlineCache foodDBOnlineCache;

    /***
     * Cached version of currently selected food
     */
    private Food cachedOfSelected;

    /***
     * Currently selected food
     */
    private Food selectedFood;

    /***
     * Currently logged in user
     */
    private User currentUser;

    public FoodDBOnline(FoodDBHTTP foodDBHTTP, FoodDBJsonParser foodDBJsonParser, FoodDBOnlineCache foodDBOnlineCache){
        this.foodDBHTTP = foodDBHTTP;
        this.foodDBJsonParser = foodDBJsonParser;
        this.foodDBOnlineCache = foodDBOnlineCache;

        foodDBOnlineCache.createDatabaseWithSchema();
    }

    @Override
    public String enterAnIngredient(String ingredient){

        ingredient = ingredient.replace(" ", "%20");

        // not accepted characters
        ingredient = ingredient.replace("[", "");
        ingredient = ingredient.replace("]", "");

        String out = foodDBHTTP.enterAnIngredient(ingredient);

        String result = foodDBJsonParser.enterAnIngredient(out);

        return result;

    }

    @Override
    public String selectAFood(int quantity, Measure measure, Food food, boolean cachedVersion){

        if (cachedVersion){
            this.selectedFood = cachedOfSelected;
            return "ok";
        } else {

            this.selectedFood = food;
            this.selectedFood.setSelectedMeasure(measure);

            String out = foodDBHTTP.selectAFood(quantity, measure, selectedFood);

            // Converts HTTP results into POJOs and returns a msg regarding the success of call
            String result = foodDBJsonParser.selectAFood(out, selectedFood);

            // Save the food to cache
            foodDBOnlineCache.cacheSelectedFood(selectedFood, quantity);

            return result;
        }

    }

    @Override
    public boolean checkIfCachedVersion(Measure measure, Food food, int quantity){

        List<String> out = foodDBOnlineCache.getCachedFood(food.getFoodId(), measure.getUri(), quantity);

        // Converts HTTP results into a Food POJO
        Food cachedVersion = foodDBJsonParser.getFood(out);

        // if there was a cached version, store it
        if (cachedVersion != null){
            cachedOfSelected = cachedVersion;
            return true;
        }

        return false;

    }


    @Override
    public List<Food> getSearchResults(){
        return foodDBJsonParser.getSearchResults();
    }

    @Override
    public Food getSelectedFood() {
        return selectedFood;
    }

    @Override
    public void setSelectedFood(Food food) {
        this.selectedFood = food;
    }


    @Override
    public boolean login(String username, String password){

        // Check if the database has this login
        // login with hashed password
        List<String> out = foodDBOnlineCache.login(username, hashPassword(password));

        // Converts results to a POJO
        currentUser = foodDBJsonParser.getUser(out);

        if(currentUser == null){
            throw new IllegalArgumentException("ERROR: Invalid login");
        } else {
            return true;
        }


    }

    /***
     * Hashes password in SHA-1 format
     * @param  password password to hash
     * @return hashedPassword
     * Citation: GeeksforGeeks. (2019, January 3). SHA-1 Hash.
     * https://www.geeksforgeeks.org/sha-1-hash-in-java/
     */
    public String hashPassword(String password){

        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User getUser(){
        return currentUser;
    }

    @Override
    public boolean createUser(String username, String password) {

        // if username doesn't already exist, can create user
        if (!foodDBOnlineCache.checkUsernameExists(username)){

            foodDBOnlineCache.createUser(username, hashPassword(password), new User(username));
            return true;

        } else {
            throw new IllegalArgumentException( "ERROR: Account with username already exists");

        }

    }

    @Override
    public void saveColours(){
        foodDBOnlineCache.updatePersonalisation(currentUser);
    }

    @Override
    public void saveTotalNutrients(){
        foodDBOnlineCache.updatePersonalisation(currentUser);
    }


    @Override
    public String saveFood(Food food){
        return foodDBOnlineCache.saveFood(currentUser.getUsername(), food);
    }

    @Override
    public List<Food> getSavedFoods(){

        // retrieve list of saved foods for this user from the database
        List<String> out = foodDBOnlineCache.getSavedFoods(currentUser.getUsername());

        // convert JSon results into a list of Food POJOs
        List<Food> savedFoods = foodDBJsonParser.getSavedFoods(out);

        return savedFoods;
    }

    @Override
    public void clearCache(){
        foodDBOnlineCache.clearCache();
    }




}