package project.model;

import java.util.Map;

/***
 * User can have stored personalised colours and maximum total nutrients
 */
public class User {

    private String username;

    // default colours
    private boolean darkMode = true;
    private String buttonColour = "WHITE";
    private String textColour = "WHITE";
    private String tableColour = "#80B380";
    private String quantityColour = "#99CCCC";
    private String maxQuantityColour = "#A0B7FA";
    private String backgroundColour = "BLACK";

    /***
     * Stores the user's maximum values for nutrients
     */
    private TotalNutrients maximumTotalNutrients;

    /***
     * Stores the nutrients for the running total nutrients
     */
    private TotalNutrients runningTotalsTotalNutrients;

    /***
     * Stores the nutrients for the running total nutrients (as daily %)
     */
    private TotalNutrients runningTotalsTotalDaily;

    public User(String username){
        this.username = username;
        maximumTotalNutrients = new TotalNutrients();
        maximumTotalNutrients.setQuantities(500.0); // default value

        // default values
        runningTotalsTotalNutrients = new TotalNutrients();
        runningTotalsTotalDaily = new TotalNutrients();
        runningTotalsTotalDaily.setUnits("%");
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    /***
     * Sets the colours to the default dark mode colours
     */
    public void setDarkMode() {
        this.darkMode = !darkMode;

        if (darkMode){
            this.buttonColour = "WHITE";
            this.textColour = "WHITE";
            this.tableColour = "#80B380";
            this.quantityColour = "#99CCCC";
            this.maxQuantityColour = "#A0B7FA";
            this.backgroundColour = "BLACK";
        } else {
            this.buttonColour = "BLACK";
            this.textColour = "BLACK";
            this.tableColour = "#80B380";
            this.quantityColour = "#99CCCC";
            this.maxQuantityColour = "#A0B7FA";
            this.backgroundColour = "WHITE";
        }
    }

    /***
     * Sets the colours to the default dark mode colours
     * @param darkMode if darkmode is on or not
     */
    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;

        if (darkMode){
            this.buttonColour = "WHITE";
            this.textColour = "WHITE";
            this.tableColour = "#80B380";
            this.quantityColour = "#A0B7FA";
            this.maxQuantityColour = "#A0B7FA";
            this.backgroundColour = "BLACK";
        } else {
            this.backgroundColour = "WHITE";
            this.buttonColour = "BLACK";
            this.textColour = "BLACK";
            this.tableColour = "#80B380";
            this.quantityColour = "#A0B7FA";
            this.maxQuantityColour = "#A0B7FA";
        }
    }

    public String getButtonColour() {
        return buttonColour;
    }

    public void setButtonColour(String buttonColour) {
        this.buttonColour = buttonColour;
    }

    public String getTextColour() {
        return textColour;
    }

    public void setTextColour(String textColour) {
        this.textColour = textColour;
    }

    public String getQuantityColour() {
        return quantityColour;
    }

    public void setQuantityColour(String quantityColour) {
        this.quantityColour = quantityColour;
    }

    public String getMaxQuantityColour() {
        return maxQuantityColour;
    }

    public void setMaxQuantityColour(String maxQuantityColour) {
        this.maxQuantityColour = maxQuantityColour;
    }

    public String getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(String backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public String getTableColour() {
        return tableColour;
    }

    public void setTableColour(String tableColour) {
        this.tableColour = tableColour;
    }

    public String getUsername() {
        return username;
    }

    public TotalNutrients getMaximumTotalNutrients(){
        return maximumTotalNutrients;
    }

    public void setMaximumTotalNutrientValue(String nutrient, Double value){
        this.maximumTotalNutrients.setQuantity(nutrient, value);
    }

    public Double getMaxNutrientQuantity(String nutrient) {
        return maximumTotalNutrients.getQuantity(nutrient);
    }

    public Map<String, Nutrient> getMaxTotalNutrientsList(){
        return maximumTotalNutrients.getNutrientList();
    }

    public TotalNutrients getRunningTotalsTotalNutrients(){
        return runningTotalsTotalNutrients;
    }

    public TotalNutrients getRunningTotalsTotalDaily(){
        return runningTotalsTotalDaily;
    }

    public void addToRunningTotalNutrientsValue(String nutrient, Double quantity){
        runningTotalsTotalNutrients.addToQuantity(nutrient, quantity);
    }

    public void addToRunningTotalDailyNutrientsValue(String nutrient, Double quantity){
        runningTotalsTotalDaily.addToQuantity(nutrient, quantity);
    }

    public Map<String, Nutrient> getRunningTotalNutrientsList(){
        return runningTotalsTotalNutrients.getNutrientList();
    }

    public Map<String, Nutrient> getRunningTotalDailyList(){
        return runningTotalsTotalDaily.getNutrientList();
    }


    public Double getRunningTotalDailyQuantity(String nutrient){
        return runningTotalsTotalDaily.getQuantity(nutrient);
    }

    public Double getRunningTotalQuantity(String nutrient){
        return runningTotalsTotalNutrients.getQuantity(nutrient);
    }

}
