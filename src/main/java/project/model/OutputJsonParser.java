package project.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/***
 * Helper class to convert json results from Reddit to get accessers
 */
public class OutputJsonParser {

    private Gson gson;

    public OutputJsonParser(){
        this.gson = new Gson();
    }

    /***
     * Converts result of HTTP call to get an output link for post
     * @param in result of HTTP call
     * @return link if successful, else an error message
     */
    public String getOutputLink(String in){

        if (in == null){
            return "ERROR";
        }

        JsonElement outputJE =  gson.fromJson(in, JsonElement.class);

        if (outputJE == null || outputJE.getAsJsonObject() == null){
            return "ERROR";
        }

        if (outputJE.getAsJsonObject().get("success") != null){

            if (outputJE.getAsJsonObject().get("success").getAsBoolean() == true){
                // Get the post link
                return "Here is your Reddit post link: " +
                        outputJE.getAsJsonObject().get("jquery")
                        .getAsJsonArray().get(10)
                        .getAsJsonArray().get(3)
                        .getAsJsonArray().get(0).getAsString();
            }

        } else if (outputJE.getAsJsonObject().get("error") != null){

            return "ERROR: " + outputJE.getAsJsonObject().get("error").getAsString();
        }

        return "ERROR";


    }

    /***
     * Converts result of HTTP call to get a Reddit access token
     * @param in result of HTTP call
     * @return access token if successful, else an error message
     */
    public String getAccessToken(String in){

        if (in == null){
            return "ERROR";
        }

        JsonElement outputJE =  gson.fromJson(in, JsonElement.class);

        if (outputJE == null || outputJE.getAsJsonObject() == null){
            return "ERROR";
        } else if (outputJE.getAsJsonObject().get("access_token") != null){
            return outputJE.getAsJsonObject().get("access_token").getAsString();
        } else if (outputJE.getAsJsonObject().get("error") != null){

            if (outputJE.getAsJsonObject().get("error").getAsString().equals("invalid_grant")){
                return "ERROR: Username or password is incorrect";
            }

            return "ERROR: " + outputJE.getAsJsonObject().get("error").getAsString();
        }

        return "ERROR";
    }
}
