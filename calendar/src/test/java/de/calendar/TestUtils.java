package de.calendar;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * calendar:
 * * de.calendar.cucumber:
 * * * Created by KAABERT on 26.01.2017.
 */
class TestUtils {

    private static final String DOMAIN = "http://localhost:8080";

    static Response post(String url, String token, JSONObject data) {
        return readResponse(url, token, data, "POST");
    }

    static Response put(String url, String token, JSONObject data) {
        return readResponse(url, token, data, "PUT");
    }

    static Response delete(String url, String token) {
        return readResponse(url, token, null, "DELETE");
    }

    static Response get(String url, String token) {
        return readResponse(url, token, null, "GET");
    }

    private static Response readResponse(String stringURL, String token, JSONObject data, String httpMethod) {
        try {
            //post data
            if (!DOMAIN.endsWith("/") && !stringURL.startsWith("/")) {
                stringURL = "/" + stringURL;
            }
            String url = DOMAIN + stringURL.replaceAll(" ", "+");
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(httpMethod);
            if (token != null) {
                connection.setRequestProperty("Authorization", token);
            }
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
}
