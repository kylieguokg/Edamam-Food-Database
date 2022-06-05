package project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import project.model.*;
import project.presenter.MainPresenter;
import project.view.MainView;

public class App extends Application {

    private static Model model;
    private static MainView mainView;
    private static MainPresenter mainPresenter;

    @Override
    public void start(Stage stage){
        stage.setTitle("Food Database");
        mainView.setUp(stage, mainPresenter);
        mainPresenter.setUp();
    }


    public static void main(String[] args) {

        boolean inputOnline;
        boolean outputOnline;

        if (args.length == 2){
            if (args[0].equals("offline")) {
                inputOnline = false;
            } else if (args[0].equals("online")) {
                inputOnline = true;
            } else {
                Platform.exit();
                return;
            }

            if (args[1].equals("offline")) {
                outputOnline = false;
            } else if (args[1].equals("online")) {
                outputOnline = true;
            } else {
                Platform.exit();
                return;
            }

            FoodDB foodDB;

            if (inputOnline){
                FoodDBHTTP foodDBHTTP = new FoodDBHTTP();
                FoodDBJsonParser foodDBJsonParser = new FoodDBJsonParser();
                FoodDBOnlineCache foodDBOnlineCache = new FoodDBOnlineCache();
                foodDB = new FoodDBOnline(foodDBHTTP, foodDBJsonParser, foodDBOnlineCache);
            } else {
                foodDB = new FoodDBDummy();
            }

            Output output;

            if (outputOnline){
                OutputHTTP outputHTTP = new OutputHTTP();
                OutputJsonParser outputJsonParser = new OutputJsonParser();
                output = new OutputOnline(outputHTTP, outputJsonParser);
            } else {
                output = new OutputDummy();
            }

            model = new Model(foodDB, output);
            mainView = new MainView();
            mainPresenter = new MainPresenter(model, mainView);



            launch();

        } else {
            System.out.println("invalid");
            Platform.exit();
        }


    }


}
