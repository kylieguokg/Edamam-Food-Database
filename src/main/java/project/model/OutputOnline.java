package project.model;

import java.util.ArrayList;
import java.util.List;


/***
 * Manages the sending of output that requires the internet
 */
public class OutputOnline implements Output{

    /***
     * Manages sending the output report to PasteBin and Reddit
     */
    private OutputHTTP outputHTTP;

    /***
     * Helper class to convert json results from Reddit to get accessers
     */
    private OutputJsonParser outputJsonParser;

    /***
     * Access token that allows user to post to Reddit from app
     */
    private String redditAccessToken;

    /***
     * List of observers who need to be updated if output state is changed
     */
    private List<Observer> observerList;

    /***
     * State of a user's attempt to log in
     */
    private String redditUserLoggedIn;


    /***
     * Link to Reddit post of user's saved foods
     */
    private String redditOutputLink;

    /***
     * Link to Pastebin post of user's saved foods
     */
    private String pastebinOutputLink;

    /***
     * If reddit sucessfully logged in;
     */
    private boolean redditLoggedIn;

    private String errorMSG;

    public OutputOnline(OutputHTTP outputHTTP, OutputJsonParser outputJsonParser){
        this.outputHTTP = outputHTTP;
        this.outputJsonParser = outputJsonParser;
        this.observerList = new ArrayList<Observer>();
    }

    @Override
    public void output(String text){

        String out = outputHTTP.output(text);
        if (out.startsWith("ERROR")){
            errorMSG = out;
            notifyObservers();
        } else {
            pastebinOutputLink = outputHTTP.getOutputLink();
            notifyObservers();
        }

    }

    @Override
    public String getOutputLink(){
        return pastebinOutputLink;
    }

    @Override
    public boolean isUserLoggedInReddit(){
       return redditLoggedIn;
    }

    @Override
    public void getAccessToken(String username, String password){

        String out = outputHTTP.getAccessToken(username, password);

        // if there was a error with HTTP
        if (out.startsWith("ERROR")){
            errorMSG = out;
            notifyObservers();
            return;
        } else {

            redditAccessToken = outputJsonParser.getAccessToken(out);

            // error found in parsing
            if (redditAccessToken ==  null || redditAccessToken.startsWith("ERROR")){
                errorMSG = redditAccessToken;
                notifyObservers();
            } else {
                redditUserLoggedIn = "ok";
                redditLoggedIn = true;
                notifyObservers();
            }

        }

    }

    @Override
    public void postToReddit(String output){

        String out = outputHTTP.postToReddit(output, redditAccessToken);

        // if there was a error with HTTP
        if (out.startsWith("ERROR")){
            errorMSG = out;
            notifyObservers();
            return;
        } else {

            out = outputJsonParser.getOutputLink(out);

            // error found in parsing
            if (out ==  null || out.startsWith("ERROR")){
                errorMSG = out;
                notifyObservers();
            } else {
                redditOutputLink = out;
                notifyObservers();
            }

        }

    }

    @Override
    public void notifyObservers() {

        for (Observer observer : observerList){
            observer.update();
        }

        // reset states
        pastebinOutputLink = null;
        redditOutputLink = null;
        redditUserLoggedIn = null;
        errorMSG = null;

    }

    @Override
    public void subscribe(Observer observer){
        observerList.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer){
        if (observerList.contains(observer)){
            observerList.remove(observer);
        }
    }

    @Override
    public String getRedditLoggedUserIn(){
        return redditUserLoggedIn;
    }


    @Override
    public String getRedditOutputLink(){
        return redditOutputLink;
    }


    @Override
    public String getErrorMSG(){
        return errorMSG;
    }


}
