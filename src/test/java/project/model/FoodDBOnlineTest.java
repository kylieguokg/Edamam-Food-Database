package project.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FoodDBOnlineTest {

    private FoodDB foodDB;
    private FoodDBJsonParser foodDBJsonParser;
    private FoodDBHTTP foodDBHTTP;
    private FoodDBOnlineCache foodDBOnlineCache;

    private Gson gson;

    @BeforeEach
    public void setup(){

        this.foodDBHTTP = mock(FoodDBHTTP.class);
        this.foodDBJsonParser = new FoodDBJsonParser();
        this.foodDBOnlineCache = mock(FoodDBOnlineCache.class);
        this.foodDB = new FoodDBOnline(foodDBHTTP, foodDBJsonParser, foodDBOnlineCache);

        gson = new Gson();

    }


    @Test
    /***
     * Tries to enter a valid search
     */
    public void enterAnIngredientTest(){

        when(foodDBHTTP.enterAnIngredient(any())).thenReturn(jsonFriedChickenSearch);

       assertEquals(foodDB.enterAnIngredient("fried chicken"), "ok");

    }


    @Test
    /***
     * Tries to select a food using cached data
     */
    public void selectAFoodCachedTest(){

        Nutrients nutrients = new Nutrients(307, 15.92, 20.36, 14.93, 0.9);
        Food selectedFood = new Food("food_alxugipafrrbkaa7nha90a0xyzuz", "Fried Chicken", nutrients, "Generic foods", "food");
        Measure measure = new Measure( "http://www.edamam.com/ontologies/edamam.owl#Measure_piece", "Piece", 16.0);

        assertTrue(foodDB.selectAFood(2, measure , selectedFood, true).equals("ok"));

    }

    @Test
    /***
     * Tries to select a food not using cached data
     */
    public void selectAFoodHTTPTest(){

        Nutrients nutrients = new Nutrients(307, 15.92, 20.36, 14.93, 0.9);
        Food selectedFood = new Food("food_alxugipafrrbkaa7nha90a0xyzuz", "Fried Chicken", nutrients, "Generic foods", "food");
        Measure measure = new Measure("http://www.edamam.com/ontologies/edamam.owl#Measure_piece", "Piece", 16.0);

        when(foodDBHTTP.selectAFood(2, measure, selectedFood)).thenReturn(jsonFriedChickenSelect);

        assertTrue(foodDB.selectAFood(2, measure , selectedFood, false).equals("ok"));

        assertEquals(foodDB.getSelectedFood(), selectedFood);
        assertEquals(foodDB.getSelectedFood().getSelectedMeasure(), measure);

    }

    @Test
    /***
     * Tries to check if there is a cached version when there is
     */
    public void checkIfCachedVersionTrue(){

        Nutrients nutrients = new Nutrients(307, 15.92, 20.36, 14.93, 0.9);
        Food selectedFood = new Food("food_alxugipafrrbkaa7nha90a0xyzuz", "Fried Chicken", nutrients, "Generic foods", "food");
        Measure measure = new Measure( "http://www.edamam.com/ontologies/edamam.owl#Measure_piece", "Piece", 16.0);

        List<String> in = new ArrayList<String>();

        in.add(savedFoodsJSON);

        when(foodDBOnlineCache.getCachedFood(selectedFood.getFoodId(), measure.getUri(), 2)).thenReturn(in);

        assertTrue(foodDB.checkIfCachedVersion(measure, selectedFood, 2));

    }

    @Test
    /***
     * Tries to check if there is a cached version when there is not
     */
    public void checkIfCachedVersionFalse(){

        Nutrients nutrients = new Nutrients(307, 15.92, 20.36, 14.93, 0.9);
        Food selectedFood = new Food("food_alxugipafrrbkaa7nha90a0xyzuz", "Fried Chicken", nutrients, "Generic foods", "food");
        Measure measure = new Measure( "http://www.edamam.com/ontologies/edamam.owl#Measure_piece", "Piece", 16.0);

        List<String> in = new ArrayList<String>();

        when(foodDBOnlineCache.getCachedFood(selectedFood.getFoodId(), measure.getUri(), 2)).thenReturn(in);

        assertFalse(foodDB.checkIfCachedVersion(measure, selectedFood, 2));

    }


    @Test
    /***
     * Tries to login with a valid user
     */
    public void loginValid(){

        List<String> in = new ArrayList<String>();

        in.add(userDetails);

        when(foodDBOnlineCache.login(any(), any())).thenReturn(in);

        assertTrue(foodDB.login("kylie", "123"));

    }

    @Test
    /***
     * Tries to login with a user and password that do not exist
     */
    public void loginInvalid(){

        List<String> in = new ArrayList<String>();


        when(foodDBOnlineCache.login(any(), any())).thenReturn(in);


        IllegalArgumentException exception =  assertThrows(IllegalArgumentException.class, () ->
                foodDB.login("kylie", "123"));


        assertEquals(exception.getMessage(), "ERROR: Invalid login");

    }


    @Test
    /***
     * Tries to create a user, and username does not exist yet
     */
    public void createUserDNETest(){


        when(foodDBOnlineCache.checkUsernameExists(any())).thenReturn(false);

        // username does not exist

        assertTrue(foodDB.createUser("kylie", "123"));

    }


    @Test
    /***
     * Tries to create a user, but username already exists
     */
    public void createUserExistsTest(){


        when(foodDBOnlineCache.checkUsernameExists(any())).thenReturn(true);

        // username does not exist

        IllegalArgumentException exception =  assertThrows(IllegalArgumentException.class, () ->
                foodDB.createUser("kylie", "123"));


        assertEquals(exception.getMessage(), "ERROR: Account with username already exists");

    }






    String userDetails = "{\"username\":\"kylie\",\"darkMode\":true,\"buttonColour\":\"WHITE\",\"textColour\":\"WHITE\",\"tableColour\":\"#6D41F5\",\"quantityColour\":\"#A0B7FA\",\"maxQuantityColour\":\"#A0B7FA\",\"backgroundColour\":\"BLACK\",\"maximumTotalNutrients\":{\"nutrients\":{\"ENERC_KCAL\":{\"label\":\"Energy\",\"nutrient\":\"ENERC_KCAL\",\"quantity\":500.0,\"unit\":\"kcal\"},\"FAT\":{\"label\":\"Fat\",\"nutrient\":\"FAT\",\"quantity\":500.0,\"unit\":\"g\"},\"FASAT\":{\"label\":\"Saturated\",\"nutrient\":\"FASAT\",\"quantity\":500.0,\"unit\":\"g\"},\"FATRN\":{\"label\":\"Trans\",\"nutrient\":\"FATRN\",\"quantity\":500.0,\"unit\":\"g\"},\"FAMS\":{\"label\":\"Monounsaturated\",\"nutrient\":\"FAMS\",\"quantity\":500.0,\"unit\":\"g\"},\"FAPU\":{\"label\":\"Polyunsaturated\",\"nutrient\":\"FAPU\",\"quantity\":500.0,\"unit\":\"g\"},\"CHOCDF\":{\"label\":\"Carbohydrate, by difference\",\"nutrient\":\"CHOCDF\",\"quantity\":500.0,\"unit\":\"g\"},\"FIBTG\":{\"label\":\"Fibre\",\"nutrient\":\"FIBTG\",\"quantity\":500.0,\"unit\":\"g\"},\"SUGAR\":{\"label\":\"Sugars\",\"nutrient\":\"SUGAR\",\"quantity\":500.0,\"unit\":\"g\"},\"PROCNT\":{\"label\":\"Protein\",\"nutrient\":\"PROCNT\",\"quantity\":500.0,\"unit\":\"g\"},\"CHOLE\":{\"label\":\"Cholesterol\",\"nutrient\":\"CHOLE\",\"quantity\":500.0,\"unit\":\"mg\"},\"NA\":{\"label\":\"Sodium\",\"nutrient\":\"NA\",\"quantity\":500.0,\"unit\":\"mg\"},\"CA\":{\"label\":\"Calcium\",\"nutrient\":\"CA\",\"quantity\":500.0,\"unit\":\"mg\"},\"MG\":{\"label\":\"Magnesium\",\"nutrient\":\"MG\",\"quantity\":500.0,\"unit\":\"mg\"},\"K\":{\"label\":\"Potassium\",\"nutrient\":\"K\",\"quantity\":500.0,\"unit\":\"mg\"},\"FE\":{\"label\":\"Iron\",\"nutrient\":\"FE\",\"quantity\":500.0,\"unit\":\"mg\"},\"ZN\":{\"label\":\"Zinc\",\"nutrient\":\"ZN\",\"quantity\":500.0,\"unit\":\"mg\"},\"P\":{\"label\":\"Phosphorus\",\"nutrient\":\"P\",\"quantity\":500.0,\"unit\":\"mg\"},\"VITA_RAE\":{\"label\":\"Vitamin A\",\"nutrient\":\"VITA_RAE\",\"quantity\":500.0,\"unit\":\"µg\"},\"VITC\":{\"label\":\"Vitamin C\",\"nutrient\":\"VITC\",\"quantity\":500.0,\"unit\":\"µg\"},\"THIA\":{\"label\":\"Thiamin\",\"nutrient\":\"THIA\",\"quantity\":500.0,\"unit\":\"mg\"},\"RIBF\":{\"label\":\"Riboflavin\",\"nutrient\":\"RIBF\",\"quantity\":500.0,\"unit\":\"mg\"},\"NIA\":{\"label\":\"Niacin\",\"nutrient\":\"NIA\",\"quantity\":500.0,\"unit\":\"mg\"},\"VITB6A\":{\"label\":\"Vitamin B-6\",\"nutrient\":\"VITB6A\",\"quantity\":500.0,\"unit\":\"mg\"},\"FOLDFE\":{\"label\":\"Folate, DFE\",\"nutrient\":\"FOLDFE\",\"quantity\":500.0,\"unit\":\"µg\"},\"FOLFD\":{\"label\":\"Folate, food\",\"nutrient\":\"FOLFD\",\"quantity\":500.0,\"unit\":\"µg\"},\"FOLAC\":{\"label\":\"Folic acid\",\"nutrient\":\"FOLAC\",\"quantity\":500.0,\"unit\":\"µg\"},\"VITB12\":{\"label\":\"Vitamin B-12\",\"nutrient\":\"VITB12\",\"quantity\":500.0,\"unit\":\"µg\"},\"VITD\":{\"label\":\"Vitamin D (D2 + D3)\",\"nutrient\":\"VITD\",\"quantity\":500.0,\"unit\":\"µg\"},\"TOCPHA\":{\"label\":\"Vitamin E (alpha-tocopherol)\",\"nutrient\":\"TOCPHA\",\"quantity\":500.0,\"unit\":\"mg\"},\"VITK1\":{\"label\":\"Vitamin K (phylloquinone)\",\"nutrient\":\"VITK1\",\"quantity\":500.0,\"unit\":\"µg\"},\"WATER\":{\"label\":\"Water\",\"nutrient\":\"WATER\",\"quantity\":500.0,\"unit\":\"g\"},\"SUGAR.added\":{\"label\":\"Sugar added\",\"nutrient\":\"SUGAR.added\",\"quantity\":500.0,\"unit\":\"g\"},\"CHOCDF.net\":{\"label\":\"Carbohydrate (net)\",\"nutrient\":\"CHOCDF.net\",\"quantity\":500.0,\"unit\":\"g\"},\"Sugar.alcohol\":{\"label\":\"Sugar alcohols\",\"nutrient\":\"Sugar.alcohol\",\"quantity\":500.0,\"unit\":\"g\"}},\"arePercentages\":false}}";


    String savedFoodsJSON = "\n" +
            "\n" +
            "{\"foodId\":\"food_a6wh6w5b8zsx9zbvjy1v8aje1l8q\",\"label\":\"Three-Cheese Grilled Cheese\",\"nutrients\":{\"ENERC_KCAL\":316.5167802323675,\"PROCNT\":12.731557510104173,\"FAT\":17.337934125229033,\"CHOCDF\":27.69723849248004,\"FIBTG\":1.373684930004947},\"category\":\"Generic meals\",\"categoryLabel\":\"meal\",\"measures\":[{\"uri\":\"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\"label\":\"Whole\",\"weight\":251.06274632395002},{\"uri\":\"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\"label\":\"Serving\",\"weight\":251.06274632395002},{\"uri\":\"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\"label\":\"Gram\",\"weight\":1.0},{\"uri\":\"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\"label\":\"Ounce\",\"weight\":28.349523125},{\"uri\":\"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\"label\":\"Pound\",\"weight\":453.59237},{\"uri\":\"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\"label\":\"Kilogram\",\"weight\":1000.0},{\"uri\":\"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\"label\":\"Milliliter\",\"weight\":1.0}],\"totalNutrients\":{\"nutrients\":{\"ENERC_KCAL\":{\"label\":\"Energy\",\"nutrient\":\"ENERC_KCAL\",\"quantity\":794.6557210275233,\"unit\":\"kcal\"},\"FAT\":{\"label\":\"Total lipid (fat)\",\"nutrient\":\"FAT\",\"quantity\":43.529093570637336,\"unit\":\"g\"},\"FASAT\":{\"label\":\"Fatty acids, total saturated\",\"nutrient\":\"FASAT\",\"quantity\":23.715972420329685,\"unit\":\"g\"},\"FATRN\":{\"label\":\"Fatty acids, total trans\",\"nutrient\":\"FATRN\",\"quantity\":1.013002297568504,\"unit\":\"g\"},\"FAMS\":{\"label\":\"Fatty acids, total monounsaturated\",\"nutrient\":\"FAMS\",\"quantity\":11.789562276489795,\"unit\":\"g\"},\"FAPU\":{\"label\":\"Fatty acids, total polyunsaturated\",\"nutrient\":\"FAPU\",\"quantity\":3.3576352858802117,\"unit\":\"g\"},\"CHOCDF\":{\"label\":\"Carbohydrate, by difference\",\"nutrient\":\"CHOCDF\",\"quantity\":69.5374476151146,\"unit\":\"g\"},\"FIBTG\":{\"label\":\"Fiber, total dietary\",\"nutrient\":\"FIBTG\",\"quantity\":3.448811111108651,\"unit\":\"g\"},\"SUGAR\":{\"label\":\"Sugars, total\",\"nutrient\":\"SUGAR\",\"quantity\":7.593397755085008,\"unit\":\"g\"},\"PROCNT\":{\"label\":\"Protein\",\"nutrient\":\"PROCNT\",\"quantity\":31.96419793468065,\"unit\":\"g\"},\"CHOLE\":{\"label\":\"Cholesterol\",\"nutrient\":\"CHOLE\",\"quantity\":110.42515938497871,\"unit\":\"mg\"},\"NA\":{\"label\":\"Sodium, Na\",\"nutrient\":\"NA\",\"quantity\":1311.4762297843638,\"unit\":\"mg\"},\"CA\":{\"label\":\"Calcium, Ca\",\"nutrient\":\"CA\",\"quantity\":534.7497450588891,\"unit\":\"mg\"},\"MG\":{\"label\":\"Magnesium, Mg\",\"nutrient\":\"MG\",\"quantity\":66.09194301027361,\"unit\":\"mg\"},\"K\":{\"label\":\"Potassium, K\",\"nutrient\":\"K\",\"quantity\":302.40656423239943,\"unit\":\"mg\"},\"FE\":{\"label\":\"Iron, Fe\",\"nutrient\":\"FE\",\"quantity\":5.351460865386774,\"unit\":\"mg\"},\"ZN\":{\"label\":\"Zinc, Zn\",\"nutrient\":\"ZN\",\"quantity\":3.6865165248366103,\"unit\":\"mg\"},\"P\":{\"label\":\"Phosphorus, P\",\"nutrient\":\"P\",\"quantity\":482.68255082698164,\"unit\":\"mg\"},\"VITA_RAE\":{\"label\":\"Vitamin A, RAE\",\"nutrient\":\"VITA_RAE\",\"quantity\":300.4727700540767,\"unit\":\"µg\"},\"VITC\":{\"label\":\"Vitamin C, total ascorbic acid\",\"nutrient\":\"VITC\",\"quantity\":9.301329999999671,\"unit\":\"mg\"},\"THIA\":{\"label\":\"Thiamin\",\"nutrient\":\"THIA\",\"quantity\":0.9360762165618624,\"unit\":\"mg\"},\"RIBF\":{\"label\":\"Riboflavin\",\"nutrient\":\"RIBF\",\"quantity\":0.8186369066082992,\"unit\":\"mg\"},\"NIA\":{\"label\":\"Niacin\",\"nutrient\":\"NIA\",\"quantity\":6.994080617827328,\"unit\":\"mg\"},\"VITB6A\":{\"label\":\"Vitamin B-6\",\"nutrient\":\"VITB6A\",\"quantity\":0.25317590304321397,\"unit\":\"mg\"},\"FOLDFE\":{\"label\":\"Folate, DFE\",\"nutrient\":\"FOLDFE\",\"quantity\":228.07706858981635,\"unit\":\"µg\"},\"FOLFD\":{\"label\":\"Folate, food\",\"nutrient\":\"FOLFD\",\"quantity\":93.08700192314978,\"unit\":\"µg\"},\"FOLAC\":{\"label\":\"Folic acid\",\"nutrient\":\"FOLAC\",\"quantity\":79.33881111111111,\"unit\":\"µg\"},\"VITB12\":{\"label\":\"Vitamin B-12\",\"nutrient\":\"VITB12\",\"quantity\":0.7811687957227377,\"unit\":\"µg\"},\"VITD\":{\"label\":\"Vitamin D (D2 + D3)\",\"nutrient\":\"VITD\",\"quantity\":0.6806685359045102,\"unit\":\"µg\"},\"TOCPHA\":{\"label\":\"Vitamin E (alpha-tocopherol)\",\"nutrient\":\"TOCPHA\",\"quantity\":1.210138959706543,\"unit\":\"mg\"},\"VITK1\":{\"label\":\"Vitamin K (phylloquinone)\",\"nutrient\":\"VITK1\",\"quantity\":5.420108292630275,\"unit\":\"µg\"},\"WATER\":{\"label\":\"Water\",\"nutrient\":\"WATER\",\"quantity\":100.65344670399409,\"unit\":\"g\"},\"SUGAR.added\":{\"label\":\"Sugar added\",\"nutrient\":\"SUGAR.added\",\"quantity\":0.0,\"unit\":\"g\"},\"CHOCDF.net\":{\"label\":\"Carbohydrate (net)\",\"nutrient\":\"CHOCDF.net\",\"quantity\":0.0,\"unit\":\"g\"},\"Sugar.alcohol\":{\"label\":\"Sugar alcohols\",\"nutrient\":\"Sugar.alcohol\",\"quantity\":0.0,\"unit\":\"g\"}},\"arePercentages\":false},\"totalDaily\":{\"nutrients\":{\"ENERC_KCAL\":{\"label\":\"Energy\",\"nutrient\":\"ENERC_KCAL\",\"quantity\":39.732786051376166,\"unit\":\"%\"},\"FAT\":{\"label\":\"Fat\",\"nutrient\":\"FAT\",\"quantity\":66.96783626251897,\"unit\":\"%\"},\"FASAT\":{\"label\":\"Saturated\",\"nutrient\":\"FASAT\",\"quantity\":118.57986210164843,\"unit\":\"%\"},\"FATRN\":{\"label\":\"Trans\",\"nutrient\":\"FATRN\",\"quantity\":0.0,\"unit\":\"%\"},\"FAMS\":{\"label\":\"Monounsaturated\",\"nutrient\":\"FAMS\",\"quantity\":0.0,\"unit\":\"%\"},\"FAPU\":{\"label\":\"Polyunsaturated\",\"nutrient\":\"FAPU\",\"quantity\":0.0,\"unit\":\"%\"},\"CHOCDF\":{\"label\":\"Carbs\",\"nutrient\":\"CHOCDF\",\"quantity\":23.1791492050382,\"unit\":\"%\"},\"FIBTG\":{\"label\":\"Fiber\",\"nutrient\":\"FIBTG\",\"quantity\":13.795244444434605,\"unit\":\"%\"},\"SUGAR\":{\"label\":\"Sugars\",\"nutrient\":\"SUGAR\",\"quantity\":0.0,\"unit\":\"%\"},\"PROCNT\":{\"label\":\"Protein\",\"nutrient\":\"PROCNT\",\"quantity\":63.9283958693613,\"unit\":\"%\"},\"CHOLE\":{\"label\":\"Cholesterol\",\"nutrient\":\"CHOLE\",\"quantity\":36.808386461659566,\"unit\":\"%\"},\"NA\":{\"label\":\"Sodium\",\"nutrient\":\"NA\",\"quantity\":54.64484290768183,\"unit\":\"%\"},\"CA\":{\"label\":\"Calcium\",\"nutrient\":\"CA\",\"quantity\":53.47497450588891,\"unit\":\"%\"},\"MG\":{\"label\":\"Magnesium\",\"nutrient\":\"MG\",\"quantity\":15.736176907208002,\"unit\":\"%\"},\"K\":{\"label\":\"Potassium\",\"nutrient\":\"K\",\"quantity\":6.434182217710626,\"unit\":\"%\"},\"FE\":{\"label\":\"Iron\",\"nutrient\":\"FE\",\"quantity\":29.730338141037635,\"unit\":\"%\"},\"ZN\":{\"label\":\"Zinc\",\"nutrient\":\"ZN\",\"quantity\":33.51378658942373,\"unit\":\"%\"},\"P\":{\"label\":\"Phosphorus\",\"nutrient\":\"P\",\"quantity\":68.95465011814024,\"unit\":\"%\"},\"VITA_RAE\":{\"label\":\"Vitamin A\",\"nutrient\":\"VITA_RAE\",\"quantity\":33.38586333934186,\"unit\":\"%\"},\"VITC\":{\"label\":\"Vitamin C\",\"nutrient\":\"VITC\",\"quantity\":10.334811111110746,\"unit\":\"%\"},\"THIA\":{\"label\":\"Thiamin (B1)\",\"nutrient\":\"THIA\",\"quantity\":78.00635138015521,\"unit\":\"%\"},\"RIBF\":{\"label\":\"Riboflavin (B2)\",\"nutrient\":\"RIBF\",\"quantity\":62.972069739099936,\"unit\":\"%\"},\"NIA\":{\"label\":\"Niacin (B3)\",\"nutrient\":\"NIA\",\"quantity\":43.7130038614208,\"unit\":\"%\"},\"VITB6A\":{\"label\":\"Vitamin B6\",\"nutrient\":\"VITB6A\",\"quantity\":19.47506946486261,\"unit\":\"%\"},\"FOLDFE\":{\"label\":\"Folate equivalent (total)\",\"nutrient\":\"FOLDFE\",\"quantity\":57.01926714745409,\"unit\":\"%\"},\"FOLFD\":{\"label\":\"Folate, food\",\"nutrient\":\"FOLFD\",\"quantity\":0.0,\"unit\":\"%\"},\"FOLAC\":{\"label\":\"Folic acid\",\"nutrient\":\"FOLAC\",\"quantity\":0.0,\"unit\":\"%\"},\"VITB12\":{\"label\":\"Vitamin B12\",\"nutrient\":\"VITB12\",\"quantity\":32.548699821780744,\"unit\":\"%\"},\"VITD\":{\"label\":\"Vitamin D\",\"nutrient\":\"VITD\",\"quantity\":4.537790239363401,\"unit\":\"%\"},\"TOCPHA\":{\"label\":\"Vitamin E\",\"nutrient\":\"TOCPHA\",\"quantity\":8.067593064710287,\"unit\":\"%\"},\"VITK1\":{\"label\":\"Vitamin K\",\"nutrient\":\"VITK1\",\"quantity\":4.516756910525229,\"unit\":\"%\"},\"WATER\":{\"label\":\"Water\",\"nutrient\":\"WATER\",\"quantity\":0.0,\"unit\":\"%\"},\"SUGAR.added\":{\"label\":\"Sugar added\",\"nutrient\":\"SUGAR.added\",\"quantity\":0.0,\"unit\":\"%\"},\"CHOCDF.net\":{\"label\":\"Carbohydrate (net)\",\"nutrient\":\"CHOCDF.net\",\"quantity\":0.0,\"unit\":\"%\"},\"Sugar.alcohol\":{\"label\":\"Sugar alcohols\",\"nutrient\":\"Sugar.alcohol\",\"quantity\":0.0,\"unit\":\"%\"}},\"arePercentages\":true},\"calories\":794.0,\"dietLabels\":[],\"healthLabels\":[\"VEGETARIAN\",\"PESCATARIAN\",\"EGG_FREE\",\"PEANUT_FREE\",\"TREE_NUT_FREE\",\"SOY_FREE\",\"FISH_FREE\",\"SHELLFISH_FREE\",\"PORK_FREE\",\"RED_MEAT_FREE\",\"CRUSTACEAN_FREE\",\"CELERY_FREE\",\"MUSTARD_FREE\",\"SESAME_FREE\",\"LUPINE_FREE\",\"MOLLUSK_FREE\",\"ALCOHOL_FREE\",\"KOSHER\"],\"cautions\":[],\"totalWeight\":251.06274632395002,\"selectedMeasure\":{\"uri\":\"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\"label\":\"Whole\",\"weight\":251.06274632395002}}";


    String jsonFriedChickenSearch = "{\n" +
            "  \"text\": \"fried chicken\",\n" +
            "  \"parsed\": [\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_alxugipafrrbkaa7nha90a0xyzuz\",\n" +
            "        \"label\": \"Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 307,\n" +
            "          \"PROCNT\": 15.92,\n" +
            "          \"FAT\": 20.36,\n" +
            "          \"CHOCDF\": 14.93,\n" +
            "          \"FIBTG\": 0.9\n" +
            "        },\n" +
            "        \"category\": \"Generic foods\",\n" +
            "        \"categoryLabel\": \"food\",\n" +
            "        \"image\": \"https://www.edamam.com/food-img/36b/36b69cbc3200dc7bd7de8224c2c87187.jpg\"\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"hints\": [\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_alxugipafrrbkaa7nha90a0xyzuz\",\n" +
            "        \"label\": \"Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 307,\n" +
            "          \"PROCNT\": 15.92,\n" +
            "          \"FAT\": 20.36,\n" +
            "          \"CHOCDF\": 14.93,\n" +
            "          \"FIBTG\": 0.9\n" +
            "        },\n" +
            "        \"category\": \"Generic foods\",\n" +
            "        \"categoryLabel\": \"food\",\n" +
            "        \"image\": \"https://www.edamam.com/food-img/36b/36b69cbc3200dc7bd7de8224c2c87187.jpg\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_piece\",\n" +
            "          \"label\": \"Piece\",\n" +
            "          \"weight\": 16\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 16\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_bpbsk7zaearxssa39q2fia84qudv\",\n" +
            "        \"label\": \"Fried Chicken Tenders\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 271,\n" +
            "          \"PROCNT\": 19.22,\n" +
            "          \"FAT\": 13.95,\n" +
            "          \"CHOCDF\": 17.25,\n" +
            "          \"FIBTG\": 1.2\n" +
            "        },\n" +
            "        \"category\": \"Generic foods\",\n" +
            "        \"categoryLabel\": \"food\",\n" +
            "        \"image\": \"https://www.edamam.com/food-img/cc3/cc359f770f8f4d9ab2eeb40cc7bf6768.jpg\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 30\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 184\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_strip\",\n" +
            "          \"label\": \"Strip\",\n" +
            "          \"weight\": 30\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_piece\",\n" +
            "          \"label\": \"Piece\",\n" +
            "          \"weight\": 15.375\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_a5ytvh0b13b00ebp3p4o0bdefng1\",\n" +
            "        \"label\": \"Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 264.07997125922844,\n" +
            "          \"PROCNT\": 11.869469820098798,\n" +
            "          \"FAT\": 19.703193913944062,\n" +
            "          \"CHOCDF\": 9.46584982080799,\n" +
            "          \"FIBTG\": 0.49494293495240904\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"fryer; buttermilk; flour; salt; pepper; poultry seasoning; cooking oil\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 338.4378537842224\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 338.4378537842224\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_auuoef9brfymexb09qbwkbytl6c1\",\n" +
            "        \"label\": \"Chicken Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 240.60413576935278,\n" +
            "          \"PROCNT\": 13.837249937842945,\n" +
            "          \"FAT\": 15.294612610411985,\n" +
            "          \"CHOCDF\": 11.392630796482868,\n" +
            "          \"FIBTG\": 0.4711057734005334\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"Buttermilk; Cayenne Pepper; Onion Powder; Parsley; Boneless, Skinless Chicken Breasts; Canola Oil; Flour; Salt; Pepper\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 286.84501866118615\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 286.84501866118615\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_bekkid4bmt3sajaalfw3ia3qqbk5\",\n" +
            "        \"label\": \"Fried Chicken Breast\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 303,\n" +
            "          \"PROCNT\": 21.91,\n" +
            "          \"FAT\": 18.11,\n" +
            "          \"CHOCDF\": 12.01\n" +
            "        },\n" +
            "        \"category\": \"Generic foods\",\n" +
            "        \"categoryLabel\": \"food\",\n" +
            "        \"image\": \"https://www.edamam.com/food-img/782/7820ce4e4145a2d3490378fba886554b.jpg\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 82\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_piece\",\n" +
            "          \"label\": \"Piece\",\n" +
            "          \"weight\": 81.5\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_bmjchvtb4f81oaapceua7a0mkyoh\",\n" +
            "        \"label\": \"Baked Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 221.86814410890844,\n" +
            "          \"PROCNT\": 13.385698084682272,\n" +
            "          \"FAT\": 13.093637699840347,\n" +
            "          \"CHOCDF\": 12.082641618786324,\n" +
            "          \"FIBTG\": 0.6331063612661763\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"chicken breast; buttermilk; panko breadcrumbs; flour; cornmeal; unsalted butter; salt; chili powder; garlic powder; onion powder; black pepper; smoked paprika; red pepper flakes\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 240.36354698156762\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 240.36354698156762\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_blstc9salfu68cak8owoeaqgaa75\",\n" +
            "        \"label\": \"Butter Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 245.58715107722477,\n" +
            "          \"PROCNT\": 13.622078027917098,\n" +
            "          \"FAT\": 17.00803834632769,\n" +
            "          \"CHOCDF\": 9.428236768792248,\n" +
            "          \"FIBTG\": 0.7998605737444366\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"boneless skinless chicken breast; flour; garlic powder; oregano; black pepper; salt; Light Butter; Vegetable Oil; olive oil\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 236.9460694820626\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 236.9460694820626\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_aybw9wbbu1u6zxb21ynnqbl0ob3i\",\n" +
            "        \"label\": \"Buttermilk Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 246.46701474453891,\n" +
            "          \"PROCNT\": 10.654556580080477,\n" +
            "          \"FAT\": 18.372404424902385,\n" +
            "          \"CHOCDF\": 9.43759668906086,\n" +
            "          \"FIBTG\": 0.4599040977366686\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"chicken; buttermilk; salt; pepper; all-purpose flour; Vegetable oil\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 390.9917801853646\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 390.9917801853646\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_bat9v0yanea6fmbgaf5m4by3kr8q\",\n" +
            "        \"label\": \"Cajun Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 314.5373564482418,\n" +
            "          \"PROCNT\": 11.487403418084176,\n" +
            "          \"FAT\": 25.257394169425528,\n" +
            "          \"CHOCDF\": 10.136861643505284,\n" +
            "          \"FIBTG\": 0.46312380813448456\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"Chicken Thighs; Salt; Pepper; Buttermilk; Hot Sauce; Egg; Flour; Cornstarch; Olive Oil; Onion Powder; Garlic Powder; Dried Oregano; Dried Basil; White Pepper; Cayenne Pepper; Paprika\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 382.56094193756354\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 382.56094193756354\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_awnd1m0aq7hpr3az8i4ygaqiolci\",\n" +
            "        \"label\": \"Country Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 260.3817811229198,\n" +
            "          \"PROCNT\": 13.935233592228963,\n" +
            "          \"FAT\": 19.034682163328466,\n" +
            "          \"CHOCDF\": 7.83500779264643,\n" +
            "          \"FIBTG\": 0.4378728822699625\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"all-purpose flour; garlic salt; pepper; paprika; poultry seasoning; egg; 2% milk; chicken thighs; chicken drumsticks; canola oil\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 444.4626021341864\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 444.4626021341864\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_a47djxha5cch2qbbdtxf2a5yg5oe\",\n" +
            "        \"label\": \"Fried Chicken Drumsticks\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 211.75106695358383,\n" +
            "          \"PROCNT\": 10.781744561003304,\n" +
            "          \"FAT\": 16.210910943730898,\n" +
            "          \"CHOCDF\": 5.628072994290695,\n" +
            "          \"FIBTG\": 0.44363398171169793\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"chicken drumsticks; egg; corn meal; bread crumbs; Salt; Pepper; water; Vegetable oil\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 223.9552614798361\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 223.9552614798361\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_bxzt49gaznabgibwd6z71bug0hsp\",\n" +
            "        \"label\": \"Filipino Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 273.6768722139015,\n" +
            "          \"PROCNT\": 13.070346067290387,\n" +
            "          \"FAT\": 22.96204902625582,\n" +
            "          \"CHOCDF\": 3.490894340346722,\n" +
            "          \"FIBTG\": 0.14806850280822295\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"chicken leg; lemon; garlic; salt; black pepper; cooking oil\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 194.5993990632437\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 194.5993990632437\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_ax5xfg4avhiktfa2qtv7yazv49jd\",\n" +
            "        \"label\": \"Garlic Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 269.25110166718633,\n" +
            "          \"PROCNT\": 13.355620952803735,\n" +
            "          \"FAT\": 18.939531422862533,\n" +
            "          \"CHOCDF\": 10.833665091147978,\n" +
            "          \"FIBTG\": 0.5190695144408938\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"egg; baking powder; garlic; olive oil; chicken; salt; pepper; all purpose flour; shortening\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 382.18403987344277\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 382.18403987344277\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_bxvs34xbor1u36ae4m53gaastewk\",\n" +
            "        \"label\": \"Fried Chicken Gizzards\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 156.75615505073492,\n" +
            "          \"PROCNT\": 11.18046819623034,\n" +
            "          \"FAT\": 11.188226785844835,\n" +
            "          \"CHOCDF\": 2.4763251620472846,\n" +
            "          \"FIBTG\": 0.21253681013461367\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"chicken gizzards; all-purpose flour; salt; black pepper; garlic powder; vegetable oil\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 365.785800976385\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 365.785800976385\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_awt4mmfbzwna4ab4riu1jassb9cc\",\n" +
            "        \"label\": \"Golden Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 260.6292154034667,\n" +
            "          \"PROCNT\": 14.283540684079215,\n" +
            "          \"FAT\": 18.102987943091183,\n" +
            "          \"CHOCDF\": 9.491324401200133,\n" +
            "          \"FIBTG\": 0.4233784615163857\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"butter; baking mix; paprika; salt; pepper; fryer chicken\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 153.7789769303598\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 153.7789769303598\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_aosu2zvbi59p5zb3j65jybz01oop\",\n" +
            "        \"label\": \"Honey Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 298.48490949182184,\n" +
            "          \"PROCNT\": 12.985228448489542,\n" +
            "          \"FAT\": 21.123032902887676,\n" +
            "          \"CHOCDF\": 14.28818765388138,\n" +
            "          \"FIBTG\": 0.4090125015278162\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"chicken; salt; pepper; honey; garlic powder; chicken bouillon granules; all-purpose flour; vegetable oil\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 228.13222979261602\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 228.13222979261602\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_alizn96bncc4e4aq951gqbwvnwnb\",\n" +
            "        \"label\": \"Italian Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 262.7212458568921,\n" +
            "          \"PROCNT\": 12.677777065067389,\n" +
            "          \"FAT\": 20.608283376059926,\n" +
            "          \"CHOCDF\": 6.503178781955934,\n" +
            "          \"FIBTG\": 0.5794413376032744\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"all-purpose flour; garlic; Kosher salt; pepper; milk; paprika; dried oregano; chicken breasts; fennel; romaine; parsley; extra-virgin olive oil; lemon; peanut oil\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 286.9885233963848\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 286.9885233963848\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_ai8rwv6a9vtqcrb7azuybavf1q0l\",\n" +
            "        \"label\": \"Kentucky Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 263.5208098833262,\n" +
            "          \"PROCNT\": 12.580474789174657,\n" +
            "          \"FAT\": 18.28716027839709,\n" +
            "          \"CHOCDF\": 12.089011675819604,\n" +
            "          \"FIBTG\": 1.1116567855435682\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"egg; milk; all-purpose flour; salt; black pepper; msg; cooking oil; chickens\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 127.25874335206954\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 127.25874335206954\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_bz3egdqa8r9qlrbuqijw7bg461yn\",\n" +
            "        \"label\": \"Keto Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 226.1206748186877,\n" +
            "          \"PROCNT\": 16.93279125416603,\n" +
            "          \"FAT\": 15.420696107531324,\n" +
            "          \"CHOCDF\": 4.882988677314613,\n" +
            "          \"FIBTG\": 0.9742637100942445\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"meat; Salt, table; Almond Flour; Cheese, parmesan, grated; Spices, pepper, black; Garlic; Spices, paprika; Egg, whole, raw, fresh; Milk; oil; Lemons\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 237.37938368376086\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 237.37938368376086\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"food\": {\n" +
            "        \"foodId\": \"food_amqopzsamvksghae9pfqvb0e2uv5\",\n" +
            "        \"label\": \"Korean Fried Chicken\",\n" +
            "        \"nutrients\": {\n" +
            "          \"ENERC_KCAL\": 239.28496064841178,\n" +
            "          \"PROCNT\": 11.425648500898362,\n" +
            "          \"FAT\": 16.635682953844906,\n" +
            "          \"CHOCDF\": 10.140784580816021,\n" +
            "          \"FIBTG\": 0.4663795234191187\n" +
            "        },\n" +
            "        \"category\": \"Generic meals\",\n" +
            "        \"categoryLabel\": \"meal\",\n" +
            "        \"foodContentsLabel\": \"chicken drumsticks; Kosher salt; pepper; cornstarch; baking powder; vegetable oil; garlic; fresh ginger; low-sodium soy sauce; dark brown sugar; honey; rice vinegar; toasted sesame oil; all-purpose flour; vodka; sesame seeds\"\n" +
            "      },\n" +
            "      \"measures\": [\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_unit\",\n" +
            "          \"label\": \"Whole\",\n" +
            "          \"weight\": 345.27495840598533\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_serving\",\n" +
            "          \"label\": \"Serving\",\n" +
            "          \"weight\": 345.27495840598533\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
            "          \"label\": \"Gram\",\n" +
            "          \"weight\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_ounce\",\n" +
            "          \"label\": \"Ounce\",\n" +
            "          \"weight\": 28.349523125\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_pound\",\n" +
            "          \"label\": \"Pound\",\n" +
            "          \"weight\": 453.59237\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram\",\n" +
            "          \"label\": \"Kilogram\",\n" +
            "          \"weight\": 1000\n" +
            "        },\n" +
            "        {\n" +
            "          \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_milliliter\",\n" +
            "          \"label\": \"Milliliter\",\n" +
            "          \"weight\": 1\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"_links\": {\n" +
            "    \"next\": {\n" +
            "      \"title\": \"Next page\",\n" +
            "      \"href\": \"https://api.edamam.com/api/food-database/v2/parser?session=40&app_id=7499d990&app_key=ea5a50092200159b99b878a969386d8f&ingr=fried+chicken&nutrition-type=cooking\"\n" +
            "    }\n" +
            "  }\n" +
            "}";


    String jsonFriedChickenSelect = "{\n" +
            "  \"uri\": \"http://www.edamam.com/ontologies/edamam.owl#0dc9b807-81c7-43ff-aefa-3d6b805fab6c\",\n" +
            "  \"calories\": 49,\n" +
            "  \"totalWeight\": 16,\n" +
            "  \"dietLabels\": [],\n" +
            "  \"healthLabels\": [\n" +
            "    \"KETO_FRIENDLY\",\n" +
            "    \"DAIRY_FREE\",\n" +
            "    \"EGG_FREE\",\n" +
            "    \"MILK_FREE\",\n" +
            "    \"PEANUT_FREE\",\n" +
            "    \"TREE_NUT_FREE\",\n" +
            "    \"SOY_FREE\",\n" +
            "    \"FISH_FREE\",\n" +
            "    \"SHELLFISH_FREE\",\n" +
            "    \"PORK_FREE\",\n" +
            "    \"RED_MEAT_FREE\",\n" +
            "    \"CRUSTACEAN_FREE\",\n" +
            "    \"CELERY_FREE\",\n" +
            "    \"MUSTARD_FREE\",\n" +
            "    \"SESAME_FREE\",\n" +
            "    \"LUPINE_FREE\",\n" +
            "    \"MOLLUSK_FREE\",\n" +
            "    \"ALCOHOL_FREE\",\n" +
            "    \"NO_SUGAR_ADDED\"\n" +
            "  ],\n" +
            "  \"cautions\": [],\n" +
            "  \"totalNutrients\": {\n" +
            "    \"ENERC_KCAL\": {\n" +
            "      \"label\": \"Energy\",\n" +
            "      \"quantity\": 49.120000000000005,\n" +
            "      \"unit\": \"kcal\"\n" +
            "    },\n" +
            "    \"FAT\": {\n" +
            "      \"label\": \"Total lipid (fat)\",\n" +
            "      \"quantity\": 3.2576,\n" +
            "      \"unit\": \"g\"\n" +
            "    },\n" +
            "    \"FASAT\": {\n" +
            "      \"label\": \"Fatty acids, total saturated\",\n" +
            "      \"quantity\": 0.57056,\n" +
            "      \"unit\": \"g\"\n" +
            "    },\n" +
            "    \"FATRN\": {\n" +
            "      \"label\": \"Fatty acids, total trans\",\n" +
            "      \"quantity\": 0.01968,\n" +
            "      \"unit\": \"g\"\n" +
            "    },\n" +
            "    \"FAMS\": {\n" +
            "      \"label\": \"Fatty acids, total monounsaturated\",\n" +
            "      \"quantity\": 1.2376,\n" +
            "      \"unit\": \"g\"\n" +
            "    },\n" +
            "    \"FAPU\": {\n" +
            "      \"label\": \"Fatty acids, total polyunsaturated\",\n" +
            "      \"quantity\": 1.04176,\n" +
            "      \"unit\": \"g\"\n" +
            "    },\n" +
            "    \"CHOCDF\": {\n" +
            "      \"label\": \"Carbohydrate, by difference\",\n" +
            "      \"quantity\": 2.3888,\n" +
            "      \"unit\": \"g\"\n" +
            "    },\n" +
            "    \"FIBTG\": {\n" +
            "      \"label\": \"Fiber, total dietary\",\n" +
            "      \"quantity\": 0.14400000000000002,\n" +
            "      \"unit\": \"g\"\n" +
            "    },\n" +
            "    \"SUGAR\": {\n" +
            "      \"label\": \"Sugars, total\",\n" +
            "      \"quantity\": 0.0128,\n" +
            "      \"unit\": \"g\"\n" +
            "    },\n" +
            "    \"PROCNT\": {\n" +
            "      \"label\": \"Protein\",\n" +
            "      \"quantity\": 2.5472,\n" +
            "      \"unit\": \"g\"\n" +
            "    },\n" +
            "    \"CHOLE\": {\n" +
            "      \"label\": \"Cholesterol\",\n" +
            "      \"quantity\": 8.8,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"NA\": {\n" +
            "      \"label\": \"Sodium, Na\",\n" +
            "      \"quantity\": 95.04,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"CA\": {\n" +
            "      \"label\": \"Calcium, Ca\",\n" +
            "      \"quantity\": 1.76,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"MG\": {\n" +
            "      \"label\": \"Magnesium, Mg\",\n" +
            "      \"quantity\": 3.84,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"K\": {\n" +
            "      \"label\": \"Potassium, K\",\n" +
            "      \"quantity\": 40.160000000000004,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"FE\": {\n" +
            "      \"label\": \"Iron, Fe\",\n" +
            "      \"quantity\": 0.1328,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"ZN\": {\n" +
            "      \"label\": \"Zinc, Zn\",\n" +
            "      \"quantity\": 0.0944,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"P\": {\n" +
            "      \"label\": \"Phosphorus, P\",\n" +
            "      \"quantity\": 43.52,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"VITA_RAE\": {\n" +
            "      \"label\": \"Vitamin A, RAE\",\n" +
            "      \"quantity\": 0.8,\n" +
            "      \"unit\": \"µg\"\n" +
            "    },\n" +
            "    \"VITC\": {\n" +
            "      \"label\": \"Vitamin C, total ascorbic acid\",\n" +
            "      \"quantity\": 0.096,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"THIA\": {\n" +
            "      \"label\": \"Thiamin\",\n" +
            "      \"quantity\": 0.01472,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"RIBF\": {\n" +
            "      \"label\": \"Riboflavin\",\n" +
            "      \"quantity\": 0.032639999999999995,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"NIA\": {\n" +
            "      \"label\": \"Niacin\",\n" +
            "      \"quantity\": 0.9576000000000001,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"VITB6A\": {\n" +
            "      \"label\": \"Vitamin B-6\",\n" +
            "      \"quantity\": 0.02368,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"FOLDFE\": {\n" +
            "      \"label\": \"Folate, DFE\",\n" +
            "      \"quantity\": 2.4,\n" +
            "      \"unit\": \"µg\"\n" +
            "    },\n" +
            "    \"FOLFD\": {\n" +
            "      \"label\": \"Folate, food\",\n" +
            "      \"quantity\": 0.96,\n" +
            "      \"unit\": \"µg\"\n" +
            "    },\n" +
            "    \"FOLAC\": {\n" +
            "      \"label\": \"Folic acid\",\n" +
            "      \"quantity\": 0.8,\n" +
            "      \"unit\": \"µg\"\n" +
            "    },\n" +
            "    \"VITB12\": {\n" +
            "      \"label\": \"Vitamin B-12\",\n" +
            "      \"quantity\": 0.05280000000000001,\n" +
            "      \"unit\": \"µg\"\n" +
            "    },\n" +
            "    \"VITD\": {\n" +
            "      \"label\": \"Vitamin D (D2 + D3)\",\n" +
            "      \"quantity\": 0.032,\n" +
            "      \"unit\": \"µg\"\n" +
            "    },\n" +
            "    \"TOCPHA\": {\n" +
            "      \"label\": \"Vitamin E (alpha-tocopherol)\",\n" +
            "      \"quantity\": 0.17920000000000003,\n" +
            "      \"unit\": \"mg\"\n" +
            "    },\n" +
            "    \"VITK1\": {\n" +
            "      \"label\": \"Vitamin K (phylloquinone)\",\n" +
            "      \"quantity\": 1.12,\n" +
            "      \"unit\": \"µg\"\n" +
            "    },\n" +
            "    \"WATER\": {\n" +
            "      \"label\": \"Water\",\n" +
            "      \"quantity\": 7.444800000000001,\n" +
            "      \"unit\": \"g\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"totalDaily\": {\n" +
            "    \"ENERC_KCAL\": {\n" +
            "      \"label\": \"Energy\",\n" +
            "      \"quantity\": 2.456,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"FAT\": {\n" +
            "      \"label\": \"Fat\",\n" +
            "      \"quantity\": 5.011692307692307,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"FASAT\": {\n" +
            "      \"label\": \"Saturated\",\n" +
            "      \"quantity\": 2.8528,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"CHOCDF\": {\n" +
            "      \"label\": \"Carbs\",\n" +
            "      \"quantity\": 0.7962666666666667,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"FIBTG\": {\n" +
            "      \"label\": \"Fiber\",\n" +
            "      \"quantity\": 0.5760000000000001,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"PROCNT\": {\n" +
            "      \"label\": \"Protein\",\n" +
            "      \"quantity\": 5.0944,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"CHOLE\": {\n" +
            "      \"label\": \"Cholesterol\",\n" +
            "      \"quantity\": 2.9333333333333336,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"NA\": {\n" +
            "      \"label\": \"Sodium\",\n" +
            "      \"quantity\": 3.96,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"CA\": {\n" +
            "      \"label\": \"Calcium\",\n" +
            "      \"quantity\": 0.176,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"MG\": {\n" +
            "      \"label\": \"Magnesium\",\n" +
            "      \"quantity\": 0.9142857142857143,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"K\": {\n" +
            "      \"label\": \"Potassium\",\n" +
            "      \"quantity\": 0.8544680851063831,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"FE\": {\n" +
            "      \"label\": \"Iron\",\n" +
            "      \"quantity\": 0.7377777777777778,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"ZN\": {\n" +
            "      \"label\": \"Zinc\",\n" +
            "      \"quantity\": 0.8581818181818182,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"P\": {\n" +
            "      \"label\": \"Phosphorus\",\n" +
            "      \"quantity\": 6.217142857142857,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"VITA_RAE\": {\n" +
            "      \"label\": \"Vitamin A\",\n" +
            "      \"quantity\": 0.08888888888888889,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"VITC\": {\n" +
            "      \"label\": \"Vitamin C\",\n" +
            "      \"quantity\": 0.10666666666666666,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"THIA\": {\n" +
            "      \"label\": \"Thiamin (B1)\",\n" +
            "      \"quantity\": 1.2266666666666668,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"RIBF\": {\n" +
            "      \"label\": \"Riboflavin (B2)\",\n" +
            "      \"quantity\": 2.51076923076923,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"NIA\": {\n" +
            "      \"label\": \"Niacin (B3)\",\n" +
            "      \"quantity\": 5.985,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"VITB6A\": {\n" +
            "      \"label\": \"Vitamin B6\",\n" +
            "      \"quantity\": 1.8215384615384613,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"FOLDFE\": {\n" +
            "      \"label\": \"Folate equivalent (total)\",\n" +
            "      \"quantity\": 0.6,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"VITB12\": {\n" +
            "      \"label\": \"Vitamin B12\",\n" +
            "      \"quantity\": 2.2,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"VITD\": {\n" +
            "      \"label\": \"Vitamin D\",\n" +
            "      \"quantity\": 0.21333333333333335,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"TOCPHA\": {\n" +
            "      \"label\": \"Vitamin E\",\n" +
            "      \"quantity\": 1.1946666666666668,\n" +
            "      \"unit\": \"%\"\n" +
            "    },\n" +
            "    \"VITK1\": {\n" +
            "      \"label\": \"Vitamin K\",\n" +
            "      \"quantity\": 0.9333333333333335,\n" +
            "      \"unit\": \"%\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"ingredients\": [\n" +
            "    {\n" +
            "      \"parsed\": [\n" +
            "        {\n" +
            "          \"quantity\": 1,\n" +
            "          \"measure\": \"piece\",\n" +
            "          \"food\": \"fried chicken\",\n" +
            "          \"foodId\": \"food_alxugipafrrbkaa7nha90a0xyzuz\",\n" +
            "          \"weight\": 16,\n" +
            "          \"retainedWeight\": 16,\n" +
            "          \"measureURI\": \"http://www.edamam.com/ontologies/edamam.owl#Measure_piece\",\n" +
            "          \"status\": \"OK\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";







}
