package project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class MaximumNutrientsStateTest {

    private MaximumNutrientsState maximumNutrientsState;
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

    private Observer observer;

    @BeforeEach
    public void setup(){

        this.foodDB = mock(FoodDB.class);
        this.user = new User("user");
        this.observer = mock(Observer.class);
        this.maximumNutrientsState = new MaximumNutrientsState(foodDB, user);

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

        maximumNutrientsState.subscribe(observer);

    }


    @Test
    /***
     * Negative case: Can't have negative maximums
     */
    public void setMaximumTotalNutrientValueNegativeQuantityTest(){

        IllegalArgumentException exception =  assertThrows(IllegalArgumentException.class, () ->
                maximumNutrientsState.setMaximumTotalNutrientValue("PROCNT", -1.0));


        assertEquals(exception.getMessage(), "ERROR: Cannot have a max quantity less than 0");

    }

    @Test
    /***
     * Edge case: Can have 0 maximums
     */
    public void setMaximumTotalNutrientValueZeroQuantityTest(){

        maximumNutrientsState.setMaximumTotalNutrientValue("PROCNT", 0.0);

        verify(observer).update();
        assertEquals(maximumNutrientsState.getMaximumTotalNutrients().getNutrientList().get("PROCNT").getQuantity(), 0);


    }


    @Test
    /***
     * Positive case: Positive quantity
     */
    public void setMaximumTotalNutrientValueValidQuantityTest(){

        maximumNutrientsState.setMaximumTotalNutrientValue("PROCNT", 28.0);

       verify(observer).update();
       assertEquals(maximumNutrientsState.getMaximumTotalNutrients().getNutrientList().get("PROCNT").getQuantity(), 28);


    }




}
