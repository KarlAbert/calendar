package de.calendar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * calendar:
 * * de.calendar:
 * * * Created by KAABERT on 18.01.2017.
 */
public class TestUtils {

    private static String DOMAIN = "http://localhost:8080";
    private static String testUsername = "TestUser1";
    private static String testUserPassword = "Password1";

    public static Response post(String stringUrl, String token, JSONObject data) {
        stringUrl += !stringUrl.contains("?") ? "?" : "&";
        stringUrl += "token=";
        stringUrl += token;

        return post(stringUrl, data);
    }

    public static Response post(String stringURL, JSONObject data) {
        try {
            //post Data
            HttpURLConnection connection = (HttpURLConnection) new URL(DOMAIN + stringURL).openConnection();
            connection.setRequestMethod("POST");
            if (data != null) {
                connection.setRequestProperty("Data", data.toString());
            }

            //Get Response
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String responseBody = "", line;
            while ((line = br.readLine()) != null) {
                responseBody += line;
                responseBody += '\r';
            }
            br.close();

            return new Response(connection.getResponseCode(), responseBody);
        } catch (IOException e) {
            //e.printStackTrace();
            /*if(e.getCause().getClass().equals(ConnectException.class)) {
                throw new RuntimeException(e);
            }*/
            throw new RuntimeException(e);
        }
    }

    public static String tryLogin(String token) {
        if (token == null) {
            login(testUsername, testUserPassword);
        }
        return token;
    }

    public static Response login(String username, String password) {
        JSONObject data = new JSONObject()
                .put("username", username)
                .put("password", password);

        return TestUtils.post("/login", data);
    }
}
