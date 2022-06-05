package project.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * Manages the saved colours of a user
 */
public class SavedColoursState implements Subject{

    /***
     * Stores the user's saved colours
     */
    private FoodDB foodDB;

    /***
     * Currently logged in user
     */
    private User currentUser;

    /***
     * List of observers who need to be updated if the saved colours state is changed
     */
    private List<Observer> observerList = new CopyOnWriteArrayList<Observer>();

    /***
     * Returns whether or not the colours were saved
     */
    private boolean coloursUpdated = false;

    /***
     * Returns whether or not dark mode status was saved
     */
    private boolean darkModeUpdated = false;

    public SavedColoursState(FoodDB foodDB, User currentUser){
        this.foodDB = foodDB;
        this.currentUser = currentUser;
    }

    @Override
    public void notifyObservers() {

        for (Observer observer : observerList){
            observer.update();
        }

        // reset
        coloursUpdated = false;
        darkModeUpdated = false;
    }

    @Override
    public void subscribe(Observer observer){
        observerList.add(observer);
    }

    /***
     * Saves user's colour preferences to database
     * @param darkMode
     * @param backgroundColour
     * @param buttonColour
     * @param textColour
     * @param tableColour
     * @param quantityColour
     * @param maxQuantityColour
     */
    public void saveColours(boolean darkMode, String backgroundColour, String buttonColour, String textColour,
                            String tableColour, String quantityColour, String maxQuantityColour){

        currentUser.setDarkMode(darkMode);
        currentUser.setBackgroundColour(backgroundColour);
        currentUser.setButtonColour(buttonColour);
        currentUser.setTextColour(textColour);
        currentUser.setTableColour(tableColour);
        currentUser.setQuantityColour(quantityColour);
        currentUser.setMaxQuantityColour(maxQuantityColour);

        // save colours to database
        foodDB.saveColours();

        coloursUpdated = true;
        notifyObservers();
    }


    public boolean isColoursUpdated() {
        return coloursUpdated;
    }


    /***
     * Sets the user's preferences to dark mode
     */
    public void setDarkMode() {
        currentUser.setDarkMode();
        foodDB.saveColours();
        darkModeUpdated = true;
        notifyObservers();
    }

    public boolean darkModeUpdated(){
        return darkModeUpdated;
    }

    public User getUser(){
        return currentUser;
    }

    @Override
    public void unsubscribe(Observer observer){
        if (observerList.contains(observer)){
            observerList.remove(observer);
        }
    }


    public boolean isDarkMode(){
        return currentUser.isDarkMode();
    }

    public String getTextColour() {
        return currentUser.getTextColour();
    }

    public String getButtonColour() {
        return currentUser.getButtonColour();
    }

    public String getBackgroundColour() {
        return currentUser.getBackgroundColour();
    }

    public String getTableColour() {
        return currentUser.getTableColour();
    }

    public String getMaxQuantityColour() {
        return currentUser.getMaxQuantityColour();
    }

    public String getQuantityColour() {return currentUser.getQuantityColour();}

}

