package de.calendar.controller;

import de.calendar.model.Event;
import de.calendar.model.Invitation;
import de.calendar.model.User;
import de.calendar.repositories.EventRepository;
import de.calendar.repositories.InvitationRepository;
import de.calendar.repositories.UserRepository;
import de.calendar.utils.CalendarUtils;
import de.calendar.utils.TokenGenerator;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created EventController in de.calendar.controller
 * by ARSTULKE on 19.01.2017.
 */
@SuppressWarnings("unused")
@RestController
public class EventController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    private java.util.function.Function<Event, JSONObject> mapper = event -> new JSONObject()
            .put("title", event.getTitle())
            .put("start", event.getStartString())
            .put("end", event.getEndString())
            .put("id", event.getId());

    @Autowired
    private Authorization authorization;

    @PostMapping("/event")
    public ResponseEntity<String> createEvent(@RequestParam(name = "token") String token, @RequestBody String dataString) {
        return authorization.authorize(token, dataString, (user, data) -> {
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

                Event event = new Event(data.getString("title"), start, end, user);
                user.getOwningevents().add(event);
                eventRepository.save(event);
                userRepository.save(user);

                return new ResponseEntity<>(mapper.apply(event).toString(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("You have to set the http body as json.\n" +
                        "The json object should contain the following keys:\n" +
                        " - title\n" +
                        " - from (dd.mm.yyyy HH:MM)\n" +
                        " - until (dd.mm.yyyy HH:MM)", HttpStatus.BAD_REQUEST);
            }
        });
    }

    @GetMapping("/event")
    public ResponseEntity<String> getEvents(@RequestParam(name = "token") String token, @RequestParam(name = "id", required = false) String id, @RequestParam(name = "from", required = false) String fromString, @RequestParam(name = "until", required = false) String untilString) {
        return authorization.authorize(token, null, (user, jsonObject) -> {
            LocalDateTime fromTMP = CalendarUtils.parse(fromString);
            LocalDateTime untilTMP = CalendarUtils.parse(untilString);
            if ((fromTMP != null || untilTMP != null) && id == null) {
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

                List<JSONObject> events = user.getSubscriptions()
                        .stream()
                        .filter(event -> CalendarUtils.isEventBetween(event, from, until))
                        .map(mapper)
                        .collect(Collectors.toList());

                events.addAll(user.getOwningevents()
                        .stream()
                        .filter(event -> CalendarUtils.isEventBetween(event, from, until))
                        .map(mapper)
                        .collect(Collectors.toList()));
                return new ResponseEntity<>(new JSONArray(events).toString(), HttpStatus.OK);
            } else if (id != null) {
                Event event = eventRepository.findOne(Long.valueOf(id));
                if (!(user.getOwningevents().contains(event) || user.getSubscriptions().contains(event))) {
                    return new ResponseEntity<>("You aren't the owner of this event.", HttpStatus.FORBIDDEN);
                }
                return new ResponseEntity<>(mapper.apply(event).toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You have to set the \"from\" or the \"until\" query param.\nIf you only set one the other would be 1 hour before/after the given param.", HttpStatus.BAD_REQUEST);
            }
        });
    }

    @PutMapping("/event")
    public ResponseEntity<String> editEvent(@RequestParam(name = "token") String token, @RequestParam("id") String id, @RequestBody String dataString) {
        return authorization.authorize(token, dataString, (user, data) -> {
            if (valid(data)) {
                Event event = eventRepository.findOne(Long.valueOf(id));
                if (!user.getOwningevents().contains(event)) {
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

                    return new ResponseEntity<>(mapper.apply(event).toString(), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("You have to set the http body as json.\n" +
                        "The json object should contain the following keys:\n" +
                        " - title\n" +
                        " - start\n" +
                        " - end", HttpStatus.BAD_REQUEST);
            }
        });
    }

    @DeleteMapping("/event")
    public ResponseEntity<String> deleteEvent(@RequestParam(name = "token") String token, @RequestParam("id") String id) {
        return authorization.authorize(token, null, (user, data) -> {
            Event event = eventRepository.findOne(Long.valueOf(id));
            if (!user.getOwningevents().contains(event)) {
                return new ResponseEntity<>("You aren't the owner of this event.", HttpStatus.FORBIDDEN);
            }
            for (User benutzer : userRepository.findAll()) {
                new HashSet<>(benutzer.getSubscriptions()).stream().filter(ownerEvent -> ownerEvent.getId().equals(event.getId())).forEach(ownerEvent -> {
                    benutzer.getSubscriptions().remove(ownerEvent);
                });
                new HashSet<>(benutzer.getOwningevents()).stream().filter(ownerEvent -> ownerEvent.getId().equals(event.getId())).forEach(ownerEvent -> {
                    benutzer.getOwningevents().remove(ownerEvent);
                });
                userRepository.save(user);
            }
            eventRepository.delete(event);

            return new ResponseEntity<>("", HttpStatus.OK);
        });
    }

    @PostMapping("event/invitation")
    public ResponseEntity<String> inviteEvent(@RequestParam(name = "token") String token, @RequestParam("id") String id) {
        return authorization.authorize(token, null, (user, data) -> {
            if (id == null || !StringUtils.isNumeric(id)) {
                return new ResponseEntity<>("\"" + id + "\" is not a register ID.", HttpStatus.BAD_REQUEST);
            }

            Event event = eventRepository.findOne(Long.valueOf(id));
            if (user.getOwningevents().contains(event)) {
                String invitationToken = TokenGenerator.generateRandom(64);
                while (invitationRepository.findOne(invitationToken) != null) {
                    invitationToken = TokenGenerator.generateRandom(64);
                }

                event.getInvitations().add(new Invitation(invitationToken));
                eventRepository.save(event);

                return new ResponseEntity<>(new JSONObject().put("url", String.format("/event/invitation?invitationToken=%s&id=%s", invitationToken, id)).toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You aren't the owner of this event.", HttpStatus.FORBIDDEN);
            }
        });
    }

    @PutMapping("event/invitation")
    public ResponseEntity<String> joinEvent(@RequestParam(name = "token") String token, @RequestParam(name = "invitationToken") String invitationToken, @RequestParam("id") String id) {
        return authorization.authorize(token, null, (user, data) -> {
            Event event = eventRepository.findOne(Long.valueOf(id));
            Invitation invitation = new Invitation(invitationToken);
            if (event.getInvitations().contains(invitation)) {
                user.getSubscriptions().add(event);
                event.getInvitations().remove(invitation);
            } else {
                return new ResponseEntity<>("Invalid or expired invitation token.", HttpStatus.FORBIDDEN);
            }

            eventRepository.save(event);
            userRepository.save(user);
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
}
