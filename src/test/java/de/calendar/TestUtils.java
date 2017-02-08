package de.calendar;

import org.json.JSONObject;

import java.io.IOException;

/**
 * calendar:
 * * de.calendar.cucumber:
 * * * Created by KAABERT on 26.01.2017.
 */
class TestUtils {

    static Response post(String url, String token, JSONObject data) {
        try (Connection connection = Connection.open(url, Connection.Method.POST)) {
            return connection
                    .setAuthorizationHeader(token)
                    .writeRequestBody(data)
                    .readResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Response put(String url, String token, JSONObject data) {
        try (Connection connection = Connection.open(url, Connection.Method.PUT)) {
            return connection
                    .setAuthorizationHeader(token)
                    .writeRequestBody(data)
                    .readResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Response delete(String url, String token) {
        try (Connection connection = Connection.open(url, Connection.Method.DELETE)) {
            return connection
                    .setAuthorizationHeader(token)
                    .readResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Response get(String url, String token) {
        try (Connection connection = Connection.open(url, Connection.Method.GET)) {
            return connection
                    .setAuthorizationHeader(token)
                    .readResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
