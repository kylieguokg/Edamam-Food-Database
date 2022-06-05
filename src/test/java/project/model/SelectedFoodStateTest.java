package project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SelectedFoodStateTest {

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
    private SelectedFoodState selectedFoodState;

    private Observer observer;

    @BeforeEach
    public void setup() {


        this.foodDB = mock(FoodDB.class);
        this.observer = mock(Observer.class);

        this.selectedFoodState = new SelectedFoodState(foodDB);
        selectedFoodState.subscribe(observer);

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
     * Check that the food is selected correctly and that the observer is notified
     */
    public void selectAFoodTest() {

        TotalNutrients runningTotalsTotalNutrients = new TotalNutrients();
        TotalNutrients runningTotalsTotalDaily = new TotalNutrients();

        when(foodDB.selectAFood(2, measure1, result1, true)).thenReturn("ok");
        when(foodDB.getSelectedFood()).thenReturn(result1);

        selectedFoodState.selectAFood(2, measure1, result1, true, runningTotalsTotalNutrients, runningTotalsTotalDaily);

        verify(observer).update();

        assertTrue(selectedFoodState.getSelectedFood().equals(result1));

        // check it has been reset
        assertNull(selectedFoodState.getSelectedFoodState());

    }


    @Test
    /***
     * Check that the select state is updated correctly if no food is found
     * and that the observer is notified
     */
    public void selectAFoodNullTest() {

        TotalNutrients runningTotalsTotalNutrients = new TotalNutrients();
        TotalNutrients runningTotalsTotalDaily = new TotalNutrients();

        when(foodDB.selectAFood(2, measure1, result1, true)).thenReturn("ok");
        when(foodDB.getSelectedFood()).thenReturn(null); // no food available

        selectedFoodState.selectAFood(2, measure1, result1, true, runningTotalsTotalNutrients, runningTotalsTotalDaily);


        verify(observer).update();

        assertFalse(selectedFoodState.isCachedVersionAvailable());

        // should been reset
        assertNull(selectedFoodState.getCachedState());

    }

    @Test
    /***
     * Checks that if there is a cached version available,
     * selected food state returns true and the observer is notified
     */
    public void checkIfCachedVersionTest(){


        when(foodDB.checkIfCachedVersion(measure1, result1, 1)).thenReturn(true);

        selectedFoodState.checkIfCachedVersion(measure1, result1, 1);

        verify(observer).update();

        assertTrue(selectedFoodState.isCachedVersionAvailable());

        // should been reset
        assertNull(selectedFoodState.getCachedState());
    }



}