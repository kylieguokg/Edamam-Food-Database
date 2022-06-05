package project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


public class SearchStateTest {

    private FoodDBJsonParser foodDBJsonParser;

    private Food result1;
    private Measure measure1;
    private Nutrients nutrients1;

    private Food result2;
    private Measure measure2;
    private Nutrients nutrients2;
    private FoodDBHTTP foodDBHTTP;
    private FoodDBOnlineCache foodDBOnlineCache;

    private FoodDB foodDB;
    private User user;
    private SearchState searchState;
    private Observer observer;


    @BeforeEach
    public void setup() {


        this.foodDB = mock(FoodDB.class);

        this.searchState = new SearchState(foodDB);
        this.observer = mock(Observer.class);
        searchState.subscribe(observer);

        this.measure1 = new Measure("http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram", "Kilogram", 1000.0);
        this.nutrients1 = new Nutrients(261, 14.36, 15.42, 16.24, 0.7);
        this.result1 = new Food("food_bdxovpmabzq0y1b37dux7abl5muq",
                "Chicken Nugget",
                nutrients1,
                "Generic Goods",
                "food",
                "https://www.edamam.com/food-img/853/853b7c281a7108739b5b987fe290e60e.jpg",
                Arrays.asList(measure1));


        this.measure2 = new Measure("http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram", "Kilogram", 1000.0);
        this.nutrients2 = new Nutrients(261, 14.36, 15.42, 16.24, 0.7);
        this.result2 = new Food("food_bdxovpjskabzq0y1b37dux7abl5muq",
                "Fried Chicken",
                nutrients2,
                "Generic Goods",
                "food",
                "https://www.edamam.com/food-img/853/853b7c281a7108739b5b987fe290e60e.jpg",
                Arrays.asList(measure2));

        result1.setDietLabels(Arrays.asList("YUMMY"));
        result1.setHealthLabels(Arrays.asList("FUN"));
        result1.setTotalNutrients(new TotalNutrients());
        result1.getTotalNutrients().setQuantities(28.0);
        result1.setSelectedMeasure(measure1);

        result1.setTotalDaily(new TotalNutrients());
        result1.getTotalDaily().setQuantities(10.0);
        result1.getTotalDaily().setUnits("%");


    }


    @Test
    /***
     * Checks that the observer is updated if called
     */
    public void enterAnIngredientTest() {

        when(foodDB.enterAnIngredient("ingredient")).thenReturn("ok");
        searchState.enterAnIngredient("ingredient");

        // check that observer is updated
        verify(observer).update();
        // should been reset
        assertEquals(searchState.getSearchState(), null);

    }


    @Test
    /***
     * Check that the search results are correctly returned
     */
    public void searchIngredientTest() {


        List<Food> out = new ArrayList<Food>();
        out.add(result1);
        out.add(result2);

        when(foodDB.getSearchResults()).thenReturn(out);

        assertEquals(searchState.getSearchResults(), out);

    }


}