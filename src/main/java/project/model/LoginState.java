package project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * Manages logging in and creating users
 */
public class LoginState implements Subject{

    /***
     * Current user logged in
     */
    private User currentUser;

    /***
     * Database storing users and saved foods
     */
    private FoodDB foodDB;

    /***
     * Whether if a user was successfully logged in
     */
    private boolean loginState = false;

    /***
     * Whether if a user was successfully created or not
     */
    private boolean createUserState = false;

    /***
     * List of observers who need to be updated if the login state is updated
     */
    private List<Observer> observerList;


    public LoginState(FoodDB foodDB){
        this.foodDB = foodDB;
        observerList = new CopyOnWriteArrayList<Observer>();
    }

    /***
     * Attempts to log a user in with this username and password
     * @param username
     * @param password
     */
    public void login(String username, String password){

        loginState = foodDB.login(username, password);

        if (loginState){
            currentUser = foodDB.getUser();
        }

        notifyObservers();
    }

    /***
     * Attempts to create a user in the database with this username and password
     * @param username
     * @param password
     */
    public void createUser(String username, String password){
        createUserState = foodDB.createUser(username, password);
        notifyObservers();
    }

    /***
     * Current user logged in
     * @return current user logged in
     */
    public User getUser(){
        return currentUser;
    }

    @Override
    public void notifyObservers() {

        for (Observer observer : observerList){
            observer.update();
        }

        // reset
        loginState = false;
        createUserState = false;
    }

    /***
     * Whether if a user was successfully logged in
     * @return if a user was successfully logged in
     */
    public boolean getLoggedIn(){
        return loginState;
    }

    @Override
    public void subscribe(Observer observer){
        observerList.add(observer);
    }

    /***
     * Whether if a user was successfully created or not
     * @return if a user was successfully created
     */
    public boolean getCreatedUser() {
        return createUserState;
    }

    @Override
    public void unsubscribe(Observer observer){
        observerList.remove(observer);
    }



}
