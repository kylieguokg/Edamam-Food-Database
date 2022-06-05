package project.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OutputOnlineTest{

    private OutputHTTP outputHTTP;
    private OutputJsonParser outputJsonParser;
    private Output output;

    private Gson gson;

    private Observer observer;

    @BeforeEach
    public void setup(){

        this.outputHTTP = mock(OutputHTTP.class);
        this.outputJsonParser = new OutputJsonParser();
        this.observer = mock(Observer.class);
        this.output = new OutputOnline(outputHTTP, outputJsonParser);

        output.subscribe(observer);


    }


    @Test
    /***
     * Checks that an update notification is sent to the observer when an error output occurs
     */
    public void outputErrorTest(){

        when(outputHTTP.output(any())).thenReturn("ERROR: Something went wrong");
        output.output("text");

        // should been reset
        verify(observer).update();
        assertEquals(output.getErrorMSG(), null);
        assertEquals(output.getOutputLink(), null);
    }



    @Test
    /***
     * Checks that an update notification is sent to the observer when an error output occurs
     */
    public void outputValidTest(){

        when(outputHTTP.output(any())).thenReturn("ok");
        when(outputHTTP.getOutputLink()).thenReturn("link");
        output.output("text");

        // should been reset
        verify(observer).update();
        assertEquals(output.getErrorMSG(), null);
        assertEquals(output.getOutputLink(), null);


    }



    @Test
    /***
     * Checks that an update notification is sent to the observer when an error output is sent
     */
    public void postToRedditErrorTest(){

        when(outputHTTP.postToReddit(any(), any())).thenReturn("ERROR: Something went wrong");
        output.postToReddit("text");

        // should been reset
        verify(observer).update();
        assertEquals(output.getErrorMSG(), null);
        assertEquals(output.getOutputLink(), null);
    }



    @Test
    /***
     * Checks that an update notification is sent to the observer when the output is posted to reddit
     */
    public void postToRedditValidTest(){

        String out = "{\"jquery\": [[0, 1, \"call\", [\"body\"]], [1, 2, \"attr\", \"find\"], [2, 3, \"call\", [\".status\"]], [3, 4, \"attr\", \"hide\"], [4, 5, \"call\", []], [5, 6, \"attr\", \"html\"], [6, 7, \"call\", [\"\"]], [7, 8, \"attr\", \"end\"], [8, 9, \"call\", []], [1, 10, \"attr\", \"redirect\"], [10, 11, \"call\", [\"https://www.reddit.com/r/u_kylieg_uni/comments/uszk64/test_post_title/\"]], [1, 12, \"attr\", \"find\"], [12, 13, \"call\", [\"*[name=url]\"]], [13, 14, \"attr\", \"val\"], [14, 15, \"call\", [\"\"]], [15, 16, \"attr\", \"end\"], [16, 17, \"call\", []], [1, 18, \"attr\", \"find\"], [18, 19, \"call\", [\"*[name=text]\"]], [19, 20, \"attr\", \"val\"], [20, 21, \"call\", [\"\"]], [21, 22, \"attr\", \"end\"], [22, 23, \"call\", []], [1, 24, \"attr\", \"find\"], [24, 25, \"call\", [\"*[name=title]\"]], [25, 26, \"attr\", \"val\"], [26, 27, \"call\", [\" \"]], [27, 28, \"attr\", \"end\"], [28, 29, \"call\", []]], \"success\": true}";

        when(outputHTTP.postToReddit(any(), any())).thenReturn(out);
        output.postToReddit("text");

        // should been reset
        verify(observer).update();
        assertEquals(output.getErrorMSG(), null);
        assertEquals(output.getRedditOutputLink(), null);

    }

    @Test
    /***
     * Checks that an update notification is sent to the observer when there is an error with parsing the Json string
     */
    public void postToRedditErrorParseTest(){

        String httpError = "{'error':'Too many requests!'}";

        when(outputHTTP.postToReddit(any(), any())).thenReturn(httpError);

        output.postToReddit("text");

        // should been reset
        verify(observer).update();
        assertEquals(output.getErrorMSG(), null);
        assertEquals(output.getRedditOutputLink(), null);

    }

    @Test
    /***
     * Checks that an update notification is sent to the observer when there is an error with getting token
     */
    public void getAccessTokenError(){

        String httpError = "ERROR: Invalid URL";

        when(outputHTTP.getAccessToken(any(), any())).thenReturn(httpError);

        output.getAccessToken("user", "password");

        // should been reset
        verify(observer).update();
        assertEquals(output.getErrorMSG(), null);
        assertEquals(output.getRedditLoggedUserIn(), null);

    }

    @Test
    /***
     * Checks that an update notification is sent to the observer when there is an error with getting token
     */
    public void getAccessTokenParseError(){

        String httpError = "{'error':'Too many requests!'}";

        when(outputHTTP.getAccessToken(any(), any())).thenReturn(httpError);

        output.getAccessToken("user", "password");

        // should been reset
        verify(observer).update();
        assertEquals(output.getErrorMSG(), null);
        assertEquals(output.getRedditLoggedUserIn(), null);

    }

    @Test
    /***
     * Checks that an update notification is sent to the observer when a token is gotten
     */
    public void getAccessTokenValid(){

        String token = "{\"access_token\": \"1828605230871-HYVZO00rw8o5qRPr92eM8U1BxUv4Bg\", \"token_type\": \"bearer\", \"expires_in\": 86400, \"scope\": \"*\"}";

        when(outputHTTP.getAccessToken(any(), any())).thenReturn(token);

        output.getAccessToken("user", "password");

        // should been reset
        verify(observer).update();
        assertEquals(output.getErrorMSG(), null);
        assertEquals(output.getRedditLoggedUserIn(), null);

    }




}