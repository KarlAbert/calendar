package de.calendar.controller;

import de.calendar.model.Event;
import de.calendar.model.User;
import de.calendar.repositories.UserRepository;
import de.calendar.utils.CalendarUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created EventController in de.calendar.controller
 * by ARSTULKE on 19.01.2017.
 */
@RestController
public class EventController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/event/create")
    public ResponseEntity<String> createEvent(@RequestParam(name = "token") String token, @RequestHeader(name = "Data") String dataString) {
        return authorize(token, dataString, (user, data) -> {
            if (valid(data)) {
                LocalDateTime startTMP = CalendarUtils.parse(data.has("start") ? data.getString("start") : null);
                LocalDateTime endTMP = CalendarUtils.parse(data.has("end") ? data.getString("end") : null);
                LocalDateTime start, end;
                if(startTMP == null && endTMP != null) {
                    start = endTMP.minusHours(1L);
                    end = endTMP;
                } else if(startTMP != null && endTMP == null) {
                    start = startTMP;
                    end = startTMP.plusHours(1L);
                } else {
                    start = startTMP;
                    end = endTMP;
                }

                Event event = new Event(data.getString("title"), start, end);
                user.getEvents().add(event);
                userRepository.save(user);

                return new ResponseEntity<>("", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You have to set the http header \"Data\" as json.\n" +
                        "The json object should contain the following keys:\n" +
                        " - title\n" +
                        " - from (dd.mm.yyyy HH:MM)\n" +
                        " - until (dd.mm.yyyy HH:MM)", HttpStatus.BAD_REQUEST);
            }
        });
    }

    @RequestMapping("/event")
    public ResponseEntity<String> getEvents(@RequestParam(name = "token") String token, @RequestParam(name = "from", required = false) String fromString, @RequestParam(name = "until", required = false) String untilString) {
        return authorize(token, null, (user, jsonObject) -> {
            LocalDateTime fromTMP = CalendarUtils.parse(fromString);
            LocalDateTime untilTMP = CalendarUtils.parse(untilString);
            if (fromTMP != null || untilTMP != null) {
                LocalDateTime from;
                LocalDateTime until;
                if (fromTMP == null) {
                    from = untilTMP.minusHours(1L);
                    until = untilTMP;
                } else if (untilTMP == null) {
                    from = fromTMP;
                    until = fromTMP.plusHours(1L);
                } else {
                    from = fromTMP;
                    until = untilTMP;
                }

                List<JSONObject> events = user.getEvents()
                        .stream()
                        .filter(event -> CalendarUtils.isEventBetween(event, from, until))
                        .map(event -> new JSONObject()
                                .put("title", event.getTitle())
                                .put("start", event.getStartString())
                                .put("end", event.getEndString())
                                .put("id", event.getID()))
                        .collect(Collectors.toList());
                return new ResponseEntity<>(new JSONArray(events).toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You have to set the \"from\" or the \"until\" query param.\nIf you only set one the other would be 1 hour before/after the given param.", HttpStatus.BAD_REQUEST);
            }
        });
    }

    @RequestMapping("/event/{id}/edit")
    public ResponseEntity<String> editEvent(@RequestParam(name = "token") String token, @PathVariable("id") String id, @RequestHeader(name = "Data") String dataString) {
        return authorize(token, dataString, (user, data) -> {
            if (valid(data)) {
                List<Event> events = user.getEvents().stream().filter(event -> event.getID().equals(Long.valueOf(id))).collect(Collectors.toList());
                if (events.size() > 1) {
                    throw new AssertionError("Datenbankfehler mehrere Termine mit der ID " + id);
                } else if (events.size() == 0) {
                    return new ResponseEntity<>("Das gesuchte Element konnte nicht gefunen werden.", HttpStatus.NOT_FOUND);
                } else {
                    Event event = events.get(0);

                    LocalDateTime startTMP = CalendarUtils.parse(data.has("start") ? data.getString("start") : null);
                    LocalDateTime endTMP = CalendarUtils.parse(data.has("end") ? data.getString("end") : null);
                    LocalDateTime start, end;
                    if(startTMP == null && endTMP != null) {
                        start = endTMP.minusHours(1L);
                        end = endTMP;
                    } else if(startTMP != null && endTMP == null) {
                        start = startTMP;
                        end = startTMP.plusHours(1L);
                    } else {
                        start = startTMP;
                        end = endTMP;
                    }

                    event.setTitle(data.getString("title"));
                    event.setStart(start);
                    event.setEnd(end);

                    userRepository.save(user);

                    return new ResponseEntity<>("", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("You have to set the http header \"Data\" as json.\n" +
                        "The json object should contain the following keys:\n" +
                        " - title\n" +
                        " - start\n" +
                        " - end", HttpStatus.BAD_REQUEST);
            }
        });
    }

    @RequestMapping("event/{id}/delete")
    public ResponseEntity<String> deleteEvent(@RequestParam(name = "token") String token, @PathVariable("id") String id) {
        return authorize(token, null, (user, data) -> {
            List<Event> events = user.getEvents().stream().filter(event -> event.getID().equals(Long.valueOf(id))).collect(Collectors.toList());
            if (events.size() > 1) {
                throw new AssertionError("Datenbankfehler mehrere Termine mit der ID " + id);
            } else if (events.size() == 0) {
                return new ResponseEntity<>("Das gesuchte Element konnte nicht gefunen werden.", HttpStatus.NOT_FOUND);
            } else {
                Event event = events.get(0);
                user.getEvents().remove(event);

                userRepository.save(user);

                return new ResponseEntity<>("", HttpStatus.OK);
            }
        });
    }

    public static boolean valid(JSONObject data) {
        if (!data.has("title")) return false;
        if (!(data.has("start") || data.has("end"))) return false;

        if (CalendarUtils.parse(data.has("start") ? data.getString("start"): null) == null && CalendarUtils.parse(data.has("end") ? data.getString("end"): null) == null) return false;

        return true;
    }

    private ResponseEntity<String> authorize(String token, String dataString, Function function) {
        User user = userRepository.findOneByTokenValue(token);
        if (user == null) {
            return new ResponseEntity<>("Invalid or expired token.", HttpStatus.UNAUTHORIZED);
        } else {
            return function.doSomething(user, dataString == null ? null : new JSONObject(dataString));
        }
    }

    private interface Function {
        ResponseEntity<String> doSomething(User user, JSONObject data);
    }
}
