package project.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * Manages the process of searching for a food
 */
public class SearchState implements  Subject{

    /***
     * Stores mechanism to retrieve search results from Edanam API
     */
    private FoodDB foodDB;

    /***
     * Stores the search results of current search
     */
    private List<Food> searchResults;

    /***
     * List of observers who need to be updated if the search state is changed
     */
    private List<Observer> observerList;

    /***
     * State of search by user
     */
    private String searchState;

    public SearchState(FoodDB foodDB){
        this.foodDB = foodDB;
        observerList = new CopyOnWriteArrayList<Observer>();
    }

    public List<Food> getSearchResults(){
        searchResults = foodDB.getSearchResults();
        return searchResults;
    }

    @Override
    public void unsubscribe(Observer observer){
        if (observerList.contains(observer)){
            observerList.remove(observer);
        }
    }

    /***
     * Used for searching for an ingredient in the database
     * @param ingredient ingredient to be searched
     * @return search result (if search was successful or if there were any errors)
     */
    public void enterAnIngredient(String ingredient){

        searchState = foodDB.enterAnIngredient(ingredient);
        notifyObservers();
    }

    @Override
    public void notifyObservers() {

        for (Observer observer : observerList){
            observer.update();
        }

        // reset
        searchState = null;
    }

    @Override
    public void subscribe(Observer observer){
        observerList.add(observer);
    }

    public String getSearchState() {
        return searchState;
    }
}
