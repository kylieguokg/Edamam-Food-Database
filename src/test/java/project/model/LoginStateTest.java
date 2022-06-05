package project.model;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;
import org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class LoginStateTest {

    private FoodDBJsonParser foodDBJsonParser;

    private Food result1;
    private Measure measure1;
    private Nutrients nutrients1;

    private Food result2;
    private Measure measure2;
    private Nutrients nutrients2;
    private FoodDBHTTP foodDBHTTP;
    private FoodDBOnlineCache foodDBOnlineCache;

    private Gson gson;
    private FoodDB foodDB;
    private LoginState loginState;
    private Observer observer;

    @BeforeEach
    public void setup() {

        this.foodDB = mock(FoodDB.class);
        this.observer = mock(Observer.class);
        this.loginState = new LoginState(foodDB);

        loginState.subscribe(observer);
    }


    @Test
    /***
     * Checks if loginState returns correctly, given the database has said the user logged in
     */
    public void loginTestSuccess() {

        when (foodDB.login("user", "123")).thenReturn(true);

        User user = new User("user");

        when(foodDB.getUser()).thenReturn(user);

        loginState.login("user", "123");

        assertEquals(loginState.getUser(), user);

        verify(observer).update();

        // should be reset
        assertFalse(loginState.getLoggedIn());

    }


    @Test
    /***
     * Checks if loginState returns correctly, given the database has said the user does not exist
     */
    public void loginTestFail() {

        when (foodDB.login("user", "123")).thenReturn(false);

        loginState.login("user", "123");

        assertEquals(loginState.getUser(), null);


        verify(observer).update();

        assertFalse(loginState.getLoggedIn());

    }

    @Test
    /***
     * Checks if loginState returns correctly,
     * given the database has said the user was created successfully
     */
    public void createUserTestSuccess(){

        when(foodDB.createUser("user", "123")).thenReturn(true);

        loginState.createUser("user", "123");

        verify(observer).update();

        // should be reset
        assertFalse(loginState.getCreatedUser());


    }


    @Test
    /***
     * Checks if loginState returns correctly,
     * given the database has said the user was not created successfully
     */
    public void createUserTestFail(){

        when(foodDB.createUser("user", "123")).thenReturn(false);

        loginState.createUser("user", "123");

        verify(observer).update();

        assertFalse(loginState.getCreatedUser());
    }






}


