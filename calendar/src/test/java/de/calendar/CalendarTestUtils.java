package de.calendar;

import de.calendar.model.Event;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created CalendarTestUtils in de.calendar.cucumber
 * by ARSTULKE on 19.01.2017.
 */
public class CalendarTestUtils {
    public static Response createDaylongEvent(String title, String dateString, String token) {
        return createTimespanEvent(title, dateString + " 00:00", dateString + " 23:59", token);
    }

    public static Response createTimespanEvent(String title, String fromString, String untilString, String token) {
        Event event = new Event(title, fromString, untilString);
        return saveEvent(event, token);
    }

    public static JSONArray findAllEventsByDate(String fromString, String untilString, String token) {
        token = TestUtils.tryLogin(token);
        String url = String.format("/event?from=%s&until%s", fromString, untilString);
        return TestUtils.get(url, token).getJSONArray();
    }

    public static Event findOneEventByTitleAndDate(String title, String from, String until, String token) {
        JSONArray events = CalendarTestUtils.findAllEventsByDate(from, until, token);
        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.getJSONObject(i);
            if (event.getString("title").equals(title)) {
                return new Event(event.getString("title"), event.getString("from"), event.getString("until"));
            }
        }
        return null;
    }

    public static Response saveEvent(Event event, String token) {
        JSONObject eventJSON = new JSONObject()
                .put("title", event.getTitle())
                .put("from", event.getFrom())
                .put("until", event.getUntil());
        return TestUtils.post("/event/" + event.getID() + "/edit", token, eventJSON);
    }

    public static void deleteEvent(Event event, String token) {
        TestUtils.get("/event/" + event.getID() + "/delete", token);
    }
}
