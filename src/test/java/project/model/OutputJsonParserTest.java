package project.model;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class OutputJsonParserTest {


    private Gson gson;
    private OutputJsonParser outputJsonParser;

    @BeforeEach
    public void setup() {

        this.outputJsonParser = new OutputJsonParser();
        gson = new Gson();

    }


    @Test
    /***
     * When there is no output link given
     */
    public void getOutputLinkNull() {

        assertEquals(outputJsonParser.getOutputLink(null), "ERROR");

    }


    @Test
    /***
     * When there is a valid output link given
     */
    public void getOutputLinkValid() {

        String out = "{\"jquery\": [[0, 1, \"call\", [\"body\"]], [1, 2, \"attr\", \"find\"], [2, 3, \"call\", [\".status\"]], [3, 4, \"attr\", \"hide\"], [4, 5, \"call\", []], [5, 6, \"attr\", \"html\"], [6, 7, \"call\", [\"\"]], [7, 8, \"attr\", \"end\"], [8, 9, \"call\", []], [1, 10, \"attr\", \"redirect\"], [10, 11, \"call\", [\"https://www.reddit.com/r/u_kylieg_uni/comments/uszk64/test_post_title/\"]], [1, 12, \"attr\", \"find\"], [12, 13, \"call\", [\"*[name=url]\"]], [13, 14, \"attr\", \"val\"], [14, 15, \"call\", [\"\"]], [15, 16, \"attr\", \"end\"], [16, 17, \"call\", []], [1, 18, \"attr\", \"find\"], [18, 19, \"call\", [\"*[name=text]\"]], [19, 20, \"attr\", \"val\"], [20, 21, \"call\", [\"\"]], [21, 22, \"attr\", \"end\"], [22, 23, \"call\", []], [1, 24, \"attr\", \"find\"], [24, 25, \"call\", [\"*[name=title]\"]], [25, 26, \"attr\", \"val\"], [26, 27, \"call\", [\" \"]], [27, 28, \"attr\", \"end\"], [28, 29, \"call\", []]], \"success\": true}";

        assertTrue(outputJsonParser.getOutputLink(out).equals("Here is your Reddit post link: https://www.reddit.com/r/u_kylieg_uni/comments/uszk64/test_post_title/"));

    }


    @Test
    /***
     * When an error JSON was given
     */
    public void getOutputLinkError() {

        String httpError = "{'error':'Too many requests!'}";

        assertTrue(outputJsonParser.getOutputLink(httpError).equals("ERROR: Too many requests!"));

    }

    @Test
    /***
     * Checks that an error msg is returned if null is given to the parser
     */
    public void getAccessTokenNull() {
        assertEquals(outputJsonParser.getAccessToken(null), "ERROR");
    }

    @Test
    /***
     * Checks that the correct token is returned if a Json containing it is given
     */
    public void getAccessTokenValid() {

        String access = "{\"access_token\": \"1828605230871-PqnCQkw9V0oD0qXysm2-dmfemTdkMw\", \"token_type\": \"bearer\", \"expires_in\": 86400, \"scope\": \"*\"}\n";

        assertEquals(outputJsonParser.getAccessToken(access), "1828605230871-PqnCQkw9V0oD0qXysm2-dmfemTdkMw");
    }


    @Test
    /***
     * Checks the correct error msg is returned if a Json string with an error is given to it
     */
    public void getAccessTokenError() {

        String httpError = "{'error':'Too many requests!'}";

        assertTrue(outputJsonParser.getAccessToken(httpError).equals("ERROR: Too many requests!"));
    }

    @Test
    /***
     * Checks the correct error msg is returned if the user did not login correctly
     */
    public void getAccessTokenInvalidLogin() {

        String httpError = "{'error':'invalid_grant'}";

        assertTrue(outputJsonParser.getAccessToken(httpError).equals("ERROR: Username or password is incorrect"));
    }


}