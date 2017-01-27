package de.calendar;

import de.calendar.model.Event;
import de.calendar.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * calendar:
 * * de.calendar:
 * * * Created by KAABERT on 26.01.2017.
 */
public class CalendarTestUtils {

    public static String testUsername = "TestUser1";
    public static String testUserPassword = "Password1";
    private static Function<? super JSONObject, Event> eventMapping = jsonObject -> {
        Event event = new Event(
                jsonObject.getString("title"),
                jsonObject.getString("start"),
                jsonObject.getString("end"));
                event.setId(jsonObject.getLong("id"));
        return event;
    };

    public static Response register(User user) {
        return TestUtils.post("/user", null, new JSONObject()
                .put("username", user.getUsername())
                .put("firstname", user.getFirstname())
                .put("lastname", user.getLastname())
                .put("email", user.getEmail())
                .put("password", user.getPassword()));
    }

    public static Response login(String username, String password) {
        return TestUtils.post("/user", null, new JSONObject()
                .put("username", username)
                .put("password", password));

    }

    public static Response createEvent(Event event, String token) {
        return TestUtils.post("/event", token, new JSONObject()
                .put("title", event.getTitle())
                .put("start", event.getStart().toString())
                .put("end", event.getEnd().toString()));
    }

    private static String format(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(localDateTime);
    }

    public static LocalDateTime parse(String string) {
        return LocalDateTime.parse(string, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public static Event getEvent(Long id, String token) {
        return eventMapping.apply(TestUtils.get("/event/" + id, token).getJSONObject());
    }

    public static Response deleteEvent(Long id, String token) {
        return TestUtils.delete("/event/" + id, token);
    }

    public static Response editEvent(Long id, Event event, String token) {
        return TestUtils.put("/event/" + id, token, new JSONObject()
                .put("title", event.getTitle())
                .put("start", event.getStart())
                .put("end", event.getEnd()));
    }

    public static Response inviteEvent(Long id, String token) {
        return TestUtils.post("/event/" + id + "/user", token, null);
    }

    public static Response joinEvent(Long id, String invitationToken, String token) {
        return TestUtils.put("/event/" + id + "/user/" + invitationToken, token, null);
    }

    public static Event findEvent(String title, String fromString, String untilString, String token) {
        LocalDateTime from = parse(fromString);
        LocalDateTime until = parse(untilString);
        List<Event> all = findEvent(from, until, token)
                .stream()
                .filter(event -> event.getTitle().equals(title) && event.getStart().equals(from) && event.getEnd().equals(until))
                .collect(Collectors.toList());

        if (all.size() > 1) {
            throw new RuntimeException("Multiple Elements");
        } else if (all.size() == 0) {
            return null;
        } else {
            return all.get(0);
        }
    }

    public static List<Event> findEvent(LocalDateTime from, LocalDateTime until, String token) {
        JSONArray arr = TestUtils.get(String.format("/event?from=%s&until=%s", from, until), token).getJSONArray();
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            list.add(arr.getJSONObject(i));
        }

        return list.stream().map(eventMapping).collect(Collectors.toList());
    }

    public static String registerAndLogin(String username) {
        register(new User("Teeeeest", "Usssser", username, username + "@generated.com", "passwort"));
        return login(username, "passwort").getJSONObject().getString("token");
    }
}
