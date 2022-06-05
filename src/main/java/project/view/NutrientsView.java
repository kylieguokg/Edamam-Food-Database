package project.view;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import project.model.Food;
import project.model.Measure;
import project.model.Nutrient;
import project.presenter.NutrientsPresenter;

import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import project.view.NutrientsView;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

/***
 *   Displays the nutrition information for a food
 */
public class NutrientsView implements View {


    /***
     * Responsible for mutating the nutrients view and the nutrients state
     */
    private NutrientsPresenter nutrientsPresenter;

    private MainView mainView;
    private View backView;
    private BorderPane borderPane;
    private ScrollPane scrollPane;
    private Scene scene;

    /***
     * Stores all the main nodes
     */
    private VBox mainBox;

    /***
     * Displays the nutrition information for the food
     */
    private TreeTableView<Nutrient> nutrientsTable;

    /***
     * Displays any error messages or results
     */
    private Label resultLBL;

    private HBox measureBox;
    private ComboBox<String> selectMeasure;
    private Map<String, Measure> measures;
    private TextField quantityTF;


    private Label titleLBL;

    /***
     * Additional nutritional information
     */
    private VBox summaryBox;


    /***
     * Button for seeing nutritional information for a food
     */
    private Button nutritionBTN;

    /***
     * Button for saving the food
     */
    private Button saveFoodBTN;

    private Food selectedFood;
    private Measure selectedMeasure;
    private Integer selectedQuantity;

    private Label cautionLBL = new Label("Cautions: ");
    private Label healthLabelsLBL = new Label("Health Labels: ");
    private Label dietLabelsLBL = new Label("Diet Labels: ");
    private Label totalWeightNameLBL = new Label("Total Weight: ");
    private Label totalWeightLBL = new Label();
    private Label caloriesNameLBL = new Label("Calories: ");
    private Label caloriesLBL = new Label();
    private Label measureLBL;
    private Label quantityLBL;

    private FlowPane dietFP;
    private FlowPane cautionFP;
    private FlowPane healthFP;


    public NutrientsView(MainView mainView
    ){
        this.mainView = mainView;

        this.borderPane = new BorderPane();
        this.scene = new Scene(borderPane, 1400, 800);
        scene.getStylesheets().add(mainView.getStyleSheet());
    }

    @Override
    public Scene getScene(){
        return scene;
    }

    /***
     * Sets up all the main elements of view
     * @param nutrrientsPresenter
     */
    public void setUp(NutrientsPresenter nutrrientsPresenter){

        this.nutrientsPresenter = nutrrientsPresenter;

        mainBox = new VBox(30);
        mainBox.getStyleClass().add("vbox");
        mainBox.setAlignment(Pos.CENTER);

        scrollPane = new ScrollPane();
        scrollPane.setContent(mainBox);
        scrollPane.setPadding(new Insets(0, 50, 50, 50));
        scrollPane.fitToWidthProperty().set(true);
        borderPane.setCenter(scrollPane);

        titleLBL = new Label("Nutrition Information of " + nutrientsPresenter.getSelectedFoodLabel());
        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);


        measureBox = new HBox(20);
        measureBox.setAlignment(Pos.CENTER);
        mainBox.getChildren().add(measureBox);

        summaryBox = new VBox(10);

        mainBox.getChildren().add(summaryBox);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / 6);


        nutrientsTable = new TreeTableView<Nutrient>();
        mainBox.getChildren().addAll(nutrientsTable);

        setUpTopBox();

        dietFP = new FlowPane();
        cautionFP = new FlowPane();
        healthFP = new FlowPane();

        setUpAdjustMeasure();
        setUpNutrientsTable();
        setUpSaveFoodBTN();

    }



    /***
     * Sets up the input fields for measure and quantity
     */
    public void setUpAdjustMeasure(){

        measureLBL = new Label("Select a measure");
        selectMeasure = new ComboBox<String>();

        Food food = (Food) nutrientsPresenter.getSelectedFood();

        // Add all the possible measures for this food
        // to the dropdown measure field
        measures = new HashMap<String, Measure>();

        for (Measure measure: food.getMeasures()){
            measures.put(measure.toString(), measure);
            selectMeasure.getItems().add(measure.toString());
        }

        // Quantity text field
        quantityLBL = new Label("Input a quantity\n(integer only)");
        quantityLBL.setPadding(new Insets(0, 0, 0, 40));
        quantityTF = new TextField();

        // Stores any error messages
        resultLBL = new Label("");

        // Shows the nutritional information
        nutritionBTN = new Button("See nutritional information");
        setUpSeeNutritionalInfoBTN();

        measureBox.getChildren().addAll(measureLBL, selectMeasure, quantityLBL, quantityTF, nutritionBTN);
    }

    /***
     * Calls the show nutrititional information method
     */
    public void setUpSeeNutritionalInfoBTN(){

       nutritionBTN.setId("borderBTN");

        nutritionBTN.setOnAction((ActionEvent e) -> {
            removeMSG();

            try {
                nutrientsPresenter.seeNutritionalInformation(measures.get(selectMeasure.getValue()), quantityTF.getText());
            } catch (NumberFormatException n){
                displayErrorMSG(n.getMessage());
            } catch (IllegalArgumentException i){
                displayErrorMSG(i.getMessage());
            }

        });

        quantityTF.setOnAction((ActionEvent e) -> {
            removeMSG();

            try {
                nutrientsPresenter.seeNutritionalInformation(measures.get(selectMeasure.getValue()), quantityTF.getText());
            } catch (NumberFormatException n){
                displayErrorMSG(n.getMessage());
            } catch (IllegalArgumentException i){
                displayErrorMSG(i.getMessage());
            }

        });


    }

    /***
     * Displays error msg in red
     * @param msg to display
     */
    public void displayErrorMSG(String msg){
        resultLBL.setId("failure");
        resultLBL.setText(msg);
    }

    /***
     * Displays success msg in green
     * @param msg to display
     */
    public void displaySuccessMSG(String msg){
        resultLBL.setId("success");
        resultLBL.setText(msg);
    }

    /***
     * Sets up the extra non-domain specific aspects of view
     */
    public void setUpTopBox(){
        borderPane.setTop(mainView.getTopSP());
    }



    /***
     * Sets up the nutrients information table
     */
    public void setUpNutrientsTable(){

        nutrientsTable.setPlaceholder(new Label("Please select a measure and quantity"));
        nutrientsTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        // Stores the nutrient name
        TreeTableColumn<Nutrient, String> col0 = new TreeTableColumn<>("Nutrient");
        nutrientsTable.getColumns().add(col0);
        col0.setMaxWidth(10000);
        col0.getStyleClass().add("column-border");
        col0.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Nutrient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Nutrient, String> n) {

                Nutrient nutrient = n.getValue().getValue();

                return new SimpleStringProperty(nutrientsPresenter.formatNutrientSTR(nutrient));

            }
        });

        // Stores the total quantity of a nutrient
        TreeTableColumn<Nutrient, String> col1 = new TreeTableColumn<>("Quantity");
        nutrientsTable.getColumns().add(col1);
        col1.getStyleClass().add("column-border");
        col1.setId("right-aligned");
        col1.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Nutrient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Nutrient, String> n) {

                Nutrient nutrient = n.getValue().getValue();
                return new SimpleStringProperty(nutrientsPresenter.formatQuantitySTR(nutrient));

            }
        });


        // Stores the actual total quantity of list of saved foods
        TreeTableColumn<Nutrient, String> col2 = new TreeTableColumn<>("Running Total");
        nutrientsTable.getColumns().add(col2);
        col2.getStyleClass().add("column-border");
        col2.setId("right-aligned");
        col2.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Nutrient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Nutrient, String> n) {

                Nutrient nutrient = n.getValue().getValue();
                return new SimpleStringProperty(nutrientsPresenter.formatRunningTotalSTR(nutrient));

            }
        });

        // Stores the resulting total quantity of list of saved foods if nutrient was added
        TreeTableColumn<Nutrient, String> col3 = new TreeTableColumn<>("Resulting\nRunning Total");
        nutrientsTable.getColumns().add(col3);
        col3.getStyleClass().add("column-border");
        col3.setId("right-aligned");
        col3.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Nutrient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Nutrient, String> n) {

                Nutrient nutrient = n.getValue().getValue();
                return new SimpleStringProperty(nutrientsPresenter.formatResultingRunningTotalSTR(nutrient));

            }
        });

        // Stores the set maximum quantity of each nutrient
        TreeTableColumn<Nutrient, String> col4 = new TreeTableColumn<>("Maximum");
        nutrientsTable.getColumns().add(col4);
        col4.getStyleClass().add("column-border");
        col4.setId("right-aligned");
        col4.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Nutrient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Nutrient, String> n) {

                Nutrient nutrient = n.getValue().getValue();
                return new SimpleStringProperty(nutrientsPresenter.formatMaximumSTR(nutrient));

            }
        });

        // Stores the total nutrients of a food as a daily %
        TreeTableColumn<Nutrient, String> col5 = new TreeTableColumn<>("Daily (%)");
        nutrientsTable.getColumns().add(col5);
        col5.getStyleClass().add("column-border");
        col5.setId("right-aligned");
        col5.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Nutrient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Nutrient, String> n) {

                Nutrient nutrient = n.getValue().getValue();
                return new SimpleStringProperty(nutrientsPresenter.formatTotalDailySTR(nutrient));

            }
        });




    }


    /***
     * Removes msg from view
     */
    public void removeMSG(){
        resultLBL.setText("");
        resultLBL.setId("gone");
    }



    /***
     *  Summary box set up
     *  Calories, total weight, diet labels, health labels, cautions
     */
    public void showSummaryInformation(Food food){

        selectedFood = food;

        // clears the summary box info first
        summaryBox.setPadding(new Insets(0, 20, 0, 20));
        summaryBox.getChildren().clear();

        HBox caloriesBox = new HBox(10);
        caloriesLBL = new Label(selectedFood.getCalories() + "");
        caloriesNameLBL.setId("nutrients");
        caloriesBox.getChildren().add(caloriesNameLBL);
        caloriesBox.getChildren().add(caloriesLBL);
        summaryBox.getChildren().add(caloriesBox);

        HBox totalWeightBox = new HBox(10);
        totalWeightNameLBL.setId("nutrients");
        totalWeightLBL = new Label(selectedFood.getTotalWeight() + " g");
        totalWeightLBL.setTextAlignment(TextAlignment.LEFT);
        totalWeightBox.getChildren().add(totalWeightNameLBL);
        totalWeightBox.getChildren().add(totalWeightLBL);
        summaryBox.getChildren().add(totalWeightBox);


        // Diet labels
        HBox dietBox = new HBox(10);
        dietBox.setId("nutrientsBox");
        dietBox.getChildren().add(dietLabelsLBL);
        dietLabelsLBL.setId("nutrients");

        dietFP = new FlowPane();
        dietFP.setVgap(6);
        dietFP.setPrefWrapLength(1200);

        if (food.getDietLabels() == null || food.getDietLabels().size() == 0){
            Label newLabel = new Label("none");
            dietFP.getChildren().add(newLabel);
        } else {

            for (String diet : food.getDietLabels()){
                Label newLabel = new Label(diet + "  |  ");
                dietFP.getChildren().add(newLabel);
            }
        }

        dietBox.getChildren().addAll(dietFP);

        summaryBox.getChildren().add(dietBox);

        // Health labels
        HBox healthBox = new HBox(10);
        healthBox.setId("nutrientsBox");
        healthBox.getChildren().add(healthLabelsLBL);
        healthLabelsLBL.setId("nutrients");

        healthFP = new FlowPane();
        healthFP.setVgap(6);
        healthFP.setPrefWrapLength(1200);

        if (food.getHealthLabels() == null || food.getHealthLabels().size() == 0){
            Label newLabel = new Label("none");
            healthFP.getChildren().add(newLabel);
        } else {

            for (String health : food.getHealthLabels()){
                Label newLabel = new Label(health + "  |  ");
                healthFP.getChildren().add(newLabel);
            }
        }

        healthBox.getChildren().addAll(healthFP);
        summaryBox.getChildren().add(healthBox);

        // Cautions
        HBox cautionBox = new HBox(10);
        cautionBox.setId("nutrientsBox");
        cautionBox.getChildren().add(cautionLBL);
        cautionLBL.setId("nutrients");
        cautionFP = new FlowPane();
        cautionFP.setVgap(6);
        cautionFP.setPrefWrapLength(1200);

        if (food.getCautions() == null ||  food.getCautions().size() == 0){
            Label newLabel = new Label("none");
            cautionFP.getChildren().add(newLabel);
        } else {
            for (String caution : food.getCautions()){
                Label newLabel = new Label(caution + "  |  ");
                cautionFP.getChildren().add(newLabel);
            }
        }

        cautionBox.getChildren().add(cautionFP);
        summaryBox.getChildren().add(cautionBox);

    }


    public void refreshTable(){
        nutrientsTable.refresh();
    }

    /***
     *  Show nutrition information in table
     */
    public void showNutrientsTable(){

        // Store all the nutrients
        Map<String, Nutrient> nutrientMap = nutrientsPresenter.getRunningTotalNutrientsList();

        // Subcategory - Fat
        TreeItem<Nutrient> fat = new TreeItem<Nutrient>(new Nutrient("fats", "Fats"));
        for (String fatStr: nutrientsPresenter.getFatStrings()){
            fat.getChildren().add(new TreeItem<Nutrient>(nutrientMap.get(fatStr)));
        }

        // Subcategory - Carbs
        TreeItem<Nutrient> carbs = new TreeItem<Nutrient>(new Nutrient("carbs", "Carbs"));
        for (String carb: nutrientsPresenter.getCarbStrings()){
            carbs.getChildren().add(new TreeItem<Nutrient>(nutrientMap.get(carb)));
        }

        // Overall category
        TreeItem<Nutrient> nutrients = new TreeItem<Nutrient>(new Nutrient("nutrients", "Nutrients"));
        nutrients.setExpanded(true);
        nutrients.getChildren().add(fat);
        nutrients.getChildren().add(carbs);

        // Add the other nutrient types
        for (String nutrient: nutrientMap.keySet() ){

            if (nutrientsPresenter.getCarbStrings().contains(nutrient) || nutrientsPresenter.getFatStrings().contains(nutrient)){
                continue;
            }
            nutrients.getChildren().add(new TreeItem<Nutrient>(nutrientMap.get(nutrient)));
        }

        nutrientsTable.setRoot(nutrients);
    }

    /***
     * Shows the nutritional information for a food
     */
    public void showNutrientsInformation(Food selectedFood){

        titleLBL.setText("Nutrition Information of " + nutrientsPresenter.getSelectedFoodLabel() + " - "
                + selectMeasure.getValue());

        showNutrientsTable();
        showSummaryInformation(selectedFood);

    }

    /***
     * Sets up the button for saving an ingredient
     */
    public void setUpSaveFoodBTN(){

        saveFoodBTN = new Button(" Save Ingredient");
        ImageView saveIV = new ImageView(new Image("heart.png"));
        saveIV.setFitHeight(20.0);
        saveIV.setFitWidth(20.0);
        saveFoodBTN.setGraphic(saveIV);
        saveFoodBTN.setId("borderBTN");

        saveFoodBTN.setOnAction((ActionEvent e) -> {
            removeMSG();

            nutrientsPresenter.saveFood(measures.get(selectMeasure.getValue()), quantityTF.getText());
        });

        mainBox.getChildren().addAll(saveFoodBTN, resultLBL);
    }

    @Override
    public void openSettings(VBox settingsMenu){
        borderPane.setRight(settingsMenu);

    }


    @Override
    public void closeSettings(VBox settingsMenu){
        borderPane.setRight(null);

    }


    @Override
    public void setColours(boolean darkMode, String backgroundColour, String buttonColour, String textColour,
                           String tableColour, String quantityColour,
                           String maxQuantityColour){


        // table colours
        nutrientsTable.setStyle("-fx-border-color: " + tableColour);
        nutrientsTable.lookup(".column-header-background ").setStyle("-fx-background-color: " + tableColour);
        nutrientsTable.lookup(".column-header-background .filler").setStyle("-fx-background-color: " + tableColour);

        nutrientsTable.lookupAll(".tree-table-view .column-header").stream().forEach((node) ->
        {
            node.setStyle("-fx-background-color: " + tableColour);
        });

        nutrientsTable.lookupAll(".column-border").stream().forEach((node) ->
        {
            node.setStyle("-fx-border-color: " + tableColour);
        });

        nutrientsTable.getColumns().stream().forEach((column) ->
        {
            column.setStyle("-fx-border-color: "+ tableColour);
        });

        nutrientsTable.lookupAll(".label").forEach((node) ->
        {
            node.setStyle("-fx-text-fill: " + textColour);
        });

        nutrientsTable.getColumns().stream().forEach((column) ->
        {
            column.setStyle("-fx-border-color: "+ tableColour + ";-fx-text-fill: " + textColour);
        });

        nutrientsTable.lookupAll(".tree-table-column").forEach((node) ->
        {
            node.setStyle("-fx-border-color: "+ tableColour + ";-fx-text-fill: " + textColour);
        });

        // text
        titleLBL.setStyle("-fx-text-fill: " + textColour);
        cautionLBL.setStyle("-fx-text-fill: " + textColour);
        healthLabelsLBL.setStyle("-fx-text-fill: " + textColour);
        dietLabelsLBL.setStyle("-fx-text-fill: " + textColour);
        totalWeightLBL.setStyle("-fx-text-fill: " + textColour);
        totalWeightNameLBL.setStyle("-fx-text-fill: " + textColour);
        caloriesLBL.setStyle("-fx-text-fill: " + textColour);
        caloriesNameLBL.setStyle("-fx-text-fill: " + textColour);
        measureLBL.setStyle("-fx-text-fill: " + textColour);
        quantityLBL.setStyle("-fx-text-fill: " + textColour);

        dietFP.getChildren().forEach((node) -> {
            node.setStyle("-fx-text-fill: " + textColour);
        });

        cautionFP.getChildren().forEach((node) -> {
            node.setStyle("-fx-text-fill: " + textColour);
        });

        healthFP.getChildren().forEach((node) -> {
            node.setStyle("-fx-text-fill: " + textColour);
        });

       // buttons

        saveFoodBTN.setStyle("-fx-border-color: " + buttonColour + "; -fx-text-fill: " + buttonColour);
        nutritionBTN.setStyle("-fx-border-color: " + buttonColour + "; -fx-text-fill: " + buttonColour);

        // background
        scene.setFill(Paint.valueOf(backgroundColour));

        refreshTable();

    }

    /***
     * Sets up an alert to ask user if they want to
     * use the cache version of the data or not
     */
    public void setUpCacheHitAlert(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setContentText("Cache hit for this data – use cache, or request fresh data from the API?");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

        alert.getDialogPane().setStyle(" -fx-font-family: 'Comfortaa';");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            nutrientsPresenter.modelSelectAFood(true);

        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {

            // HTTP CALL
            nutrientsPresenter.modelSelectAFood(false);
        } else {

        }
    }



}
