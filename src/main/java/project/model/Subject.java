package project.model;

/***
 * A state change in a Subject results in a state change in an Observer
 */
public interface Subject {

    /***
     * Notifies observers of a state change in Subject
     */
    public void notifyObservers();

    /***
     * Add an observer to subject's list of observers
     * @param observer to add
     */
    public void subscribe(Observer observer);

    /***
     * Removes an observer from a subject's list of observers
     * @param observer to remove
     */
    public void unsubscribe(Observer observer);
}
