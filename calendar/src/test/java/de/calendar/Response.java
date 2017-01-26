package de.calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * calendar:
 * * de.calendar:
 * * * Created by KAABERT on 18.01.2017.
 */
public class Response {
    private final int status;
    private final String data;

    Response(int status, String data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public JSONObject getJSONObject() {
        try {
            return new JSONObject(data);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray getJSONArray() {
        try {
            return new JSONArray(data);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty() {
        return data.length() == 0;
    }
}
