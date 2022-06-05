package project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.WeakHashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TotalNutrientsTest{


    @Test
    /***
     * Check that total nutrients is copied correctly
     */
    public void copyTest(){

        TotalNutrients totalNutrients = new TotalNutrients();

        totalNutrients.setQuantities(28.0);
        totalNutrients.setUnits("$$");

        for (Nutrient nutrient : (totalNutrients.copy()).getNutrientList().values()){
            assertEquals(nutrient.getQuantity(), 28.0);
            assertEquals(nutrient.getUnit(), "$$");
        }

    }


}