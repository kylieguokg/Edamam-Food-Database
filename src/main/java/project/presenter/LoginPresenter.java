package project.presenter;

import project.model.LoginState;
import project.model.Observer;
import project.view.LoginView;

/***
 * Responsible for mutating the login view and login state
 */
public class LoginPresenter implements Observer {

    /***
     * Model logic for logging in and creating user
     */
    private LoginState loginState;

    /***
     * View for logging in and registering
     */
    private LoginView loginView;

    public LoginPresenter(LoginState loginState, LoginView loginView) {

        this.loginState = loginState;
        this.loginView = loginView;

        loginState.subscribe(this);
    }

    /***
     * Sends request to loginState to log user in
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        if ("".equals(username)) throw new IllegalArgumentException("Not long enough!");
        if ("".equals(password)) throw new IllegalArgumentException("Not long enough!");

        loginState.login(username, password);

    }

    @Override
    public void update(){

        // if the user was created successfully
        if (loginState.getCreatedUser()){
            loginView.displayRegisterMSG("SUCCESS: User created");
        }

    }


    /***
     * Sends request to loginState to create user
     * @param username
     * @param password
     */
    public void createUser(String username, String password) {
        if ("".equals(username)) throw new IllegalArgumentException("Not long enough!");
        if ("".equals(password)) throw new IllegalArgumentException("Not long enough!");

        loginState.createUser(username, password);

    }


}
