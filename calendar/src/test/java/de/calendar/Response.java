package de.calendar;

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

    public Response(int status, String data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getRawData() {
        return data;
    }

    public JSONObject getJSONData() {
        try {
            return new JSONObject(data);
        } catch (JSONException e) {
            return new JSONObject();
        }
    }
}
