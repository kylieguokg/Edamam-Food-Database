package project.model;

/***
 * Short version of nutrients for a food
 */
public class Nutrients {

    /***
     * Calories
     */
    private double ENERC_KCAL;

    /***
     * Protein
     */
    private double PROCNT;

    /***
     * Fat
     */
    private double FAT;

    /***
     * Carbohydrates (by difference)
     */
    private double CHOCDF;

    /***
     * Fibre
     */
    private double FIBTG;

    public Nutrients(double enerc_kcal, double procnt, double fat, double chocdf, double fibtg) {
        ENERC_KCAL = enerc_kcal;
        PROCNT = procnt;
        FAT = fat;
        CHOCDF = chocdf;
        FIBTG = fibtg;
    }

    /***
     * @return Calories
     */
    public double getENERC_KCAL() {
        return ENERC_KCAL;
    }

    public void setENERC_KCAL(int ENERC_KCAL) {
        this.ENERC_KCAL = ENERC_KCAL;
    }

    /***
     * @return Protein
     */
    public double getPROCNT() {
        return PROCNT;
    }

    public void setPROCNT(double PROCNT) {
        this.PROCNT = PROCNT;
    }

    /***
     * @return Fat
     */
    public double getFAT() {
        return FAT;
    }

    public void setFAT(double FAT) {
        this.FAT = FAT;
    }

    /***
     * @return Carbohydrates (by difference)
     */
    public double getCHOCDF() {
        return CHOCDF;
    }

    public void setCHOCDF(double CHOCDF) {
        this.CHOCDF = CHOCDF;
    }

    /***
     * @return Fibre
     */
    public double getFIBTG() {
        return FIBTG;
    }

    public void setFIBTG(int FIBTG) {
        this.FIBTG = FIBTG;
    }

    public String toString(){
        return "Energy: " + ENERC_KCAL + " | Protein: " + PROCNT +
                "\nFat: " + FAT + " | Carbohydrate, by difference: " + CHOCDF +
                "\nFibre, total dietary: "  + FIBTG;
    }
}
