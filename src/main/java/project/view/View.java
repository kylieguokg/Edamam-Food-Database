package project.view;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;

/***
 * A page in the GUI
 */
public interface View {


    /***
     * Get the current view's scene
     * @return current scene
     */
    public Scene getScene();

    /***
     * Opens the colour settings
     * @param settingsMenu stores the colour settings
     */
    public void openSettings(VBox settingsMenu);

    /***
     * Closes the settings
     * @param settingsMenu stores the colour settings
     */
    public void closeSettings(VBox settingsMenu);

    /****
     * Sets the colours of the view
     * @param darkMode
     * @param backgroundColour
     * @param buttonColour
     * @param textColour
     * @param tableColour
     * @param quantityColour
     * @param maxQuantityColour
     */
    public void setColours(boolean darkMode, String backgroundColour, String buttonColour, String textColour,
                           String tableColour, String quantityColour,
                           String maxQuantityColour);


    /***
     * Sets up the top navigation bar
     */
    public void setUpTopBox();

}

