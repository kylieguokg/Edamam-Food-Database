package project.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;



public class SavedColoursStateTest {

    private FoodDB foodDB;
    private SavedColoursState savedColoursState;
    private User user;

    private Observer observer;


    @BeforeEach
    public void setup() {
        this.foodDB = mock(FoodDB.class);
        this.observer = mock(Observer.class);
        this.user = new User("user");

        this.savedColoursState = new SavedColoursState(foodDB, user);
        savedColoursState.subscribe(observer);

    }


    @Test
    /***
     * Checks that the colours are saved correctly and an update has been to sent to the observer
     */
    public void saveColoursTest() {

        savedColoursState.saveColours(true, "BLACK", "WHITE", "RED", "YELLOW", "PURPLE", "GREEN");

        assertTrue(user.isDarkMode());
        assertEquals(user.getBackgroundColour(), "BLACK");
        assertEquals(user.getButtonColour(), "WHITE");
        assertEquals(user.getTextColour(), "RED");
        assertEquals(user.getTableColour(), "YELLOW");
        assertEquals(user.getQuantityColour(), "PURPLE");
        assertEquals(user.getMaxQuantityColour(), "GREEN");

        verify(observer).update();

        // should been reset
        assertFalse(savedColoursState.isColoursUpdated());

    }

    @Test
    /***
     * Checks that the colours are saved correctly and an update has been to sent to the observer
     */
    public void saveColoursTest2() {

        savedColoursState.saveColours(false, "WHITE", "PINK", "RED", "YELLOW", "PURPLE", "GREEN");

        assertFalse(user.isDarkMode());
        assertEquals(user.getBackgroundColour(), "WHITE");
        assertEquals(user.getButtonColour(), "PINK");
        assertEquals(user.getTextColour(), "RED");
        assertEquals(user.getTableColour(), "YELLOW");
        assertEquals(user.getQuantityColour(), "PURPLE");
        assertEquals(user.getMaxQuantityColour(), "GREEN");

        verify(observer).update();

        // should been reset
        verify(observer).update();
        assertFalse(savedColoursState.isColoursUpdated());

    }

    @Test
    /***
     * Checks that the colours are set correctly for dark mode and an update has been to sent to the observer
     */
    public void setDarkModeTest() {

        // default is dark mode so if we click dark mode -> light mode

        // switch to light mode
        savedColoursState.setDarkMode();

        assertFalse(user.isDarkMode());
        assertEquals(user.getBackgroundColour(), "WHITE");
        assertEquals(user.getButtonColour(), "BLACK");
        assertEquals(user.getTextColour(), "BLACK");
        assertEquals(user.getTableColour(), "#80B380");
        assertEquals(user.getQuantityColour(), "#99CCCC");
        assertEquals(user.getMaxQuantityColour(), "#A0B7FA");


        // should been reset
        verify(observer).update();
        assertFalse(savedColoursState.darkModeUpdated());

        // switch back to dark mode
        savedColoursState.setDarkMode();

        assertTrue(user.isDarkMode());
        assertEquals(user.getBackgroundColour(), "BLACK");
        assertEquals(user.getButtonColour(), "WHITE");
        assertEquals(user.getTextColour(), "WHITE");
        assertEquals(user.getTableColour(), "#80B380");
        assertEquals(user.getQuantityColour(), "#99CCCC");
        assertEquals(user.getMaxQuantityColour(), "#A0B7FA");

        // should been reset
        verify(observer, times(2)).update();
        assertFalse(savedColoursState.darkModeUpdated());

    }

}