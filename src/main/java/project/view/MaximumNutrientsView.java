package project.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import project.model.Nutrient;
import javafx.beans.property.SimpleStringProperty;
import project.presenter.MaximumNutrientsPresenter;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import java.util.Map;



/***
 *   Displays the running total nutrition
 *   information for a user's saved list of foods
 */
public class MaximumNutrientsView implements View {

    private MainView mainView;
    private BorderPane borderPane;
    private ScrollPane scrollPane;
    private Scene scene;

    /***
     * Stores all the visible nodes
     */
    private VBox mainBox;

    /***
     * Displays the totals of the nutrients and the maximum of the nutrients
     */
    private TreeTableView<Nutrient> nutrientsTable;

    /***
     * Displays a % stacked bar chart comparing each nutritional value
     * against its set maximum
     */
    private StackedBarChart<Number, String> nutrientStringStackedBarChart;

    /***
     * Stores the nutrient names
     */
    private CategoryAxis categoryAxis;

    /***
     * Stores the percentages for each nutrient
     */
    private NumberAxis numberAxis;


    private Label titleLBL;
    private Label editLBL;


    /***
     * Manages the model logic for the total nutrients for the user's list of saved foods
     */
    private MaximumNutrientsPresenter maximumNutrientsPresenter;


    public MaximumNutrientsView(MainView mainView){
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

        titleLBL = new Label("Nutrition Information for Saved Foods");
        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);

        editLBL = new Label("Click on the cell to edit the maximum quantity");
        mainBox.getChildren().add(editLBL);

        nutrientsTable = new TreeTableView<Nutrient>();
        mainBox.getChildren().add(nutrientsTable);

        // y axis has the nutrient names
        categoryAxis = new CategoryAxis();
        categoryAxis.setLabel("Nutrients");

        // x axis has the quantities as a %
        numberAxis = new NumberAxis();
        numberAxis.setLabel("Quantities (%)");

        nutrientStringStackedBarChart = new StackedBarChart<Number, String>(numberAxis, categoryAxis);
        mainBox.getChildren().add(nutrientStringStackedBarChart);

        setUpTopBox();

    }

    @Override
    public Scene getScene(){
        return scene;
    }

    /***
     * Sets up the main elements of the view
     */
    public void setUp(MaximumNutrientsPresenter maximumNutrientsPresenter){

        this.maximumNutrientsPresenter = maximumNutrientsPresenter;

        setUpNutrientsTable();
        showNutrientsTable();

        setUpStackedBarChart();
        showStackedBarChart();

    }


    /***
     * Sets up the top navigation bar
     */
    public void setUpTopBox(){
        mainView.setUpTopBox();
        borderPane.setTop(mainView.getTopSP());
    }


    /***
     * Sets up a % stacked bar chart comparing each nutritional value
     * against its set maximum
     */
    public void setUpStackedBarChart(){

        nutrientStringStackedBarChart.setTitle("Total Quantity out of Maximum Quantity (%)");
        nutrientStringStackedBarChart.setMinSize(1000, 800);
        nutrientStringStackedBarChart.setLegendVisible(false);

    }

    /***
     * Sets up the data for stacked bar chart of quantitiyes
     */
    public void showStackedBarChart(){

        // clear data first
        nutrientStringStackedBarChart.getData().clear();

        // Sections for each bar
        XYChart.Series<Number, String> quantitySeries = new XYChart.Series<>();
        quantitySeries.setName("Quantity");

        XYChart.Series<Number, String> maxQuantitySeries = new XYChart.Series<>();
        maxQuantitySeries.setName("Maximum Quantity");

        nutrientStringStackedBarChart.getData().add(quantitySeries);
        nutrientStringStackedBarChart.getData().add(maxQuantitySeries);

        maximumNutrientsPresenter.setUpStackedBarChart();

        for (String nutrient: maximumNutrientsPresenter.getQuantityPercents().keySet()){

            Double percent = maximumNutrientsPresenter.getQuantityPercents().get(nutrient);
            Double maxPercent = maximumNutrientsPresenter.getMaxQuantityPercents().get(nutrient);

            quantitySeries.getData().add(new XYChart.Data<Number, String>(percent, nutrient));
            maxQuantitySeries.getData().add(new XYChart.Data<Number, String>(maxPercent, nutrient));
        }

    }


    /***
     * Sets up a table to store the totals of the nutrients
     * and the maximum of the nutrients
     */
    public void setUpNutrientsTable(){

        nutrientsTable.setEditable(true);
        nutrientsTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        // Stores the nutrient name
        TreeTableColumn<Nutrient, String> col0 = new TreeTableColumn<>("Nutrient");
        nutrientsTable.getColumns().add(col0);
        col0.setMaxWidth(7000);
        col0.setEditable(false);
        col0.getStyleClass().add("column-border");
        col0.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Nutrient,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Nutrient,String> n) {

                Nutrient nutrient = n.getValue().getValue();
                return new SimpleStringProperty(maximumNutrientsPresenter.formatNutrientSTR(nutrient));

            }
        });

        // Stores the running total quantity of a nutrient
        TreeTableColumn<Nutrient, String> col1 = new TreeTableColumn<>("Total Quantity");
        nutrientsTable.getColumns().add(col1);
        col1.setMaxWidth(7000);
        col1.getStyleClass().add("column-border");
        col1.setId("right-aligned");
        col1.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Nutrient,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Nutrient,String> n) {

                Nutrient nutrient = n.getValue().getValue();
                return new SimpleStringProperty(maximumNutrientsPresenter.formatRunningTotalSTR(nutrient));
            }
        });


        // Stores the total daily quantity (%) of a nutrient
        TreeTableColumn<Nutrient, String> col2 = new TreeTableColumn<>("Total Daily Quantity (%)");
        nutrientsTable.getColumns().add(col2);
        col2.setMaxWidth(7000);
        col2.getStyleClass().add("column-border");
        col2.setId("right-aligned");
        col2.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Nutrient,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Nutrient,String> n) {

                Nutrient nutrient = n.getValue().getValue();
                return new SimpleStringProperty(maximumNutrientsPresenter.formatRunningTotalDailySTR(nutrient));
            }
        });

        // Stores the maximum quantity of a nutrient (and is editable)
        TreeTableColumn<Nutrient, String> col3 = new TreeTableColumn<>("Maximum Quantity");
        nutrientsTable.getColumns().add(col3);
        col3.setMaxWidth(7000);
        col3.setEditable(true);
        col3.getStyleClass().add("column-border");
        col3.setId("right-aligned");
        col3.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Nutrient,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Nutrient,String> n) {

                Nutrient nutrient = n.getValue().getValue();
                return new SimpleStringProperty(maximumNutrientsPresenter.formatMaximumSTR(nutrient));

            }
        });
        col3.setCellFactory(TextFieldTreeTableCell.<Nutrient>forTreeTableColumn()); // makes it editable
        col3.setOnEditCommit( e -> {

            Nutrient nutrient = (Nutrient) e.getRowValue().getValue();
            String quantity = (String) e.getNewValue();
            maximumNutrientsPresenter.changeQuantity(nutrient, quantity);
        });
    }


    /***
     * Populates nutrients table with formatted data about maximum total nutrients
     */
    public void showNutrientsTable(){

        // Store all the nutrients
        Map<String, Nutrient> nutrientMap = maximumNutrientsPresenter.getMaxTotalNutrientsList();

        // Subcategory - Fat
        TreeItem<Nutrient> fat = new TreeItem<Nutrient>(new Nutrient("fats", "Fats"));
        for (String fatStr: maximumNutrientsPresenter.getFatStrings()){
            fat.getChildren().add(new TreeItem<Nutrient>(nutrientMap.get(fatStr)));
        }

        // Subcategory - Carbs
        TreeItem<Nutrient> carbs = new TreeItem<Nutrient>(new Nutrient("carbs", "Carbs"));
        for (String carb: maximumNutrientsPresenter.getCarbStrings()){
            carbs.getChildren().add(new TreeItem<Nutrient>(nutrientMap.get(carb)));
        }

        // Overall category
        TreeItem<Nutrient> nutrients = new TreeItem<Nutrient>(new Nutrient("nutrients", "Nutrients"));
        nutrients.setExpanded(true);
        nutrients.getChildren().add(fat);
        nutrients.getChildren().add(carbs);

        // Add the other nutrient types
        for (String nutrient: nutrientMap.keySet() ){

            if (maximumNutrientsPresenter.getCarbStrings().contains(nutrient) || maximumNutrientsPresenter.getFatStrings().contains(nutrient)){
                continue;
            }
            nutrients.getChildren().add(new TreeItem<Nutrient>(nutrientMap.get(nutrient)));
        }

        nutrientsTable.setRoot(nutrients);

    }



    /***
     * Refreshes the table
     */
    public void refreshTable(){

        nutrientsTable.refresh();
        showNutrientsTable();

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

        // Text colours
        titleLBL.setStyle("-fx-text-fill: " + textColour);
        editLBL.setStyle("-fx-text-fill: " + textColour);

        categoryAxis.setStyle("-fx-tick-label-fill: " + textColour);
        categoryAxis.setStyle("-fx-text-fill: " + textColour);
        numberAxis.setStyle("-fx-tick-label-fill: " + textColour);
        numberAxis.setStyle("-fx-text-fill: " + textColour);

        nutrientStringStackedBarChart.lookup(".chart-title").setStyle("-fx-text-fill: " + textColour);
        nutrientStringStackedBarChart.lookupAll(".axis").forEach((axis) -> {
            axis.setStyle("-fx-tick-label-fill:  " + textColour);
        });
        nutrientStringStackedBarChart.lookupAll(".axis .label").forEach((axis) -> {
            axis.setStyle("-fx-text-fill:  " + textColour);
        });

        // quantity and max quantity colour
        for (int i = 0; i < 8; i++){

            // even - set quantity colour
            if (i % 2 == 0){
                nutrientStringStackedBarChart.lookupAll(".default-color" + i + ".chart-bar").forEach((node) -> {
                    node.setStyle("-fx-bar-fill:" + quantityColour);
                });
            } else {
                // odd  - set max quantity colour
                nutrientStringStackedBarChart.lookupAll(".default-color" + i + ".chart-bar").forEach((node) -> {
                    node.setStyle("-fx-bar-fill:" + maxQuantityColour);
                });
            }

        }

        scene.setFill(Paint.valueOf(backgroundColour));

        nutrientsTable.refresh();

    }




}
