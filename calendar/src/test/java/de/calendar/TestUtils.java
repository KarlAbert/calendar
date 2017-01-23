package de.calendar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
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
            //post data
            if (!DOMAIN.endsWith("/") && !stringURL.startsWith("/")) {
                stringURL = "/" + stringURL;
            }
            String url = DOMAIN + stringURL;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(httpMethod);
            if (data != null) {
                connection.setRequestProperty("Content-type", "application/json");
                connection.setDoOutput(true);
                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.write(data.toString().getBytes());
                    wr.close();
                }
            }

            //Get Response
            BufferedReader br;
            if (connection.getResponseCode() == 404) {
                throw new FileNotFoundException(String.format("Die Datei %s konnte nicht gefunden werden.", stringURL));
            } else if ((connection.getResponseCode() < 200 || connection.getResponseCode() >= 300)) {
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
            throw new RuntimeException(e);
        }
    }

    public static String tryLogin(String token, String username, String password) {
        if (token == null) {
            JSONObject jsonObject = login(username, password).getJSONObject();
            token = jsonObject.getString("token");
        }
        return token;
    }

    public static String tryLogin(String token) {
        return tryLogin(token, testUsername, testUserPassword);
    }

    public static String forceLogin(String token, String username, String password) {
        try {
            return tryLogin(token, username, password);
        } catch (JSONException e) {
            tryRegister("Teeeeest", "Usssser", username, username + "@example.com", password);
            return tryLogin(token, username, password);
        }
    }

    private static Response tryRegister(String firstname, String lastname, String username, String email, String password) {
        JSONObject json = new JSONObject()
                .put("firstname", firstname)
                .put("lastname", lastname)
                .put("username", username)
                .put("email", email)
                .put("password", password);

        return TestUtils.post("/register", json);
    }

    public static Response login(String username, String password) {
        JSONObject data = new JSONObject()
                .put("username", username)
                .put("password", password);

        return TestUtils.post("/login", data);
    }
}
