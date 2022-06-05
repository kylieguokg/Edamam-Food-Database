package project.model;

/***
 * Sends the report of the list of currently selected ingredients
 * (name, measure, value) along with the total nutritional value.
 */
public interface Output extends Subject {

    /***
     * Sends the report of the list of currently selected ingredients
     * (name, measure, value) along with the total nutritional value.
     */
    public void output(String output);

    /***
     * Returns the link to the output
     * (Pastebin if online, otherwise just returns the report)
     * @return output report
     */
    public String getOutputLink();

    /***
     * Retrieves a Reddit Access token for user using their username and password
     * @param username Reddit username
     * @param password Reddit password
     */
    public void getAccessToken(String username, String password);

    /***
     * Returns whether or not a user is logged in to Reddit
     */
    public boolean isUserLoggedInReddit();

    /***
     * Posts to Reddit a post of their list of saved foods and total nutritional value
     */
    public void postToReddit(String output);


    /***
     * Gets the link of the Reddit output post
     * @return link of Reddit output post
     */
    public String getRedditOutputLink();


    /***
     * Returns the state of a user log in attempt
     * @return state of a user log in attempt
     */
    public String getRedditLoggedUserIn();

    /***
     * Gets the error message from an output attempt
     * @return error message from an output attempt
     */
    public String getErrorMSG();


}
