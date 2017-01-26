package de.calendar;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * calendar:
 * * de.calendar.cucumber:
 * * * Created by KAABERT on 26.01.2017.
 */
class TestUtils {

    private static final String DOMAIN = "http://localhost:8080";

    static Response post(String url, String token, JSONObject data) {
        return readResponse(sendRequest(url, token, data, "POST"));
    }

    static Response put(String url, String token, JSONObject data) {
        return readResponse(sendRequest(url, token, data, "PUT"));
    }

    static Response delete(String url, String token) {
        return readResponse(sendRequest(url, token, null, "DELETE"));
    }

    static Response get(String url, String token) {
        return readResponse(sendRequest(url, token, null, "GET"));
    }

    private static HttpURLConnection sendRequest(String url, String token, JSONObject data, String requestType) {
        HttpURLConnection connection = null;
        try {
            connection = openConnection(requestType, url);
            setAuthorizationHeader(connection, token);
            writeRequestBody(connection, data);

            return connection;
        } catch (IOException e) {
            closeConnection(connection);
            throw new RuntimeException(e);
        }
    }

    private static Response readResponse(HttpURLConnection connection) {
        try {
            BufferedReader br;
            if (connection.getResponseCode() == 404) {
                throw new FileNotFoundException(String.format("Die Datei %s konnte nicht gefunden werden.", connection.getURL().toString()));
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
    }

    private static void closeConnection(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
    }

    private static void setAuthorizationHeader(HttpURLConnection connection, String token) {
        if (token != null) {
            connection.setRequestProperty("Authorization", token);
        }
    }

    private static void writeRequestBody(HttpURLConnection connection, JSONObject data) throws IOException {

        if (data != null) {
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(data.toString().getBytes());
            }
        }
    }

    private static HttpURLConnection openConnection(String httpMethod, String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) formatURL(url).openConnection();
        connection.setRequestMethod(httpMethod);
        return connection;
    }

    private static URL formatURL(String stringURL) throws MalformedURLException {
        if (!DOMAIN.endsWith("/") && !stringURL.startsWith("/")) {
            stringURL = "/" + stringURL;
        }
        return new URL(DOMAIN + stringURL.replaceAll(" ", "+"));
    }
}
