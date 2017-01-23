package de.calendar.controller;

import de.calendar.model.Event;
import de.calendar.model.InvitationToken;
import de.calendar.model.User;
import de.calendar.repositories.EventRepository;
import de.calendar.repositories.UserRepository;
import de.calendar.utils.CalendarUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private EventRepository eventRepository;
    private java.util.function.Function<Event, JSONObject> mapper = event -> new JSONObject()
            .put("title", event.getTitle())
            .put("start", event.getStartString())
            .put("end", event.getEndString())
            .put("id", event.getID());

    @RequestMapping("/event/create")
    public ResponseEntity<String> createEvent(@RequestParam(name = "token") String token, @RequestBody String dataString) {
        return authorize(token, dataString, (user, data) -> {
            if (valid(data)) {
                LocalDateTime startTMP = CalendarUtils.parse(data.has("start") ? data.getString("start") : null);
                LocalDateTime endTMP = CalendarUtils.parse(data.has("end") ? data.getString("end") : null);
                LocalDateTime start, end;
                if (startTMP == null && endTMP != null) {
                    start = endTMP.minusHours(1L);
                    end = endTMP;
                } else if (startTMP != null && endTMP == null) {
                    start = startTMP;
                    end = startTMP.plusHours(1L);
                } else {
                    start = startTMP;
                    end = endTMP;
                }

                Event event = new Event(data.getString("title"), start, end);
                event.setOwner(user);

                user.getEvents().add(event);
                userRepository.save(user);
                eventRepository.save(event);

                return new ResponseEntity<>(mapper.apply(event).toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You have to set the http header \"data\" as json.\n" +
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
                        .map(mapper)
                        .collect(Collectors.toList());
                return new ResponseEntity<>(new JSONArray(events).toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You have to set the \"from\" or the \"until\" query param.\nIf you only set one the other would be 1 hour before/after the given param.", HttpStatus.BAD_REQUEST);
            }
        });
    }

    @RequestMapping("/event/{id}/edit")
    public ResponseEntity<String> editEvent(@RequestParam(name = "token") String token, @PathVariable("id") String id, @RequestBody String dataString) {
        return authorize(token, dataString, (user, data) -> {
            if (valid(data)) {
                Event event = eventRepository.findOne(Long.valueOf(id));
                if (!event.getOwner().equals(user)) {
                    return new ResponseEntity<>("You aren't the owner of this event.", HttpStatus.FORBIDDEN);
                } else {
                    LocalDateTime startTMP = CalendarUtils.parse(data.has("start") ? data.getString("start") : null);
                    LocalDateTime endTMP = CalendarUtils.parse(data.has("end") ? data.getString("end") : null);
                    LocalDateTime start, end;
                    if (startTMP == null && endTMP != null) {
                        start = endTMP.minusHours(1L);
                        end = endTMP;
                    } else if (startTMP != null && endTMP == null) {
                        start = startTMP;
                        end = startTMP.plusHours(1L);
                    } else {
                        start = startTMP;
                        end = endTMP;
                    }

                    event.setTitle(data.getString("title"));
                    event.setStart(start);
                    event.setEnd(end);

                    eventRepository.save(event);

                    return new ResponseEntity<>("", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("You have to set the http header \"data\" as json.\n" +
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
            Event event = eventRepository.findOne(Long.valueOf(id));
            if(!event.getOwner().equals(user)) {
                return new ResponseEntity<>("You aren't the owner of this event.", HttpStatus.FORBIDDEN);
            }
            user.getEvents().remove(event);

            userRepository.save(user);

            return new ResponseEntity<>("", HttpStatus.OK);
        });
    }

    @RequestMapping("event/{id}/invite")
    public ResponseEntity<String> inviteEvent(@RequestParam(name = "token") String token, @PathVariable("id") String id) {
        return authorize(token, null, (user, data) -> {
            if (id == null || !StringUtils.isNumeric(id)) {
                return new ResponseEntity<>("\"" + id + "\" is not a valid ID.", HttpStatus.BAD_REQUEST);
            }

            Event event = eventRepository.findOne(Long.valueOf(id));
            if (event.getOwner().equals(user)) {
                String link = event.createInvitationLink();
                eventRepository.save(event);

                return new ResponseEntity<>(new JSONObject().put("url", link).toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You aren't the owner of this event.", HttpStatus.FORBIDDEN);
            }
        });
    }

    @RequestMapping("event/{id}/join")
    public ResponseEntity<String> joinEvent(@RequestParam(name = "token") String token, @RequestParam(name = "invitationToken") String invitationToken, @PathVariable("id") String id) {
        return authorize(token, null, (user, data) -> {
            Event event = eventRepository.findOne(Long.valueOf(id));
            try {
                event.join(user, new InvitationToken(invitationToken));
                eventRepository.save(event);
                userRepository.save(user);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>("", HttpStatus.OK);
        });
    }

    public static boolean valid(JSONObject data) {
        if (!data.has("title")) return false;
        if (!(data.has("start") || data.has("end"))) return false;

        if (CalendarUtils.parse(data.has("start") ? data.getString("start") : null) == null && CalendarUtils.parse(data.has("end") ? data.getString("end") : null) == null)
            return false;

        return true;
    }

    private ResponseEntity<String> authorize(String token, String dataString, Function function) {
        User user = userRepository.findOneByTokenValue(token);
        if (user == null || user.getToken().isExpired()) {
            return new ResponseEntity<>("Invalid or expired token.", HttpStatus.UNAUTHORIZED);
        } else {
            return function.doSomething(user, dataString == null ? null : new JSONObject(dataString));
        }
    }

    private interface Function {
        ResponseEntity<String> doSomething(User user, JSONObject data);
    }
}
