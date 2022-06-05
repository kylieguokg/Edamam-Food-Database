package project.model;

import java.util.ArrayList;
import java.util.List;

/***
 * Returns the output as it is (since not connected to internet)
 */
public class OutputDummy implements Output {

    private List<Observer> observerList = new ArrayList<Observer>();
    private String redditUserLoggedIn;
    private String redditOutputLink;
    private String pastebinOutputLink;
    private String errorMSG;

    @Override
    public void output(String output){
        this.pastebinOutputLink = output;
        notifyObservers();
    }

    @Override
    public String getOutputLink(){
        return pastebinOutputLink;
    }

    @Override
    public void getAccessToken(String username, String password){
        notifyObservers();
    }

    @Override
    public boolean isUserLoggedInReddit(){
        return true;
    }

    @Override
    public void postToReddit(String output){
        this.redditOutputLink = output;
        notifyObservers();
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
