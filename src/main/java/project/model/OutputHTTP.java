package project.model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/***
 * Manages sending the output report to PasteBin and Reddit
 */
public class OutputHTTP  {

    String PASTEBIN_API_KEY  = System.getenv("PASTEBIN_API_KEY");
    String REDDIT_CLIENT_ID = System.getenv("REDDIT_CLIENT_ID");
    String REDDIT_SECRET = System.getenv("REDDIT_SECRET");
    String reddit_username;
    String reddit_password;

    Map<String, String> errorResponses = new LinkedHashMap<String, String>();
    private String outputLink;

    public OutputHTTP(){
        // Pastebin error responses
        errorResponses.put("Bad API request, invalid api_option", "You can only paste");
        errorResponses.put("Bad API request, invalid api_dev_key", "Invalid api_dev_key");
        errorResponses.put("Bad API request, maximum number of 25 unlisted pastes for your free account", "Maximum number of 25 unlisted pastes for your free account");
        errorResponses.put("Bad API request, maximum number of 10 private pastes for your free account", "Maximum number of 10 private pastes for your free account");
        errorResponses.put("Bad API request, api_paste_code was empty", "You have no saved recipes");
        errorResponses.put("Bad API request, maximum paste file size exceeded", "Maximum file size exceeded");
        errorResponses.put("Bad API request, Post limit, maximum pastes per 24h reached ", "Maximum pastes per 24h reached");

    }

    /***
     * Requests Pastebin to make a post with the output text
     * @param text list of saved foods output text
     * @return if operation was successful
     */
    public String output(String text){

        if (PASTEBIN_API_KEY == null){
            return "ERROR: You have not set your environment variable PASTEBIN_API_KEY";
        }

        {
            try {

                HttpRequest request = HttpRequest.newBuilder(new URI("https://pastebin.com/api/api_post.php"))
                        .header("Content-type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString( "api_dev_key=" + PASTEBIN_API_KEY +
                                "&api_paste_code=" + text +
                                "&api_option=paste"
                        ))
                        .build();

                HttpClient client = HttpClient.newBuilder().build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                String errorResponse = errorResponses.get(response.body());
                if (errorResponse != null){
                    return "ERROR: " + errorResponse;
                } else if (response.body().startsWith("Bad API request")){
                    return  "ERROR: " + response.body();
                }

                outputLink = response.body();

                return "ok";

            } catch (IOException | InterruptedException e) {
                return "ERROR: Something went wrong with our request!";

            } catch (URISyntaxException ignored) {
                // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
                // hard-coded and so needs a way to be checked for correctness at runtime.
                return "ERROR: Invalid URL";
            }


        }
    }

    /***
     * Output link of Pastebin post
     */
    public String getOutputLink(){
        return "Here is your paste link: " + outputLink;
    }


    /***
     * Attempts to retrieve an access token from Reddit
     * @param username Reddit username
     * @param password Reddit password
     * @return whether or not operation was successful
     */
    public String getAccessToken(String username, String password){

        if (REDDIT_CLIENT_ID == null || REDDIT_SECRET == null) {
            return "ERROR: Sorry you have not set your environment variables REDDIT_CLIENT_ID and REDDIT_SECRET";
        }


        String full = REDDIT_CLIENT_ID + ":" + REDDIT_SECRET;
        String encoded = Base64.getEncoder().encodeToString(full.getBytes());

        try {

            HttpRequest request = HttpRequest.newBuilder(new URI("https://www.reddit.com/api/v1/access_token"))
                    .header("User-Agent", "javafx:edanam-soft3202 (by /u/kylieg_uni")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic " + encoded)
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "grant_type=password" +
                                    "&username=" + username +
                                    "&password=" + password
                    ))
                    .build();

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            reddit_username = username;
            reddit_password = password;

            return response.body();

        } catch (IOException | InterruptedException e) {
            return "ERROR: Something went wrong with our request!";

        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
            return "ERROR: Invalid URL";
        }


    }

    /***
     * Attempts to make a post on Reddit with list of saved foods output
     * @param output List of saved foods output
     * @param accessToken Reddit access token
     * @return whether or not operation was successful
     */
    public String postToReddit(String output, String accessToken){

        try {

            HttpRequest request = HttpRequest.newBuilder(new URI("https://oauth.reddit.com/api/submit"))
                    .header("User-Agent", "javafx:edanam-soft3202 (by /u/kylieg_uni")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Bearer " + accessToken)
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "title=Your Saved Foods and Total Nutritional Information" +
                                    "&text=" + output +
                                    "&sr=u_" + reddit_username +
                                    "&kind=self"

                    ))
                    .build();

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (IOException | InterruptedException e) {
            return "ERROR: Something went wrong with our request!";

        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
            return "ERROR: Invalid URL";
        }


    }





}
