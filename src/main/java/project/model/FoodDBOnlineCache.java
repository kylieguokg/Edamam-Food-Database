package project.model;

import com.google.gson.Gson;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/***
 * Manages the retrieval and storing of data in SQLite database
 */
public class FoodDBOnlineCache {

    private static final String dbName = "foodDB.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;
    private String currentUser;
    private Gson gson;

    public FoodDBOnlineCache(){
        gson = new Gson();
    }


    /***
     * Creates user table, cached foods table and saved foods table
     * User table - stores the username, password, and personalisation info for a user
     * Cached Foods table - stores all the nutritional information for a food
     * Saved Foods table - stores the saved foods of users
     */
    public static void createDatabaseWithSchema(){

        File dbFile = new File(dbName);

        if (dbFile.exists()) {
            return;
        }

        String createUsersTableSQL =
                """
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT UNIQUE,
                    password TEXT NOT NULL,
                    personalisation TEXT NOT NULL,
                    PRIMARY KEY (username)
                );
                """;

        String createCachedFoodsTableSQL =
                """
                    CREATE TABLE IF NOT EXISTS cachedFoods (
                        foodId TEXT NOT NULL,
                        measureURI TEXT NOT NULL,
                        quantity INTEGER NOT NULL,
                      	foodInfo TEXT NOT NULL,
                        PRIMARY KEY (foodId, measureURI, quantity)
                    );
                """;

        String createSavedFoodsTableSQL =
                """
                    CREATE TABLE IF NOT EXISTS savedFoods (
                        username TEXT,
                        foodId TEXT,
                        foodInfo TEXT NOT NULL,
                        PRIMARY KEY (username, foodId),
                        FOREIGN KEY (username) REFERENCES users (username)
                                                                   ON DELETE
                                                                   CASCADE
                    );
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {

            // If we get here that means no exception raised from getConnection - meaning it worked
            conn.createStatement().execute("PRAGMA foreign_keys = ON");

            statement.execute(createUsersTableSQL);
            statement.execute(createCachedFoodsTableSQL);
            statement.execute(createSavedFoodsTableSQL);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }


    /***
     * Creates a new user, stores their username and hashed password
     * @param username username (plain text)
     * @param password password (hashed)
     * @param user user to store
     * @return if it was successful
     */
    public String createUser(String username, String password, User user){

        String createUserSQL =
                """
                INSERT OR IGNORE INTO users (username, password, personalisation)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement =
                     conn.prepareStatement(createUserSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, gson.toJson(user));

            preparedStatement.executeUpdate();

            return "Success";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
            return "Sorry, couldn't add user";
        }
    }


    /***
     * Stores a selected food in the cachedFood table
     * @param selectedFood food to be stored
     * @param quantity quantity of food
     * @return if it was successful
     */
    public void cacheSelectedFood(Food selectedFood, int quantity){


        String cacheFoodSQL =
                """
                INSERT OR IGNORE INTO cachedFoods (foodId, measureURI, quantity, foodInfo)
                VALUES (?, ?, ?, ?);
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement =
                     conn.prepareStatement(cacheFoodSQL)) {

            preparedStatement.setString(1, selectedFood.getFoodId());
            preparedStatement.setString(2, selectedFood.getSelectedMeasure().getUri());
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, gson.toJson(selectedFood));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("save food");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /***
     * Stores user's saved food in savedFoods table
     * @param username  user whose food is being saved
     * @param selectedFood food to save
     */
    public String saveFood(String username, Food selectedFood){

        String saveFoodSQL =
                """
                INSERT OR IGNORE INTO savedFoods (username, foodId, foodInfo)
                VALUES (?, ?, ?);
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement =
                     conn.prepareStatement(saveFoodSQL)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, selectedFood.getFoodId());
            preparedStatement.setString(3, gson.toJson(selectedFood));
            preparedStatement.executeUpdate();

            return "ok";

        } catch (SQLException e) {
            System.out.println("save food");
            System.out.println(e.getMessage());
            return "Could not save food to database";

        }
    }

    /***
     * Gets total nutritional information for a food, measure and quantity
     * @param foodId unique id for food
     * @param measureURI unique URI for measure
     * @param quantity quantity of food
     * @return results of query
     */
    public List<String> getCachedFood(String foodId, String measureURI, int quantity){

        String cachedFoodSQL =
                """
                SELECT *
                FROM cachedFoods
                WHERE foodId = ? AND measureURI = ? AND quantity = ?;
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement =
                     conn.prepareStatement(cachedFoodSQL)) {

            conn.createStatement().execute("PRAGMA foreign_keys = ON");

            preparedStatement.setString(1, foodId);
            preparedStatement.setString(2, measureURI);
            preparedStatement.setInt(3, quantity);

            ResultSet results = preparedStatement.executeQuery();

            // if no nutrient information is saved for that food and measure
            List<String> out = new ArrayList<String>();

            while (results.next()) {
                out.add(results.getString("foodInfo"));
            }

            return out;

            } catch (SQLException e) {
            System.out.println(e.getMessage());

            return null;
        }

    }

    /***
     * Gets list of saved foods for user
     * @param username username of user
     * @return results of query
     */
    public List<String> getSavedFoods(String username){

        String savedFoodSQL =
                """
                SELECT *
                FROM savedFoods
                WHERE username = ?;
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement =
                     conn.prepareStatement(savedFoodSQL)) {

            conn.createStatement().execute("PRAGMA foreign_keys = ON");

            preparedStatement.setString(1, username);


            ResultSet results = preparedStatement.executeQuery();

            List<String> out = new ArrayList<String>();

            while (results.next()) {
                out.add(results.getString("foodInfo"));

            }

            return out;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
            return null;
        }

    }

    /***
     * Clears the cached of searched foods
     */
    public void clearCache(){

        String clearCacheSQL =
                """
               DELETE FROM cachedFoods;
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement =
                     conn.prepareStatement(clearCacheSQL)) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("save food");
            System.out.println(e.getMessage());
            System.exit(-1);
        }


    }

    /***
     * Tries to find a matching user given a username and password
     * @param username (plain text)
     * @param password (hashed)
     * @return results of query
     */
    public List<String> login(String username, String password){

        String loginSQL =
                """
                SELECT *
                FROM users
                WHERE username = ? AND password = ?;
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement =
                     conn.prepareStatement(loginSQL)) {

            conn.createStatement().execute("PRAGMA foreign_keys = ON");

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet results = preparedStatement.executeQuery();

            List<String> out = new ArrayList<String>();

            while (results.next()) {
                out.add(results.getString("personalisation"));
            }

            return out;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
            return null;
        }

    }

    /***
     * Checks if username
     * @param username
     * @return results of query
     */
    public boolean checkUsernameExists(String username){

        String loginSQL =
                """
                SELECT *
                FROM users
                WHERE username = ?;
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement =
                     conn.prepareStatement(loginSQL)) {

            conn.createStatement().execute("PRAGMA foreign_keys = ON");

            preparedStatement.setString(1, username);

            ResultSet results = preparedStatement.executeQuery();

            List<String> out = new ArrayList<String>();

            if (results.next()){
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
            return false;
        }

    }


    /***
     * Updates the personalisation information for a user
     * @param user user whose information is being updated
     */
    public void updatePersonalisation(User user){

        String updatePersonalisationSQL =
                """
                UPDATE users 
                SET personalisation = ?
                WHERE username = ?;
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement =
                     conn.prepareStatement(updatePersonalisationSQL)) {

            conn.createStatement().execute("PRAGMA foreign_keys = ON");

            preparedStatement.setString(1, gson.toJson(user));
            preparedStatement.setString(2, user.getUsername());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

    }



}
