package project.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

/***
 * Helper class to convert Json results from
 * HTTP and the database into POJOs
 */
public class FoodDBJsonParser {

    static Gson gson = new Gson();

    private List<Food> searchResults;


    /***
     * Converts a JsonElement storing search results into a list of foods
     * @param foodJE stores search results from Edanam
     */
    public String enterAnIngredient(String in){

        JsonElement foodJE = gson.fromJson(in, JsonElement.class);

        if (foodJE.getAsJsonObject().get("HTTPError") != null){

            return foodJE.getAsJsonObject().get("HTTPError").getAsString();
        }

        if (foodJE.getAsJsonObject().get("error") != null){

            String errorMsg = foodJE.getAsJsonObject().get("message").getAsString();
            return "ERROR: " + errorMsg;
        }

        JsonArray foodArray = foodJE.getAsJsonObject().get("hints").getAsJsonArray();

        int i = 0;

        searchResults = new ArrayList<Food>();

        if (foodArray.size() == 0){
            return "ERROR: No results";
        }


        while (i < foodArray.size()){

            JsonObject curFood = foodArray.get(i).getAsJsonObject();
            Food food = gson.fromJson(curFood.get("food"), Food.class);

            JsonArray measureJA = curFood.get("measures").getAsJsonArray();

            int j = 0;
            while (j < measureJA.size()){
                food.addMeasure(gson.fromJson(measureJA.get(j).getAsJsonObject(), Measure.class));
                j += 1;
            }

            i += 1;
            searchResults.add(food);
        }

        return "ok";

    }

    /***
     * Adds data from JsonElement storing data regarding the total nutritional value of a food to a Food POJO
     * @param foodJE stores data from Edanam
     * @param selectedFood food to store the total nutritional value information in
     */
    public String selectAFood(String in,
                              Food selectedFood){

        JsonElement foodJE = gson.fromJson(in, JsonElement.class);

        if (foodJE.getAsJsonObject().get("HTTPError") != null){

            return foodJE.getAsJsonObject().get("HTTPError").getAsString();
        }

        if (foodJE.getAsJsonObject().get("error") != null){

            String errorMsg = foodJE.getAsJsonObject().get("message").getAsString();
            return "ERROR: " + errorMsg;

        }

        selectedFood.setCalories(foodJE.getAsJsonObject().get("calories").getAsDouble());
        selectedFood.setTotalWeight(foodJE.getAsJsonObject().get("totalWeight").getAsDouble());

        // Get diet labels

        List<String> dietLabels = new ArrayList<>();

        JsonArray dietLabelsArray = foodJE.getAsJsonObject().get("dietLabels").getAsJsonArray();

        for (int i = 0; i < dietLabelsArray.size(); i++){
            dietLabels.add(dietLabelsArray.get(i).getAsString());
        }

        selectedFood.setDietLabels(dietLabels);

        // Get health labels

        List<String> healthLabels = new ArrayList<>();

        JsonArray healthLabelsArray = foodJE.getAsJsonObject().get("healthLabels").getAsJsonArray();

        for (int i = 0; i < healthLabelsArray.size(); i++){
            healthLabels.add(healthLabelsArray.get(i).getAsString());
        }

        selectedFood.setHealthLabels(healthLabels);

        // Get cautions

        List<String> cautions = new ArrayList<>();

        JsonArray cautionsArray = foodJE.getAsJsonObject().get("cautions").getAsJsonArray();

        for (int i = 0; i < cautionsArray.size(); i++){
            cautions.add(cautionsArray.get(i).getAsString());
        }

        selectedFood.setCautions(cautions);


        // get total nutrients

        TotalNutrients totalNutrients = new TotalNutrients();

        JsonObject nutrientArray = foodJE.getAsJsonObject().get("totalNutrients").getAsJsonObject();

        for (String curNutrientString : totalNutrients.getNutrientList().keySet()){

            Nutrient nutrient = gson.fromJson(nutrientArray.get(curNutrientString), Nutrient.class);

            if (nutrient == null){
                continue;
            }

            nutrient.setNutrient(curNutrientString);

            totalNutrients.addNutrient(curNutrientString, nutrient);

        }

        selectedFood.setTotalNutrients(totalNutrients);


        // get total daily nutrients

        TotalNutrients totalDaily = new TotalNutrients();
        totalDaily.setUnits("%");

        JsonObject dailyArray = foodJE.getAsJsonObject().get("totalDaily").getAsJsonObject();

        for (String curNutrientString : totalDaily.getNutrientList().keySet()){

            Nutrient nutrient = gson.fromJson(dailyArray.get(curNutrientString), Nutrient.class);

            if (nutrient == null){
                continue;
            }

            nutrient.setNutrient(curNutrientString);

            totalDaily.addNutrient(curNutrientString, nutrient);

        }

        selectedFood.setTotalDaily(totalDaily);

        return "ok";
    }


    /***
     * Creates a food object from Json results
     * @param in Json results from database
     * @return Food POJO
     */
    public Food getFood(List<String> in){
        if (in.size() == 1){
            return gson.fromJson(in.get(0), Food.class);
        }
        return null;
    }

    /***
     * Creates a User object from Json results
     * @param in Json results from database
     * @return User pojo
     */
    public User getUser(List<String> in){
        if (in.size() == 1){
            return gson.fromJson(in.get(0), User.class);
        }
        return null;
    }

    /***
     * Creates a list of Food POJOs from a list of Json strings from database
     * @param in list of saved foods as a list of json strings
     * @return List of saved foods as a list of Food POJOs
     */
    public List<Food> getSavedFoods(List<String> in){

        List<Food> savedFoods = new ArrayList<Food>();

        for (int i = 0; i < in.size(); i++){
            savedFoods.add(gson.fromJson(in.get(i), Food.class));
        }

        return savedFoods;
    }

    /***
     * Returns the search results for a search for a food
     * @return search results of food
     */
    public List<Food> getSearchResults(){
        return searchResults;
    }


}