package project.view;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import project.presenter.MainPresenter;
import javafx.scene.layout.Priority;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Tooltip;


import java.io.File;
import java.util.Optional;


/***
 *  Manages the views and navigation
 */
public class MainView {

    private Stage stage;
    private Scene currentScene;
    private View currentView;

    private boolean audioOn = true;
    private MediaPlayer mediaPlayer;

    private boolean darkMode;

    private StackPane topSP;
    private HBox topBox;
    private HBox extraBox;
    private HBox navigationBox;

    private Button darkModeBTN;
    private MenuButton menuBTN;
    private MenuItem outputBTN;
    private MenuItem maxNutrientsBTN;
    private Button savedFoodsBTN = new Button("");
    private Button homeBTN = new Button();
    private MenuItem clearCacheBTN;
    private MenuItem redditBTN;
    private Button saveBTN;
    private Button settingsBTN;

    private Label backgroundColourLBL;
    private Label buttonColourLBL;
    private Label textColourLBL;
    private Label tableColourLBL;
    private Label quantityColourLBL;
    private Label maxQuantityColourLBL;

    private ColorPicker backgroundColourPicker;
    private ColorPicker buttonColourPicker;
    private ColorPicker textColourPicker;
    private ColorPicker tableColourPicker;
    private ColorPicker quantityColourPicker;
    private ColorPicker maxQuantityColourPicker;

    private Image settingsIMG;
    private Image homeIMG;
    private Image speakerIMG;
    private Image muteIMG;
    private Image menuIMG;
    private Image maxNutrientsIMG;
    private Image clearCacheIMG;

    private ImageView settingIV;
    private ImageView homeIV;
    private ImageView speakerIV;
    private ImageView muteIV;
    private ImageView menuIV;
    private ImageView maxNutrientsIV;
    private ImageView clearCacheIV;
    private ImageView redditIV;

    /***
     *  Manages all the presenters and switches between them as necessary
     */
    private MainPresenter mainPresenter;

    /***
     * Gets the top stack pane which stores the navigation header
     * @return top stock pane
     */
    public StackPane getTopSP(){
        return topSP;
    }

    /***
     *   Starts in the search view
     */
    public void setUp(Stage stage, MainPresenter mainPresenter){

        this.stage = stage;
        this.mainPresenter = mainPresenter;

        String path = "src/main/resources/Doc-Earl-Klugh.mp3";

        Media media = new Media(new File(path).toURI().toString());

        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO); // play on loop
            }
        });

    }

    /***
     * Goes to this view
     * @param view
     */
    public void goToView(View view){
        this.currentView = view;
        this.currentScene = view.getScene();
        stage.setScene(currentScene);
    }

    /***
     * Gors to login view
     * @param view
     */
    public void goToView(LoginView view){
        this.currentScene = view.getScene();
        stage.setScene(currentScene);
        stage.show();
    }


    /***
     * Sets up the top navigation bar
     */
    public void setUpTopBox(){
        setUpNavigationBar();
        setUpExtraBox();

        topBox = new HBox();
        topBox.setId("topBox");
        navigationBox.setId("hBox");
        extraBox.setId("hBox");

        // logo
        HBox logoBox = new HBox();
        ImageView logoIV = new ImageView(new Image("Edamam_Badge_Transparent.png"));
        logoIV.setFitHeight(50);
        logoIV.setPreserveRatio(true);
        logoBox.getChildren().add(logoIV);
        logoBox.setId("hBox");

        // put space between navigation bar and settings
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        topBox.getChildren().addAll(navigationBox, region, extraBox);

        topSP = new StackPane();
        topSP.getChildren().addAll(logoBox, topBox);
    }

    /***
     * Sets up navigation bar
     */
    public void setUpNavigationBar(){

        navigationBox = new HBox(20);
        navigationBox.setAlignment(Pos.TOP_LEFT);

        setUpHomeBTN();
        setUpMenuBTN();
        setUpSavedFoodsBTN();
    }


    /***
     * Sets up the dropdown menu button
     */
    public void setUpMenuBTN(){

        menuBTN = new MenuButton();

        menuIV = new ImageView(new Image("dark-mode/white-menu.png"));
        menuIV.setFitHeight(20.0);
        menuIV.setFitWidth(20.0);
        menuBTN.setGraphic(menuIV);
        menuBTN.setOnMouseEntered((e) -> {
            menuBTN.fire();
        });

        // Post to Pastebin button
        ImageView outputIV = new ImageView("pastebin-logo.png");
        outputIV.setFitHeight(20.0);
        outputIV.setFitWidth(20.0);
        outputBTN = new MenuItem("  Post to Pastebin", outputIV);
        setUpOutputBTN();

        // Post to Reddit button
        redditIV = new ImageView("reddit-logo.png");
        redditIV.setFitHeight(20.0);
        redditIV.setFitWidth(20.0);
        redditBTN = new MenuItem("  Post to Reddit", redditIV);
        setUpRedditBTN();

        // See Total Nutritional Information button
        maxNutrientsIV = new ImageView("dark-mode/white-nutrition.png");
        maxNutrientsIV.setFitHeight(20.0);
        maxNutrientsIV.setFitWidth(20.0);
        maxNutrientsBTN = new MenuItem("  See Total Nutritional Information", maxNutrientsIV);
        setUpMaxNutrientsBTN();

        // Clear Cache button
        clearCacheIV = new ImageView("dark-mode/white-cache.png");
        clearCacheIV.setFitHeight(20.0);
        clearCacheIV.setFitWidth(20.0);
        clearCacheBTN = new MenuItem("  Clear Cache", clearCacheIV);
        setUpClearCacheBTN();

        menuBTN.getItems().addAll(outputBTN, redditBTN, maxNutrientsBTN, clearCacheBTN);

        navigationBox.getChildren().add(menuBTN);
    }



    /***
     *   Sets up a button for allowing a user to output long report of saved ingredients
     */
    public void setUpOutputBTN(){

        outputBTN.setOnAction((ActionEvent e) -> {
            mainPresenter.output();
        });

    }

    /***
     *   Sets up a button for allowing a user to output long report of saved ingredients
     */
    public void setUpOutputBTN(Button btn){

        btn.setOnAction((ActionEvent e) -> {
            mainPresenter.output();
        });

    }


    /***
     *   Sets up a button for allowing a user to post to Reddit
     */
    public void setUpRedditBTN(){

        redditBTN.setOnAction((ActionEvent e) -> {
            mainPresenter.postToReddit();
        });

    }


    /***
     *   Sets up a button for allowing a user to post to Reddit
     */
    public void setUpRedditBTN(Button btn){

        btn.setOnAction((ActionEvent e) -> {
            mainPresenter.postToReddit();
        });


    }


    /***
     * Sets up Reddit Login BTN
     */
    public void setUpRedditLogin(){

        Dialog<Pair<String, String>> redditLogin = new Dialog<>();

        ImageView imageView = new ImageView("reddit-logo.png");
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        redditLogin.setGraphic(imageView);
        redditLogin.setHeaderText("Login to Reddit");
        redditLogin.setContentText("Please login to your Reddit account so that you can post.");

        ButtonType loginButtonType = new ButtonType("Sign in", ButtonBar.ButtonData.OK_DONE);
        redditLogin.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        redditLogin.getDialogPane().setStyle(" -fx-font-family: 'Comfortaa'; -fx-min-width: 350; fx-alignment: CENTER");

        GridPane loginGP = new GridPane();
        loginGP.setHgap(10);
        loginGP.setVgap(20);

        Label userNameLBL = new Label("Username: ");
        TextField userNameTF = new TextField();

        Label passwordLBL = new Label("Password: ");
        PasswordField passwordTF = new PasswordField();

        loginGP.add(userNameLBL, 0, 0);
        loginGP.add(userNameTF, 1, 0);
        loginGP.add(passwordLBL, 0, 1);
        loginGP.add(passwordTF, 1, 1);

        redditLogin.getDialogPane().setContent(loginGP);

        // only if user clicks LOGIN, take the result
        redditLogin.setResultConverter(clicked -> {
            if (clicked == loginButtonType) {
                return new Pair<>(userNameTF.getText(), passwordTF.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = redditLogin.showAndWait();


        if (result.isPresent()) {
            mainPresenter.loginReddit(userNameTF.getText(), passwordTF.getText());
        }

    }

    /***
     *   Sets up a button for allowing a user to save an ingredient to their list of saved foods
     */
    public void setUpSavedFoodsBTN(){

        ImageView saveIV = new ImageView(new Image("heart.png"));
        saveIV.setFitHeight(30.0);
        saveIV.setFitWidth(30.0);
        savedFoodsBTN.setGraphic(saveIV);
        Tooltip saveTT = new Tooltip("See saved foods");
        saveTT.setShowDelay(Duration.seconds(0));
        savedFoodsBTN.setTooltip(saveTT);

        if (navigationBox.getChildren().contains(savedFoodsBTN)){
           navigationBox.getChildren().remove(savedFoodsBTN);
        }

        savedFoodsBTN.setOnAction((ActionEvent e) -> {
            mainPresenter.goToSavedFoods();
        });

        navigationBox.getChildren().add(savedFoodsBTN);

    }

    /***
     *   Sets up a button for allowing a user to view
     *   the running total nutrition information for list of saved foods
     */
    public void setUpMaxNutrientsBTN(){

        maxNutrientsBTN.setOnAction((ActionEvent e) -> {
            mainPresenter.goToMaxNutrients();
        });

    }

    /***
     *   Sets up a button for allowing a user to view
     *   the running total nutrition information for list of saved foods
     */
    public void setUpMaxNutrientsBTN(Button btn){

        btn.setOnAction((ActionEvent e) -> {
            mainPresenter.goToMaxNutrients();
        });


    }


    /***
     *   Sets up a button for allowing a user to return to home
     *   @param navigationBox navigation box to store the button
     */
    public void setUpHomeBTN(){

        homeIV = new ImageView(homeIMG);
        homeIV.setFitHeight(30.0);
        homeIV.setFitWidth(30.0);
        homeBTN.setGraphic(homeIV);

        Tooltip homeTT = new Tooltip("Go to Search");
        homeTT.setShowDelay(Duration.seconds(0));
        homeBTN.setTooltip(homeTT);

        navigationBox.getChildren().add(homeBTN);
        homeBTN.setOnAction((ActionEvent e) -> {
            mainPresenter.goToSearchView();
        });



        homeBTN.setId("homeBTN");
    }


    /***
     *   Sets up a button for clearing cache
     *   @param navigationBox navigation box to store the button
     */
    public void setUpClearCacheBTN(){

        if (navigationBox.getChildren().contains(clearCacheBTN)){
            navigationBox.getChildren().remove(clearCacheBTN);
        }

        clearCacheBTN.setOnAction((ActionEvent e) -> {
            mainPresenter.clearCache();
        });
    }

    /***
     *   Set up theme song toggle
     */
    public void setUpThemeSongBTN(){

        // Speaker symbol
        speakerIV = new ImageView(homeIMG);
        speakerIV.setFitHeight(30.0);
        speakerIV.setFitWidth(30.0);


        Button themeSongBTN = new Button();
        themeSongBTN.setGraphic(speakerIV);
        themeSongBTN.setStyle("-fx-border-color: TRANSPARENT");
        extraBox.getChildren().add(themeSongBTN);

        themeSongBTN.setOnAction((ActionEvent e) -> {

            audioOn = !audioOn;

            if (audioOn){

                speakerIV.setImage(speakerIMG);
                mediaPlayer.setMute(false);

            } else {
                speakerIV.setImage(muteIMG);
                mediaPlayer.setMute(true);
            }

        });

    }



    /***
     *   Set up theme song toggle
     */
    public void setUpDarkModeBTN(){

        darkModeBTN = new Button();

        if (darkMode){
            darkModeBTN.setText("Dark Mode On");
        } else {
            darkModeBTN.setText("Light Mode On");
        }

        darkModeBTN.setId("borderBTN");

        extraBox.getChildren().add(darkModeBTN);

        darkModeBTN.setOnAction((ActionEvent e) -> {
            darkMode = !darkMode;
            mainPresenter.setDarkMode();
        });

    }


    /***
     * Sets view to reflect dark mode
     */
    public void setDarkMode(){
        darkMode = true;
        darkModeBTN.setText("Dark Mode On");

        settingsIMG = new Image("dark-mode/white-gear.png");
        settingIV.setImage(settingsIMG);

        homeIMG = new Image("dark-mode/white-home.png");
        homeIV.setImage(homeIMG);

        speakerIMG = new Image("dark-mode/white-speaker.png");
        muteIMG = new Image("dark-mode/white-mute.png");

        if (audioOn){
            speakerIV.setImage(speakerIMG);
        } else {
            speakerIV.setImage(muteIMG);
        }

        menuIMG = new Image("dark-mode/white-menu.png");
        menuIV.setImage(menuIMG);

        maxNutrientsIMG = new Image("dark-mode/white-nutrition.png");
        maxNutrientsIV.setImage(maxNutrientsIMG);

        clearCacheIMG = new Image("dark-mode/white-cache.png");
        clearCacheIV.setImage(clearCacheIMG);

        currentScene.getStylesheets().remove("LightMode.css");
        currentScene.getStylesheets().add("DarkMode.css");
    }


    /***
     * Sets view to reflect light mode
     */
    public void setLightMode(){
        darkMode = false;

        darkModeBTN.setText("Light Mode On");
        settingsIMG = new Image("light-mode/black-gear.png");
        settingIV.setImage(settingsIMG);

        homeIMG = new Image("light-mode/black-home.png");
        homeIV.setImage(homeIMG);

        speakerIMG = new Image("light-mode/black-speaker.png");
        muteIMG = new Image("light-mode/black-mute.png");

        if (audioOn){
            speakerIV.setImage(speakerIMG);
        } else {
            speakerIV.setImage(muteIMG);
        }

        menuIMG = new Image("light-mode/black-menu.png");
        menuIV.setImage(menuIMG);

        maxNutrientsIMG = new Image("light-mode/black-nutrition.png");
        maxNutrientsIV.setImage(maxNutrientsIMG);

        clearCacheIMG = new Image("light-mode/black-cache.png");
        clearCacheIV.setImage(clearCacheIMG);

        currentScene.getStylesheets().remove("DarkMode.css");
        currentScene.getStylesheets().add("LightMode.css");

    }

    /***
     *  Retrieves the respective stylesheet
     *  depending on the current mode
     */
    public String getStyleSheet() {
        if (darkMode) {
            return "DarkMode.css";
        } else {
            return "LightMode.css";
        }
    }


     /***
     * Converts colour to RGB format
     * Citation: How to get hex web String from JavaFX ColorPicker color?
     * (2013, July 29). Stack Overflow.
     * https://stackoverflow.com/questions/17925318/how-to-get-hex-web-string-from-javafx-colorpicker-color/18803814#18803814
     * @param color color to convert
     */
    public static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }

    /***
     * Sets up settings button
     * @param box
     */
    public void setUpSettingsButton(){

        settingsIMG = new Image("light-mode/black-gear.png");
        settingIV = new ImageView(settingsIMG);
        settingsBTN = new Button("");
        settingsBTN.setStyle("-fx-border-color: TRANSPARENT");
        settingIV.setFitHeight(30.0);
        settingIV.setFitWidth(30.0);
        settingsBTN.setGraphic(settingIV);
        Tooltip settingsTT = new Tooltip("Adjust colours");
        settingsTT.setShowDelay(Duration.seconds(0));
        settingsBTN.setTooltip(settingsTT);

        extraBox.getChildren().add(settingsBTN);

        VBox settingsMenu = new VBox(12);
        settingsMenu.setId("settings");

        Button closeButton = new Button(Character.toString(10005));
        closeButton.setId("close");
        settingsMenu.getChildren().add(closeButton);

        backgroundColourLBL = new Label("Background Colour: ");
        backgroundColourPicker = new ColorPicker();

        buttonColourLBL = new Label("Button: ");
        buttonColourPicker = new ColorPicker();

        textColourLBL = new Label("Text: ");
        textColourPicker = new ColorPicker();

        tableColourLBL = new Label("Table: ");
        tableColourPicker = new ColorPicker();

        quantityColourLBL = new Label("Quantity: ");
        quantityColourPicker = new ColorPicker();

        maxQuantityColourLBL = new Label("Max Quantity: ");
        maxQuantityColourPicker = new ColorPicker();

        settingsMenu.getChildren().addAll(backgroundColourLBL, backgroundColourPicker,
                buttonColourLBL, buttonColourPicker,
                textColourLBL, textColourPicker,
                tableColourLBL, tableColourPicker,
                quantityColourLBL, quantityColourPicker,
                maxQuantityColourLBL, maxQuantityColourPicker);


        saveBTN = new Button("Save");
        saveBTN.setId("borderBTN");

        Region space = new Region();

        settingsMenu.getChildren().addAll(space, saveBTN);

        settingsBTN.setOnAction((ActionEvent e) -> {
            currentView.openSettings(settingsMenu);
        });

        closeButton.setOnAction((ActionEvent e) -> {
            currentView.closeSettings(settingsMenu);
        });

        saveBTN.setOnAction((ActionEvent e) -> {
            Color backgroundColour = backgroundColourPicker.getValue();
            Color buttonColour = buttonColourPicker.getValue();
            Color textColour = textColourPicker.getValue();
            Color tableColour = tableColourPicker.getValue();
            Color quantityColour = quantityColourPicker.getValue();
            Color maxQuantityColour = maxQuantityColourPicker.getValue();

            mainPresenter.saveColours(darkMode, toRGBCode(backgroundColour), toRGBCode(buttonColour), toRGBCode(textColour),
                    toRGBCode(tableColour), toRGBCode(quantityColour),
                    toRGBCode(maxQuantityColour));

        });

    }


    /***
     * Sets colours to the settings panel
     * @param darkMode
     * @param backgroundColour
     * @param buttonColour
     * @param textColour
     * @param tableColour
     * @param quantityColour
     * @param maxQuantityColour
     */
    public void setSettingsColours(boolean darkMode, String backgroundColour,String buttonColour, String textColour,
                                   String tableColour, String quantityColour, String maxQuantityColour){


        if (darkMode){
            setDarkMode();
        } else if (!darkMode){
            setLightMode();
        }

        // text colour
        outputBTN.setStyle("-fx-text-fill: " + textColour);
        clearCacheBTN.setStyle("-fx-text-fill: " + textColour);
        redditBTN.setStyle("-fx-text-fill: " + textColour);
        maxNutrientsBTN.setStyle("-fx-text-fill: " + textColour);

        // button colour
        darkModeBTN.setStyle("-fx-border-color: " + buttonColour +
                "; -fx-text-fill: " + buttonColour);

       saveBTN.setStyle("-fx-border-color: " + buttonColour +
                "; -fx-text-fill: " + buttonColour);

        backgroundColourLBL.setStyle("-fx-text-fill: " + textColour);
        backgroundColourPicker.setStyle("-fx-border-color: " + buttonColour);
        backgroundColourPicker.setValue(Color.valueOf(backgroundColour));

        buttonColourLBL.setStyle("-fx-text-fill: " + textColour);
        buttonColourPicker.setStyle("-fx-border-color: " + buttonColour);
        buttonColourPicker.setValue(Color.valueOf(buttonColour));

        textColourLBL.setStyle("-fx-text-fill: " + textColour);
        textColourPicker.setStyle("-fx-border-color: " + buttonColour);
        textColourPicker.setValue(Color.valueOf(textColour));

        tableColourLBL.setStyle("-fx-text-fill: " + textColour);
        tableColourPicker.setStyle("-fx-border-color: " + buttonColour);
        tableColourPicker.setValue(Color.valueOf(tableColour));

        quantityColourLBL.setStyle("-fx-text-fill: " + textColour);
        quantityColourPicker.setStyle("-fx-border-color: " + buttonColour);
        quantityColourPicker.setValue(Color.valueOf(quantityColour));

        maxQuantityColourLBL.setStyle("-fx-text-fill: " + textColour);
        maxQuantityColourPicker.setStyle("-fx-border-color: " + buttonColour);
        maxQuantityColourPicker.setValue(Color.valueOf(maxQuantityColour));
    }


    /***
     * Sets up Extra Box which contains non-domain specific extras
     */
    public void setUpExtraBox(){
        extraBox = new HBox(20);
        extraBox.setAlignment(Pos.TOP_RIGHT);

        setUpDarkModeBTN();
        setUpThemeSongBTN();
        setUpSettingsButton();
    }


    /***
     * Sets colours of setting panel
     * @param darkMode
     * @param backgroundColour
     * @param buttonColour
     * @param textColour
     * @param tableColour
     * @param quantityColour
     * @param maxQuantityColour
     */
    public void setColours(boolean darkMode, String backgroundColour, String buttonColour, String textColour,
                           String tableColour, String quantityColour, String maxQuantityColour){

        setSettingsColours(darkMode, backgroundColour, buttonColour,textColour, tableColour, quantityColour, maxQuantityColour);


    }

    /***
     * Displays alert that the cache has been cleared
     */
    public void cacheClearedAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dialogPane = alert.getDialogPane();
        alert.setContentText("Cache has been cleared");
        dialogPane.setStyle(" -fx-font-family: 'Comfortaa';");
        alert.showAndWait();
    }

    /***
     * Displays output of an attempt to post output
     * @param out
     */
    public void setOutputTXT(String out){

        Alert alert;
        DialogPane dialogPane;
        if (out.startsWith("ERROR")){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(out);


        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            TextArea txtArea = new TextArea(out);
            txtArea.setEditable(false);
            alert.getDialogPane().setContent(txtArea);
        }

        dialogPane = alert.getDialogPane();
        dialogPane.setStyle(" -fx-font-family: 'Comfortaa';");
        alert.showAndWait();

    }


}
