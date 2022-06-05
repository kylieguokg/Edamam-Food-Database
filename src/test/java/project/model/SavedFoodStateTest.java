package project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


public class SavedFoodStateTest {

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
    private SavedFoodState savedFoodState;

    private Observer observer;


    @BeforeEach
    public void setup() {


        this.foodDB = mock(FoodDB.class);
        this.user = new User("user");
        this.observer = mock(Observer.class);
        this.savedFoodState = new SavedFoodState(foodDB, user);
        savedFoodState.subscribe(observer);

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
     * Check that the food has been saved to the state and that an update notification is sent to the observer
     */
    public void saveFoodTest() {

        when(foodDB.saveFood(result1)).thenReturn("ok");
        savedFoodState.saveFood(result1);

        assertEquals(savedFoodState.getCurSavedFood(), result1);

        // should been reset
        verify(observer).update();
        assertNull(savedFoodState.getSavedFoodState());

    }

    @Test
    /***
     * Checks that the correct output is returned if 1 food was saved (tests save food within)
     */
    public void outputTest(){

        String expected = "\n" +
                "### List of Selected Ingredients ###\n" +
                "\n" +
                "FoodId: food_bdxovpmabzq0y1b37dux7abl5muq | Label: Chicken Nugget\n" +
                "Energy: 261.0 | Protein: 14.36\n" +
                "Fat: 15.42 | Carbohydrate, by difference: 16.24\n" +
                "Fibre, total dietary: 0.7\n" +
                "Category: Generic Goods | Category Label: food\n" +
                "Image: https://www.edamam.com/food-img/853/853b7c281a7108739b5b987fe290e60e.jpg\n" +
                "Selected Measure (Quantity): Kilogram (1000.0g)\n" +
                "\n" +
                "\n" +
                "### Total Nutritional Value ###\n" +
                "\n" +
                "Nutrient: ENERC_KCAL | Label: Energy | Quantity: 28.0 | Unit: kcal\n" +
                "Nutrient: FAT | Label: Fat | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: FASAT | Label: Saturated | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: FATRN | Label: Trans | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: FAMS | Label: Monounsaturated | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: FAPU | Label: Polyunsaturated | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: CHOCDF | Label: Carbohydrate, by difference | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: FIBTG | Label: Fibre | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: SUGAR | Label: Sugars | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: PROCNT | Label: Protein | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: CHOLE | Label: Cholesterol | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: NA | Label: Sodium | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: CA | Label: Calcium | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: MG | Label: Magnesium | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: K | Label: Potassium | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: FE | Label: Iron | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: ZN | Label: Zinc | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: P | Label: Phosphorus | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: VITA_RAE | Label: Vitamin A | Quantity: 28.0 | Unit: µg\n" +
                "Nutrient: VITC | Label: Vitamin C | Quantity: 28.0 | Unit: µg\n" +
                "Nutrient: THIA | Label: Thiamin | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: RIBF | Label: Riboflavin | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: NIA | Label: Niacin | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: VITB6A | Label: Vitamin B-6 | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: FOLDFE | Label: Folate, DFE | Quantity: 28.0 | Unit: µg\n" +
                "Nutrient: FOLFD | Label: Folate, food | Quantity: 28.0 | Unit: µg\n" +
                "Nutrient: FOLAC | Label: Folic acid | Quantity: 28.0 | Unit: µg\n" +
                "Nutrient: VITB12 | Label: Vitamin B-12 | Quantity: 28.0 | Unit: µg\n" +
                "Nutrient: VITD | Label: Vitamin D (D2 + D3) | Quantity: 28.0 | Unit: µg\n" +
                "Nutrient: TOCPHA | Label: Vitamin E (alpha-tocopherol) | Quantity: 28.0 | Unit: mg\n" +
                "Nutrient: VITK1 | Label: Vitamin K (phylloquinone) | Quantity: 28.0 | Unit: µg\n" +
                "Nutrient: WATER | Label: Water | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: SUGAR.added | Label: Sugar added | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: CHOCDF.net | Label: Carbohydrate (net) | Quantity: 28.0 | Unit: g\n" +
                "Nutrient: Sugar.alcohol | Label: Sugar alcohols | Quantity: 28.0 | Unit: g\n" +
                "\n" +
                "### Total Daily Nutritional Value ###\n" +
                "\n" +
                "Nutrient: ENERC_KCAL | Label: Energy | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: FAT | Label: Fat | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: FASAT | Label: Saturated | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: FATRN | Label: Trans | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: FAMS | Label: Monounsaturated | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: FAPU | Label: Polyunsaturated | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: CHOCDF | Label: Carbohydrate, by difference | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: FIBTG | Label: Fibre | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: SUGAR | Label: Sugars | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: PROCNT | Label: Protein | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: CHOLE | Label: Cholesterol | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: NA | Label: Sodium | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: CA | Label: Calcium | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: MG | Label: Magnesium | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: K | Label: Potassium | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: FE | Label: Iron | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: ZN | Label: Zinc | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: P | Label: Phosphorus | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: VITA_RAE | Label: Vitamin A | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: VITC | Label: Vitamin C | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: THIA | Label: Thiamin | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: RIBF | Label: Riboflavin | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: NIA | Label: Niacin | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: VITB6A | Label: Vitamin B-6 | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: FOLDFE | Label: Folate, DFE | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: FOLFD | Label: Folate, food | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: FOLAC | Label: Folic acid | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: VITB12 | Label: Vitamin B-12 | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: VITD | Label: Vitamin D (D2 + D3) | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: TOCPHA | Label: Vitamin E (alpha-tocopherol) | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: VITK1 | Label: Vitamin K (phylloquinone) | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: WATER | Label: Water | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: SUGAR.added | Label: Sugar added | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: CHOCDF.net | Label: Carbohydrate (net) | Quantity: 10.0 | Unit: %\n" +
                "Nutrient: Sugar.alcohol | Label: Sugar alcohols | Quantity: 10.0 | Unit: %\n";


        List<Food> in = new ArrayList<Food>();

        in.add(result1);

        // save result 1
        savedFoodState.saveFood(result1);

        when(foodDB.getSavedFoods()).thenReturn(in);

        assertEquals(savedFoodState.getOutputText(), expected);

    }


    @Test
    /***
     * Checks that the correct output is returned if no foods were added
     */
    public void outputNothingSavedTest() {

        String expected = "\n" +
                "### List of Selected Ingredients ###\n" +
                "\n" +
                "\n" +
                "### Total Nutritional Value ###\n" +
                "\n" +
                "Nutrient: ENERC_KCAL | Label: Energy | Quantity: 0.0 | Unit: kcal\n" +
                "Nutrient: FAT | Label: Fat | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: FASAT | Label: Saturated | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: FATRN | Label: Trans | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: FAMS | Label: Monounsaturated | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: FAPU | Label: Polyunsaturated | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: CHOCDF | Label: Carbohydrate, by difference | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: FIBTG | Label: Fibre | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: SUGAR | Label: Sugars | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: PROCNT | Label: Protein | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: CHOLE | Label: Cholesterol | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: NA | Label: Sodium | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: CA | Label: Calcium | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: MG | Label: Magnesium | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: K | Label: Potassium | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: FE | Label: Iron | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: ZN | Label: Zinc | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: P | Label: Phosphorus | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: VITA_RAE | Label: Vitamin A | Quantity: 0.0 | Unit: µg\n" +
                "Nutrient: VITC | Label: Vitamin C | Quantity: 0.0 | Unit: µg\n" +
                "Nutrient: THIA | Label: Thiamin | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: RIBF | Label: Riboflavin | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: NIA | Label: Niacin | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: VITB6A | Label: Vitamin B-6 | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: FOLDFE | Label: Folate, DFE | Quantity: 0.0 | Unit: µg\n" +
                "Nutrient: FOLFD | Label: Folate, food | Quantity: 0.0 | Unit: µg\n" +
                "Nutrient: FOLAC | Label: Folic acid | Quantity: 0.0 | Unit: µg\n" +
                "Nutrient: VITB12 | Label: Vitamin B-12 | Quantity: 0.0 | Unit: µg\n" +
                "Nutrient: VITD | Label: Vitamin D (D2 + D3) | Quantity: 0.0 | Unit: µg\n" +
                "Nutrient: TOCPHA | Label: Vitamin E (alpha-tocopherol) | Quantity: 0.0 | Unit: mg\n" +
                "Nutrient: VITK1 | Label: Vitamin K (phylloquinone) | Quantity: 0.0 | Unit: µg\n" +
                "Nutrient: WATER | Label: Water | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: SUGAR.added | Label: Sugar added | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: CHOCDF.net | Label: Carbohydrate (net) | Quantity: 0.0 | Unit: g\n" +
                "Nutrient: Sugar.alcohol | Label: Sugar alcohols | Quantity: 0.0 | Unit: g\n" +
                "\n" +
                "### Total Daily Nutritional Value ###\n" +
                "\n" +
                "Nutrient: ENERC_KCAL | Label: Energy | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: FAT | Label: Fat | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: FASAT | Label: Saturated | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: FATRN | Label: Trans | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: FAMS | Label: Monounsaturated | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: FAPU | Label: Polyunsaturated | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: CHOCDF | Label: Carbohydrate, by difference | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: FIBTG | Label: Fibre | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: SUGAR | Label: Sugars | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: PROCNT | Label: Protein | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: CHOLE | Label: Cholesterol | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: NA | Label: Sodium | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: CA | Label: Calcium | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: MG | Label: Magnesium | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: K | Label: Potassium | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: FE | Label: Iron | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: ZN | Label: Zinc | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: P | Label: Phosphorus | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: VITA_RAE | Label: Vitamin A | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: VITC | Label: Vitamin C | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: THIA | Label: Thiamin | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: RIBF | Label: Riboflavin | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: NIA | Label: Niacin | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: VITB6A | Label: Vitamin B-6 | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: FOLDFE | Label: Folate, DFE | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: FOLFD | Label: Folate, food | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: FOLAC | Label: Folic acid | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: VITB12 | Label: Vitamin B-12 | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: VITD | Label: Vitamin D (D2 + D3) | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: TOCPHA | Label: Vitamin E (alpha-tocopherol) | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: VITK1 | Label: Vitamin K (phylloquinone) | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: WATER | Label: Water | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: SUGAR.added | Label: Sugar added | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: CHOCDF.net | Label: Carbohydrate (net) | Quantity: 0.0 | Unit: %\n" +
                "Nutrient: Sugar.alcohol | Label: Sugar alcohols | Quantity: 0.0 | Unit: %\n";


        List<Food> in = new ArrayList<Food>();

        // save result 1
        when(foodDB.getSavedFoods()).thenReturn(in);

        assertEquals(savedFoodState.getOutputText(), expected);

    }




}