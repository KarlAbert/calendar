package de.calendar;

import de.calendar.model.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created CalendarTestUtils in de.calendar.cucumber
 * by ARSTULKE on 19.01.2017.
 */
public class CalendarTestUtils {
    public static Response createDaylongEvent(String title, String dateString, String token) {
        return createTimespanEvent(title, dateString + " 00:00", dateString + " 23:59", token);
    }

    public static Response createTimespanEvent(String title, String start, String end, String token) {
        JSONObject eventJSON = new JSONObject()
                .put("title", title)
                .put("start", start)
                .put("end", end);
        return TestUtils.post("/event/create", token, eventJSON);
    }

    public static JSONArray findAllEventsByDate(String from, String until, String token) {
        token = TestUtils.tryLogin(token);
        String url = String.format("/event?from=%s&until=%s", from, until);
        url = url.replace(" ", "%20");
        Response response = TestUtils.get(url, token);
        return response.getJSONArray();
    }

    public static Event findOneEventByTitleAndDate(String title, String from, String until, String token) {
        List<Event> eventList = new ArrayList<>();
        JSONArray events = CalendarTestUtils.findAllEventsByDate(from, until, token);
        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.getJSONObject(i);
            Event eventObject = new Event(event.getString("title"), event.getString("start"), event.getString("end"));
            eventObject.setID(event.getLong("id"));
            eventList.add(eventObject);
        }

        eventList = eventList.stream().filter(event -> event.getTitle().equals(title) &&
                (event.getStartString().equals(from) || from == null) &&
                (event.getEndString().equals(until) || until == null)).collect(Collectors.toList());
        if (eventList.size() > 1) {
            throw new RuntimeException("Multiple Elements");
        } else if (eventList.size() == 0) {
            return null;
        } else {
            return eventList.get(0);
        }
    }

    public static Response saveEvent(Event event, String token) {
        JSONObject eventJSON = new JSONObject()
                .put("title", event.getTitle())
                .put("start", event.getStartString())
                .put("end", event.getEndString());
        return TestUtils.post("/event/" + event.getID() + "/edit", token, eventJSON);
    }

    public static Response deleteEvent(Event event, String token) {
        return TestUtils.get("/event/" + event.getID() + "/delete", token);
    }

}
