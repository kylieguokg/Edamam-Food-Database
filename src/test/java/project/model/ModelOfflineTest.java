package project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelOfflineTest{

    private Model model;
    private FoodDB foodDB;
    private Output output;


    @BeforeEach
    public void setup() {

        this.foodDB = mock(FoodDBDummy.class);
        this.output = mock(OutputDummy.class);

        this.model = new Model(foodDB, output);
    }


    @Test
    /***
     * Checks that once logged in the saved colour and saved food state is created
     */
    public void updateTest(){

        assertNull(model.getSavedColoursState());
        assertNull(model.getSavedFoodState());

        when (foodDB.login("user", "123")).thenReturn(true);

        model.getLoginState().login("user", "123");

        // should create them after logging in
        assertNotNull(model.getSavedColoursState());
        assertNotNull(model.getSavedFoodState());

    }



}