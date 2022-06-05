package project.view;


import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import project.model.Food;
import project.model.Observer;
import project.presenter.SearchPresenter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 *   Displays the search interface for searching for a food in the database
 */
public class SearchView implements View {

    private MainView mainView;
    private BorderPane borderPane;
    private ScrollPane scrollPane;
    private Scene scene;

    /***
     * Stores all the main nodes
     */
    private VBox mainBox;

    /***
     * Displays the search results
     */
    private TableView<Food> searchTable;

    /***
     * Stores the navigation bar
     */
    private HBox topBox;

    private TextField searchTF;

    /***
     * Shows a message displaying if an operation was successful
     */
    private Label resultLBL;
    private Label searchLBL;
    private Label titleLBL;

    private Button confirmBTN;
    private Button searchBTN;

    /***
     * Responsible for mutating the search view and the search state
     */
    private SearchPresenter searchPresenter;

    private String textColour;

    private final ExecutorService pool = Executors.newFixedThreadPool(2, runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread ;
    });

    public SearchView(MainView mainView){
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



        titleLBL = new Label("Food and Grocery Database Search");
        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);


        resultLBL = new Label("");

        setUpTopBox();

    }


    @Override
    public Scene getScene(){
        return scene;
    }


    /***
     * Sets up all the main elements of view
     * @param searchPresenter
     */
    public void setUp(SearchPresenter searchPresenter){

        this.searchPresenter = searchPresenter;

        setUpSearch();
        setUpSearchResults();
        setUpConfirmBTN();

    }



    /***
     * Set up confirm button (selects a food)
     */
    public void setUpConfirmBTN(){

        SelectionModel selectionModel = searchTable.getSelectionModel();

        confirmBTN = new Button("Confirm");
        confirmBTN.setId("borderBTN");


        confirmBTN.setOnAction((ActionEvent e) -> {

            Object obj  = selectionModel.getSelectedItem();

            searchPresenter.selectedFood(obj, searchTF.getText());

        });

        mainBox.getChildren().addAll(confirmBTN, resultLBL);


    }

    /***
     * Set up the search inputs
     */
    public void setUpSearch(){

        searchLBL = new Label("Enter an ingredient: ");

        HBox searchBox = new HBox(20);
        searchTF = new TextField();
        searchTF.setId("searchTF");
        searchBTN = new Button("Search");
        searchBTN.setId("borderBTN");

        searchBox.getChildren().addAll(searchTF, searchBTN);
        searchBox.setAlignment(Pos.CENTER);

        searchBTN.setOnAction((ActionEvent e) -> {
            removeMSG();
            searchPresenter.search(searchTF.getText());
        });

        searchTF.setOnAction((ActionEvent e) -> {
            removeMSG();
            searchPresenter.search(searchTF.getText());
        });

        mainBox.getChildren().addAll(searchLBL, searchBox);

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
     * Removes msg from scrreen
     */
    public void removeMSG(){
        resultLBL.setText("");
        resultLBL.setId("gone");
    }

    /***
     * Clears table of data
     */
    public void clearTable(){
        searchTable.getItems().clear();
    }

    /***
     * Inserts the search results in the table
     */
    public void showSearchResults() {

        searchLBL.setText("Select an ingredient from the table below");

        // first clear the data
        searchTable.getItems().clear();

        List<Food> results = searchPresenter.getSearchResults();

        for (Food food : results) {
            searchTable.getItems().add(food);
        }


    }

    /***
     * Sets up the search results table
     */
    public void setUpSearchResults(){

        searchTable = new TableView<Food>();
        mainBox.getChildren().add(searchTable);

        searchTable.setPlaceholder(new Label("No results"));
        searchTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Food, String> col1 = new TableColumn<>("Food ID");
        searchTable.getColumns().add(col1);
        col1.setMaxWidth(7000);
        col1.getStyleClass().add("column-border");
        col1.setCellValueFactory(new PropertyValueFactory<>("foodId"));

        TableColumn<Food, String> col2 = new TableColumn<>("Label");
        searchTable.getColumns().add(col2);
        col2.setMaxWidth(9000);
        col2.getStyleClass().add("column-border");
        col2.setCellValueFactory(new PropertyValueFactory<>("label"));

        TableColumn<Food, String> col3 = new TableColumn<>("Energy");
        searchTable.getColumns().add(col3);
        col3.setMaxWidth(2000);
        col3.getStyleClass().add("column-border");
        col3.setId("right-aligned");
        col3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Food, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Food, String> f) {
                Food food = f.getValue();
                return new SimpleStringProperty(searchPresenter.getEnergyFormat(food));
            }
        });

        TableColumn<Food, String> col4= new TableColumn<>("Protein");
        searchTable.getColumns().add(col4);
        col4.setMaxWidth(2000);
        col4.getStyleClass().add("column-border");
        col4.setId("right-aligned");
        col4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Food, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Food, String> f) {
                Food food = f.getValue();
                return new SimpleStringProperty(searchPresenter.getProteinFormat(food));
            }
        });

        TableColumn<Food, String> col5 = new TableColumn<>("Fat");
        searchTable.getColumns().add(col5);
        col5.setMaxWidth(2000);
        col5.getStyleClass().add("column-border");
        col5.setId("right-aligned");
        col5.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Food, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Food, String> f) {
                Food food = f.getValue();
                return new SimpleStringProperty(searchPresenter.getFatFormat(food));
            }
        });

        TableColumn<Food, String> col6 = new TableColumn<>("Carbohydrate");
        searchTable.getColumns().add(col6);
        col6.setMaxWidth(3000);
        col6.getStyleClass().add("column-border");
        col6.setId("right-aligned");
        col6.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Food, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Food, String> f) {
                Food food = f.getValue();
                return new SimpleStringProperty(searchPresenter.getCarbohydrateFormat(food));
            }
        });

        TableColumn<Food, String> col7= new TableColumn<>("Fibre");
        searchTable.getColumns().add(col7);
        col7.setMaxWidth(2500);
        col7.getStyleClass().add("column-border");
        col7.setId("right-aligned");
        col7.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Food, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Food, String> f) {
                Food food = f.getValue();
                return new SimpleStringProperty(searchPresenter.getFibreFormat(food));
            }
        });

        TableColumn<Food, String> col8 = new TableColumn<>("Category\nLabel");
        searchTable.getColumns().add(col8);
        col8.setMaxWidth(2500);
        col8.getStyleClass().add("column-border");
        col8.setCellValueFactory(new PropertyValueFactory<>("categoryLabel"));

    }

    @Override
    public void setUpTopBox(){
        borderPane.setTop(mainView.getTopSP());
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

        this.textColour = textColour;
        // table colour
        searchTable.setStyle("-fx-border-color: " + tableColour);
        searchTable.lookup(".column-header-background ").setStyle("-fx-background-color: " + tableColour);
        searchTable.lookup(".column-header-background .filler").setStyle("-fx-background-color: " + tableColour);

        searchTable.lookupAll(".table-view .column-header").stream().forEach((node) ->
        {
            node.setStyle("-fx-background-color: " + tableColour);
        });

        searchTable.lookupAll(".column-border").stream().forEach((node) ->
        {
            node.setStyle("-fx-border-color: " + tableColour);
        });

        searchTable.lookupAll(".label").forEach((node) ->
        {
            node.setStyle("-fx-text-fill: " + textColour);
        });

        searchTable.getColumns().stream().forEach((column) ->
        {
            column.setStyle("-fx-border-color: "+ tableColour + ";-fx-text-fill: " + textColour);
        });

        searchTable.lookupAll(".table-column").forEach((node) ->
        {
            node.setStyle("-fx-border-color: "+ tableColour + ";-fx-text-fill: " + textColour);
        });

        // text colour
        titleLBL.setStyle("-fx-text-fill: " + textColour);
        searchLBL.setStyle("-fx-text-fill: " + textColour);


        // button colour
        confirmBTN.setStyle("-fx-border-color: " + buttonColour + "; -fx-text-fill: " + buttonColour);
        searchBTN.setStyle("-fx-border-color: " + buttonColour + "; -fx-text-fill: " + buttonColour);


        // background colour
        scene.setFill(Paint.valueOf(backgroundColour));

    }

}
