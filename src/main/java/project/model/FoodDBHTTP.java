package project.model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


/***
 * Manages retrieving data from Edanam Food Database
 */

public class FoodDBHTTP {

    /***
     * Input API Key from Edanam
     */
    String INPUT_API_KEY = System.getenv("INPUT_API_KEY");

    /***
     * Input API ID from Edanam
     */
    String INPUT_API_APP_ID  = System.getenv("INPUT_API_APP_ID");


    /***
     * Sends HTTP request for data for searching for this ingredient
     * @param ingredient ingredient to search for
     * @return JsonElement storing the results of search
     */
    public String enterAnIngredient(String ingredient) {

        try {

            HttpRequest request = HttpRequest.newBuilder(new URI("https://api.edamam.com/api/food-database/v2/parser" +
                                                                    "?app_id=" + INPUT_API_APP_ID +
                                                                    "&app_key=" + INPUT_API_KEY +
                                                                    "&ingr=" + ingredient))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (IOException | InterruptedException e) {

            String errorStr = "{'HTTPError':'ERROR: Something went wrong with our request!'}";
            return errorStr;


        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
            String errorStr = "{'HTTPError':'ERROR: Invalid URI'}";
            return errorStr;
        }

    }

    /***
     * Sends HTTP request to get total nutritional information for a food, measure and quantity
     * @param quantity quantity of food
     * @param measure measure of food
     * @param food food more information is being requested for
     * @return JsonElement storing the results of search
     */
    public String selectAFood(int quantity, Measure measure, Food food){
        {
            try {

                String foodHeader = "{\"ingredients\": [{\"quantity\": " + quantity + ", "
                        + "\"measureURI\": \"" + measure.getUri() + "\","
                        + "\"foodId\": \"" + food.getFoodId() + "\"}]}";


                HttpRequest request = HttpRequest.newBuilder(new URI("https://api.edamam.com/api/food-database/v2/nutrients" +
                                "?app_id=" + INPUT_API_APP_ID +
                                "&app_key=" + INPUT_API_KEY
                        ))
                        .header("content-type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(foodHeader))
                        .build();


                HttpClient client = HttpClient.newBuilder().build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();

            } catch (IOException | InterruptedException e) {
                String errorStr = "{'HTTPError':'ERROR: Something went wrong with our request!'}";

                return errorStr;

            } catch (URISyntaxException ignored) {
                // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
                // hard-coded and so needs a way to be checked for correctness at runtime.
                String errorStr = "{'HTTPError':'ERROR: Invalid URI'}";
                return errorStr;
            }


        }
    }


}
