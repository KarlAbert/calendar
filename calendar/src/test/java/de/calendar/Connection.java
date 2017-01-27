package de.calendar;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created Connection in de.calendar
 * by ARSTULKE on 26.01.2017.
 */
public class Connection implements AutoCloseable {
    private static final String DOMAIN = "http://localhost:8080";

    private boolean readable = false;
    private HttpURLConnection connection;

    private Connection(URL url, Method method) throws IOException {
        this.connection = (HttpURLConnection) url.openConnection();
        this.connection.setRequestMethod(method.name());
        readable = true;
    }

    static Connection open(String url, Method method) throws IOException {
        return new Connection(formatURL(url), method);
    }


    Connection setAuthorizationHeader(String token) {
        if (token != null) {
            connection.setRequestProperty("Authorization", token);
        }
        return this;
    }

    Connection writeRequestBody(JSONObject data) throws IOException {
        if (data != null) {
            String dataString = data.toString();

            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(dataString.getBytes());
            }
        }
        return this;
    }

    Response readResponse() throws IOException {
        if (readable) {
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
        } else {
            throw new IllegalStateException("You have to open the connection before you read its content.");
        }
    }

    private static URL formatURL(String stringURL) throws MalformedURLException {
        if (stringURL.contains("null")) {
            throw new IllegalArgumentException("'null' is not supported");
        }
        if (!DOMAIN.endsWith("/") && !stringURL.startsWith("/")) {
            stringURL = "/" + stringURL;
        }
        return new URL(DOMAIN + stringURL.replaceAll(" ", "+"));
    }

    @Override
    public void close() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    public enum Method {
        POST, GET, PUT, DELETE
    }
}
