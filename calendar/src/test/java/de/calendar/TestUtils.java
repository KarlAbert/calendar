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

    private static String buildUrl(String stringUrl, String token) {
        stringUrl += !stringUrl.contains("?") ? "?" : "&";
        stringUrl += "token=";
        stringUrl += token;

        return stringUrl;
    }

    public static Response post(String stringURL, String token, JSONObject data) {
        return post(buildUrl(stringURL, token), data);
    }

    public static Response post(String stringURL, JSONObject data) {
        return readResponse(stringURL, data, "POST");
    }

    public static Response get(String stringURL, String token) {
        return get(buildUrl(stringURL, token));
    }

    public static Response get(String stringURL) {
        return readResponse(stringURL, null, "GET");
    }

    private static Response readResponse(String stringURL, JSONObject data, String httpMethod) {
        try {
            //post Data
            if (!DOMAIN.endsWith("/") && !stringURL.startsWith("/")) {
                stringURL = "/" + stringURL;
            }
            HttpURLConnection connection = (HttpURLConnection) new URL(DOMAIN + stringURL).openConnection();
            connection.setRequestMethod(httpMethod);
            if (data != null) {
                connection.setRequestProperty("Data", data.toString());
            }

            //Get Response
            BufferedReader br;
            //System.out.println(connection.getResponseCode());
            if (connection.getResponseCode() <= 200 || connection.getResponseCode() >= 300) {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
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
