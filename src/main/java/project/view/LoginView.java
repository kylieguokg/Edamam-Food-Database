package project.view;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import project.presenter.LoginPresenter;
import javafx.scene.layout.Region;

/***
 *   Displays the login and registration page
 */
public class LoginView {

    private Scene scene;

    /***
     *   Presenter responsible for mutating this view and the model
     */
    private LoginPresenter loginPresenter;

    /***
     *  Displays the result of login attempt
     */
    private Label loginLBL;

    /***
     *  Displays the result of registration attempt
     */
    private TextArea registerTA;

    /***
     * Holds the login form
     */
    private VBox logInBox;

    /***
     * Holds the registration form
     */
    private VBox registerBox;

    public Scene getScene(){
        return scene;
    }

    /***
     * Sets up the login and registration view
     * @param loginPresenter presenter responsible for mutating view and model
     */
    public void display(LoginPresenter loginPresenter){

        this.loginPresenter = loginPresenter;

        BorderPane borderPaneNotLoggedIn = new BorderPane();
        scene = new Scene(borderPaneNotLoggedIn, 1400, 800);
        scene.getStylesheets().add("Style.css");

        logInBox = new VBox(20);

        borderPaneNotLoggedIn.setLeft(logInBox);
        BorderPane.setMargin(logInBox, new Insets(0, 0, 0, 450));
        logInBox.setAlignment(Pos.CENTER);

        setUpLogin();

        registerBox = new VBox(20);
        registerBox.getStyleClass().add("vbox");
        borderPaneNotLoggedIn.setRight(registerBox);
        BorderPane.setMargin(registerBox, new Insets(40, 350, 0, 0));
        registerBox.setAlignment(Pos.CENTER);

        setUpRegister();
    }


    /***
     * Sets up login form view
     */
    public void setUpLogin(){

        Label loginTitle = new Label("Login");
        loginTitle.setId("title");
        logInBox.getChildren().add(loginTitle);

        Label userNameLBL = new Label("Username: ");
        TextField userNameTF = new TextField();
        logInBox.getChildren().addAll(userNameLBL, userNameTF);

        Label passwordLBL = new Label("Password: ");
        PasswordField passwordTF = new PasswordField();
        logInBox.getChildren().addAll(passwordLBL, passwordTF);

        Region space = new Region();
        logInBox.getChildren().add(space);

        Button signInBTN = new Button("Sign in");
        logInBox.getChildren().add(signInBTN);

        loginLBL = new Label();

        logInBox.getChildren().add(loginLBL);

        signInBTN.setOnAction((ActionEvent e) -> {

            try {
                loginPresenter.login(userNameTF.getText(), passwordTF.getText());
            } catch (IllegalArgumentException m) {
                displayLoginMSG(m.getMessage());
                return;
            }

        });

        passwordTF.setOnAction((ActionEvent e) -> {
            try {
                loginPresenter.login(userNameTF.getText(), passwordTF.getText());
            } catch (IllegalArgumentException m) {
                displayLoginMSG(m.getMessage());
                return;
            }
        });


    }

    /***
     * Sets up registration form view
     */
    public void setUpRegister(){
        Label registerTitle = new Label("Register");
        registerTitle.setId("title");
        registerBox.getChildren().add(registerTitle);

        Label newUserNameLBL = new Label("Username: ");
        TextField newUserNameTF = new TextField();
        registerBox.getChildren().addAll(newUserNameLBL, newUserNameTF);

        Label newPasswordLBL = new Label("Password: ");
        PasswordField newPasswordTF = new PasswordField();
        registerBox.getChildren().addAll(newPasswordLBL, newPasswordTF);

        Region space = new Region();
        registerBox.getChildren().add(space);

        Button registerBTN = new Button("Register");
        registerBox.getChildren().add(registerBTN);

        registerTA = new TextArea();
        registerTA.setEditable(false);
        registerBox.getChildren().add(registerTA);

        registerBTN.setOnAction((ActionEvent e) -> {

            try {
                loginPresenter.createUser(newUserNameTF.getText(), newPasswordTF.getText());
            } catch (IllegalArgumentException m) {
                displayRegisterMSG(m.getMessage());
                return;
            }

        });

        newPasswordTF.setOnAction((ActionEvent e) -> {
            try {
                loginPresenter.createUser(newUserNameTF.getText(), newPasswordTF.getText());
            } catch (IllegalArgumentException m) {
                displayRegisterMSG(m.getMessage());
                return;
            }
        });
    }


    /***
     * Displays result of login attempt
     */
    public void displayLoginMSG(String msg){
        loginLBL.setText(msg);
    }

    /***
     * Displays result of register attempt
     */
    public void displayRegisterMSG(String msg){
        registerTA.setText(msg);
    }


}
