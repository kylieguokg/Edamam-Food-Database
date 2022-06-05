package project.model;

/***
 * Used to observe a state change
 */
public interface Observer {

    /***
     * Update own state in accordance with the state change of the Subject
     */
    void update();

}
