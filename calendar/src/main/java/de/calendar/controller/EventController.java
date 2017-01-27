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
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.calendar.utils.CalendarUtils.parse;

/**
 * Created EventController in de.calendar.controller
 * by ARSTULKE on 19.01.2017.
 */
@SuppressWarnings("unused")
@RestController
@CrossOrigin
public class EventController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    private java.util.function.Function<Event, JSONObject> mapper = event -> new JSONObject()
            .put("title", event.getTitle())
            .put("start", event.getStart().toString())
            .put("end", event.getEnd().toString())
            .put("id", event.getId());

    @Autowired
    private Authorization authorization;

    @PostMapping("/event")
    public ResponseEntity<String> createEvent(@RequestHeader(name = "Authorization") String token, @RequestBody String dataString) {
        return authorization.authorize(token, dataString, (user, data) -> {
            if (valid(data)) {
                LocalDateTime startTMP = parse(data.has("start") ? data.getString("start") : null);
                LocalDateTime endTMP = parse(data.has("end") ? data.getString("end") : null);
                LocalDateTime start, end;
                if (startTMP == null && endTMP != null) {
                    start = endTMP.minusHours(1L);
                    end = endTMP;
                } else if (startTMP != null && endTMP == null) {
                    start = startTMP;
                    end = startTMP.plusHours(1L);
                } else {
                    //noinspection ConstantConditions
                    if (startTMP.isAfter(endTMP)) {
                        start = endTMP;
                        end = startTMP;
                    } else {
                        start = startTMP;
                        end = endTMP;
                    }
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

    @GetMapping("/event/{id}")
    public ResponseEntity<String> getEvent(@RequestHeader(name = "Authorization") String token, @PathVariable(name = "id") String id) {
        return authorization.authorize(token, null, (user, jsonObject) -> {
            if (id != null) {
                Event event = eventRepository.findOne(Long.valueOf(id));
                if (!(user.getOwningevents().contains(event) || user.getSubscriptions().contains(event))) {
                    return new ResponseEntity<>("You aren't the owner of this event.", HttpStatus.FORBIDDEN);
                }
                return new ResponseEntity<>(mapper.apply(event).toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You have to set the 'id' like /event/{id}.", HttpStatus.BAD_REQUEST);
            }
        });
    }

    @GetMapping("/event")
    public ResponseEntity<String> getEvents(@RequestHeader(name = "Authorization") String token, @RequestParam(name = "from", required = false) String fromString, @RequestParam(name = "until", required = false) String untilString) {
        return authorization.authorize(token, null, (user, jsonObject) -> {
            LocalDateTime fromTMP = parse(fromString);
            LocalDateTime untilTMP = parse(untilString);


            LocalDateTime from;
            LocalDateTime until;
            if (fromTMP == null && untilTMP == null) {
                from = LocalDateTime.MIN;
                until = LocalDateTime.MAX;
            } else if (fromTMP == null) {
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

        });
    }

    @PutMapping("/event/{id}")
    public ResponseEntity<String> editEvent(@RequestHeader(name = "Authorization") String token, @PathVariable("id") String id, @RequestBody String dataString) {
        return authorization.authorize(token, dataString, (user, data) -> {
            if (valid(data)) {
                Event event = eventRepository.findOne(Long.valueOf(id));
                if (!user.getOwningevents().contains(event)) {
                    return new ResponseEntity<>("You aren't the owner of this event.", HttpStatus.FORBIDDEN);
                } else {
                    LocalDateTime startTMP = parse(data.has("start") ? data.getString("start") : null);
                    LocalDateTime endTMP = parse(data.has("end") ? data.getString("end") : null);
                    LocalDateTime start, end;
                    if (startTMP == null && endTMP != null) {
                        start = endTMP.minusHours(1L);
                        end = endTMP;
                    } else if (startTMP != null && endTMP == null) {
                        start = startTMP;
                        end = startTMP.plusHours(1L);
                    } else {
                        //noinspection ConstantConditions
                        if (startTMP.isAfter(endTMP)) {
                            start = endTMP;
                            end = startTMP;
                        } else {
                            start = startTMP;
                            end = endTMP;
                        }
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

    @DeleteMapping("/event/{id}")
    public ResponseEntity<String> deleteEvent(@RequestHeader(name = "Authorization") String token, @PathVariable("id") String id) {
        return authorization.authorize(token, null, (user, data) -> {
            Event event = eventRepository.findOne(Long.valueOf(id));
            if (!user.getOwningevents().contains(event)) {
                return new ResponseEntity<>("You aren't the owner of this event.", HttpStatus.FORBIDDEN);
            }
            new HashSet<>(event.getOwner()).forEach(owner -> {
                owner.getOwningevents().remove(event);
                userRepository.save(owner);
            });
            new HashSet<>(event.getSubscribers()).forEach(subscriber -> {
                subscriber.getSubscriptions().remove(event);
                userRepository.save(subscriber);
            });
            eventRepository.delete(event);

            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        });
    }

    @PostMapping("event/{id}/user")
    public ResponseEntity<String> inviteEventUser(@RequestHeader(name = "Authorization") String token, @PathVariable("id") String id) {
        return authorization.authorize(token, null, (user, data) -> {
            if (id == null || !StringUtils.isNumeric(id)) {
                return new ResponseEntity<>("\"" + id + "\" is not a event ID.", HttpStatus.BAD_REQUEST);
            }

            Event event = eventRepository.findOne(Long.valueOf(id));
            if (user.getOwningevents().contains(event)) {
                String invitationToken = TokenGenerator.generateRandom(64);
                while (invitationRepository.findOne(invitationToken) != null) {
                    invitationToken = TokenGenerator.generateRandom(64);
                }

                event.getInvitations().add(new Invitation(invitationToken));
                eventRepository.save(event);

                return new ResponseEntity<>(new JSONObject().put("url", String.format("/event/%s/invitation/%s", id, invitationToken)).toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You aren't the owner of this event.", HttpStatus.FORBIDDEN);
            }
        });
    }

    @PutMapping("event/{id}/user/{invitationToken}")
    public ResponseEntity<String> createEventUser(@RequestHeader(name = "Authorization") String token, @PathVariable("id") String id, @PathVariable(name = "invitationToken") String invitationToken) {
        return authorization.authorize(token, null, (user, data) -> {
            if (id == null || id.equals("null")) {
                return new ResponseEntity<>("You have to set the 'id' like 'event/{id}/...'.", HttpStatus.BAD_REQUEST);
            }
            Event event = eventRepository.findOne(Long.valueOf(id));
            Invitation invitation = new Invitation(invitationToken);
            if (event.getInvitations().contains(invitation)) {
                user.getSubscriptions().add(event);
                event.getSubscribers().add(user);
                event.getInvitations().remove(invitation);
            } else {
                return new ResponseEntity<>("Invalid or expired invitation token.", HttpStatus.FORBIDDEN);
            }

            eventRepository.save(event);
            userRepository.save(user);
            return new ResponseEntity<>("", HttpStatus.OK);
        });
    }

    @GetMapping("event/{id}/user")
    public ResponseEntity<String> getEventUser(@RequestHeader(name = "Authorization") String token, @PathVariable("id") String id) {
        return authorization.authorize(token, null, (user, data) -> {
            if (id == null || !StringUtils.isNumeric(id)) {
                return new ResponseEntity<>("\"" + id + "\" is not a event ID.", HttpStatus.BAD_REQUEST);
            }

            Event event = eventRepository.findOne(Long.valueOf(id));
            if (event.getOwner().contains(user) || event.getSubscribers().contains(user)) {
                JSONArray arr = new JSONArray();

                Consumer<? super User> userMapping = (Consumer<User>) member -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("username", member.getUsername());
                    jsonObject.put("firstname", member.getFirstname());
                    jsonObject.put("lastname", member.getLastname());

                    arr.put(arr.length(), jsonObject);
                };

                event.getSubscribers().forEach(userMapping);
                event.getOwner().forEach(userMapping);

                return new ResponseEntity<>(arr.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You aren't the owner of this event.", HttpStatus.FORBIDDEN);
            }
        });
    }

    @SuppressWarnings("RedundantIfStatement")
    public static boolean valid(JSONObject data) {
        if (!data.has("title")) return false;
        if (!(data.has("start") || data.has("end"))) return false;

        if (parse(data.has("start") ? data.getString("start") : null) == null
                && parse(data.has("end") ? data.getString("end") : null) == null)
            return false;

        return true;
    }
}
