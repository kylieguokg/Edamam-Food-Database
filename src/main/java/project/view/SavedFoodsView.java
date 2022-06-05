package project.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import project.model.Food;
import project.presenter.SavedFoodPresenter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.util.Callback;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;


/***
 *  Displays the list of saved foods for a user
 */
public class SavedFoodsView implements View {

    private MainView mainView;
    private BorderPane borderPane;
    private ScrollPane scrollPane;
    private Scene scene;

    /***
     * Stores all the main nodes
     */
    private VBox mainBox;

    /***
     * Displays the foods in the saved list of foods
     */
    private TableView<Food> savedTable;



    private Label titleLBL;

    private ImageView maxNutrientsIV;
    /***
     * Responsible for mutating the Saved Foods View and Saved Foods state
     */
    private SavedFoodPresenter savedFoodPresenter;

    private Button outputBTN;
    private Button redditBTN;
    private Button maxNutrientsBTN;

    private HBox saveBTNBox;


    public SavedFoodsView(MainView mainView){
        this.mainView = mainView;
        this.borderPane = new BorderPane();
        this.scene = new Scene(borderPane, 1400, 800);
        scene.getStylesheets().add(mainView.getStyleSheet());

        mainBox = new VBox(30);
        mainBox.getStyleClass().add("vbox");
        mainBox.setAlignment(Pos.CENTER);

        scrollPane = new ScrollPane();
        scrollPane.setContent(mainBox);
        scrollPane.setPadding(new Insets(0, 50, 50, 50));
        borderPane.setCenter(scrollPane);
        scrollPane.fitToWidthProperty().set(true);

        titleLBL = new Label("Your Saved Foods");
        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);

        setUpTopBox();

        setUpSaveBTNBox();
    }

    public void setUpTopBox(){
        borderPane.setTop(mainView.getTopSP());
    }


    @Override
    public Scene getScene(){
        return scene;
    }


    public void setUpSaveBTNBox(){
        saveBTNBox = new HBox(20);
        saveBTNBox.setStyle("-fx-alignment: CENTER");

        ImageView outputIV = new ImageView("pastebin-logo.png");
        outputIV.setFitHeight(25.0);
        outputIV.setFitWidth(25.0);
        outputBTN = new Button("  Post to Pastebin");
        outputBTN.setGraphic(outputIV);
        outputBTN.setId("borderBTN");
        saveBTNBox.getChildren().add(outputBTN);
        mainView.setUpOutputBTN(outputBTN);

        ImageView redditIV = new ImageView("reddit-logo.png");
        redditIV.setFitHeight(25.0);
        redditIV.setFitWidth(25.0);
        redditBTN = new Button("  Post to Reddit");
        redditBTN.setGraphic(redditIV);
        redditBTN.setId("borderBTN");
        saveBTNBox.getChildren().add(redditBTN);
        mainView.setUpRedditBTN(redditBTN);

        maxNutrientsIV = new ImageView("dark-mode/white-nutrition.png");
        maxNutrientsIV.setFitHeight(25.0);
        maxNutrientsIV.setFitWidth(25.0);
        maxNutrientsBTN = new Button("  See Total Nutritional Information");
        maxNutrientsBTN.setGraphic(maxNutrientsIV);
        maxNutrientsBTN.setId("borderBTN");
        saveBTNBox.getChildren().add(maxNutrientsBTN);
        mainView.setUpMaxNutrientsBTN(maxNutrientsBTN);

        mainBox.getChildren().add(saveBTNBox);
    }


    /***
     * Sets up main elements of view
     */
    public void setUp(SavedFoodPresenter savedFoodPresenter){

        this.savedFoodPresenter = savedFoodPresenter;

        setUpSavedTable();
        showSavedTable();

    }



    /***
     * Sets up the saved ingredients table
     */
    public void setUpSavedTable(){

        savedTable = new TableView<Food>();
        mainBox.getChildren().add(savedTable);

        savedTable.setPlaceholder(new Label("No results"));
        savedTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Food, String> col1 = new TableColumn<>("Food ID");
        savedTable.getColumns().add(col1);
        col1.setMaxWidth(7000);
        col1.getStyleClass().add("column-border");
        col1.setCellValueFactory(new PropertyValueFactory<>("foodId"));

        TableColumn<Food, String> col2 = new TableColumn<>("Label");
        savedTable.getColumns().add(col2);
        col2.setMaxWidth(9000);
        col2.getStyleClass().add("column-border");
        col2.setCellValueFactory(new PropertyValueFactory<>("label"));

        TableColumn<Food, String> col3 = new TableColumn<>("Energy");
        savedTable.getColumns().add(col3);
        col3.setMaxWidth(2000);
        col3.getStyleClass().add("column-border");
        col3.setId("right-aligned");
        col3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Food, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Food, String> f) {
                Food food = f.getValue();
                return new SimpleStringProperty(savedFoodPresenter.getEnergyFormat(food));
            }
        });

        TableColumn<Food, String> col4= new TableColumn<>("Protein");
        savedTable.getColumns().add(col4);
        col4.setMaxWidth(2000);
        col4.getStyleClass().add("column-border");
        col4.setId("right-aligned");
        col4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Food, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Food, String> f) {
                Food food = f.getValue();
                return new SimpleStringProperty(savedFoodPresenter.getProteinFormat(food));
            }
        });

        TableColumn<Food, String> col5 = new TableColumn<>("Fat");
        savedTable.getColumns().add(col5);
        col5.setMaxWidth(2000);
        col5.getStyleClass().add("column-border");
        col5.setId("right-aligned");
        col5.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Food, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Food, String> f) {
                Food food = f.getValue();
                return new SimpleStringProperty(savedFoodPresenter.getFatFormat(food));
            }
        });

        TableColumn<Food, String> col6 = new TableColumn<>("Carbohydrate");
        savedTable.getColumns().add(col6);
        col6.setMaxWidth(3000);
        col6.getStyleClass().add("column-border");
        col6.setId("right-aligned");
        col6.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Food, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Food, String> f) {
                Food food = f.getValue();
                return new SimpleStringProperty(savedFoodPresenter.getCarbohydrateFormat(food));
            }
        });

        TableColumn<Food, String> col7= new TableColumn<>("Fibre");
        savedTable.getColumns().add(col7);
        col7.setMaxWidth(2500);
        col7.getStyleClass().add("column-border");
        col7.setId("right-aligned");
        col7.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Food, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Food, String> f) {
                Food food = f.getValue();
                return new SimpleStringProperty(savedFoodPresenter.getFibreFormat(food));
            }
        });

        TableColumn<Food, String> col8 = new TableColumn<>("Category\nLabel");
        savedTable.getColumns().add(col8);
        col8.setMaxWidth(2500);
        col8.getStyleClass().add("column-border");
        col8.setCellValueFactory(new PropertyValueFactory<>("categoryLabel"));

    }


    /***
     * Inserts data into saved table
     */
    public void showSavedTable(){

        // clear data first
       savedTable.getItems().clear();

        List<Food> results =  savedFoodPresenter.getSavedFoods();

        for (Food food : results) {
            savedTable.getItems().add(food);
        }
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

        if (darkMode){
            maxNutrientsIV.setImage(new Image("dark-mode/white-nutrition.png"));
        } else {
            maxNutrientsIV.setImage(new Image("light-mode/black-nutrition.png"));
        }

        // table colour
        savedTable.setStyle("-fx-border-color: " + tableColour);
        savedTable.lookup(".column-header-background ").setStyle("-fx-background-color: " + tableColour);
        savedTable.lookup(".column-header-background .filler").setStyle("-fx-background-color: " + tableColour);

        savedTable.lookupAll(".tree-table-view .column-header").stream().forEach((node) ->
        {
            node.setStyle("-fx-background-color: " + tableColour);
        });

        savedTable.lookupAll(".column-border").stream().forEach((node) ->
        {
            node.setStyle("-fx-border-color: " + tableColour);
        });

        savedTable.lookupAll(".label").forEach((node) ->
        {
            node.setStyle("-fx-text-fill: " + textColour);
        });

        savedTable.getColumns().stream().forEach((column) ->
        {
            column.setStyle("-fx-border-color: "+ tableColour + ";-fx-text-fill: " + textColour);
        });

        savedTable.lookupAll(".table-column").forEach((node) ->
        {
            node.setStyle("-fx-border-color: "+ tableColour + ";-fx-text-fill: " + textColour);
        });


        savedTable.getColumns().stream().forEach((column) ->
        {
            column.setStyle("-fx-border-color: "+ tableColour);
        });

        // button colour
        redditBTN.setStyle("-fx-border-color: " + buttonColour + "; -fx-text-fill: " + buttonColour);
        outputBTN.setStyle("-fx-border-color: " + buttonColour + "; -fx-text-fill: " + buttonColour);
        maxNutrientsBTN.setStyle("-fx-border-color: " + buttonColour + "; -fx-text-fill: " + buttonColour);


        // text colour
        titleLBL.setStyle("-fx-text-fill: " + textColour);


        // background colour
        scene.setFill(Paint.valueOf(backgroundColour));
    }



}

