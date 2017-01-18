package de.calendar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * calendar:
 * * de.calendar:
 * * * Created by KAABERT on 18.01.2017.
 */
public class TestUtils {

    public static Response post(String domain, String stringUrl, String token, JSONObject data) {
        if(!stringUrl.contains("?")) {
            stringUrl += "?";
        }
        return post(domain, stringUrl + "token=" + token, data);
    }

    public static Response post(String domain, String stringURL, JSONObject data) {
        try {
            URL url = new URL(domain + stringURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Data", data.toString());

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder responseBody = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                responseBody.append(line);
                responseBody.append('\r');
            }
            rd.close();

            return new Response(connection.getResponseCode(), responseBody.toString());
        } catch (IOException e) {
            //e.printStackTrace();
            /*if(e.getCause().getClass().equals(ConnectException.class)) {
                throw new RuntimeException(e);
            }*/
            throw new RuntimeException(e);
        }
    }
}
