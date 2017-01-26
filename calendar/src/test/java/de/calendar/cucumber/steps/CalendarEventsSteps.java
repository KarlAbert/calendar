package de.calendar.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;
import de.calendar.CalendarTestUtils;
import de.calendar.Response;
import de.calendar.model.Event;
import de.calendar.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * calendar:
 * * PACKAGE_NAME:
 * * * Created by KAABERT on 18.01.2017.
 */
public class CalendarEventsSteps {
    private String token;
    private Response saveResponse;
    private List<Event> events;
    private int eventCount;
    private Long lastID;

    //region ganztägig
    //region gegeben sei
    @Gegebensei("^das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" in dem Kalendar von TestUser$")
    public void dasEreignisAmInDemKalendarVonTestUser(String title, String dateString) throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");
        Event event = CalendarTestUtils.findEvent(title, dateString + " 00:00", dateString + " 23:59", token);
        if (event == null) {
            Response response = CalendarTestUtils.createEvent(new Event(title, dateString + " 00:00", dateString + " 23:59"), token);
            lastID = response.getJSONObject().getLong("id");
        } else {
            lastID = event.getId();
        }
    }
    //endregion

    //region wenn
    @Wenn("^TestUser das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" löscht$")
    public void testuserDasEreignisAmLöscht(String title, String dateString) throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");
        Event event = CalendarTestUtils.findEvent(title, dateString + " 00:00", dateString + " 23:59", token);

        this.eventCount = CalendarTestUtils.findEvent(dateString + " 00:00", dateString + " 23:59", token).size();
        this.saveResponse = CalendarTestUtils.deleteEvent(event.getId(), token);
    }

    @Wenn("^TestUser das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" zu \"([^\"]*)\" am \"([^\"]*)\" ändert$")
    public void testuserDasEreignisAmZuAmÄndert(String title1, String dateString1, String title2, String dateString2) throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");

        Event event = CalendarTestUtils.findEvent(title1, dateString1 + " 00:00", dateString1 + " 23:59", token);
        if (event == null) {
            fail("Kalendarereigniss konnte nicht gefunden werden.");
        }
        event.setTitle(title2);
        event.setStart(dateString2 + " 00:00");
        event.setEnd(dateString2 + " 23:59");

        this.saveResponse = CalendarTestUtils.editEvent(event.getId(), event, token);
    }

    @Wenn("^TestUser die Ereignisse zwischen dem \"([^\"]*)\" und dem \"([^\"]*)\" anzeigen lässt$")
    public void testuserDieEreignisseZwischenDemUndDemAnzeigenLässt(String from, String until) throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");
        events = CalendarTestUtils.findEvent(from + " 00:00", until + " 23:59", token);
    }

    @Wenn("^TestUser ein ganztägiges Ereigniss am \"([^\"]*)\" mit dem Titel \"([^\"]*)\" erstellt$")
    public void testuserEinEreignissAmMitDemTitelErstellt(String dateString, String title) throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");
        this.saveResponse = CalendarTestUtils.createEvent(new Event(title, dateString + " 00:00", dateString + " 23:59"), token);
    }
    //endregion

    //region dann
    @Dann("^existiert kein Ereignis am \"([^\"]*)\" im Kalendar von TestUser$")
    public void existiertKeinEreignisAmImKalendarVonTestUser(String dateString) throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");

        assertThat(this.saveResponse.getStatus(), is(204));

        List<Event> eventList = CalendarTestUtils.findEvent(dateString + " 00:00", dateString + " 23:59", token);

        assertThat(eventList.size(), is(eventCount - 1));
    }

    @Dann("^werden TestUser folgende Ergebnisse zurückgegeben:$")
    public void werdenTestUserFolgendeErgebnisseZurückgegeben(Map<String, String> data) throws Throwable {
        data.entrySet().forEach(eventEntry -> {
            Event event = new Event(eventEntry.getKey(), eventEntry.getValue() + " 00:00", eventEntry.getValue() + " 23:59");
            if (events.contains(event)) {
                assertTrue(true);
            } else {
                fail(String.format("Keine Übereinstimmung gefunden!\n%s\n%s", events, eventEntry));
            }
        });
    }

    @Dann("^steht das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" in dem Kalender von TestUser$")
    public void stehtDasEreignisAmInDemKalenderVonTestUser(String title, String dateString) throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");
        assertThat(saveResponse.getStatus(), anyOf(is(201), is(200)));

        List<Event> events = CalendarTestUtils.findEvent(dateString + " 00:00", dateString + " 23:59", token);
        Event event = new Event(title, dateString + " 00:00", dateString + " 23:59");
        if (events.contains(event)) {
            assertTrue(true);
        } else {
            fail(String.format("Kalenderereignis:{%s, %s} konnte nicht gefnunden werden.", title, dateString));
        }
    }

    //endregion
    //endregion

    //region zeitgebunden
    //region gegenben sei
    @Gegebensei("^das Ereignis \"([^\"]*)\"  von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr in dem Kalendar von TestUser$")
    public void dasEreignisVonUhrBisUhrInDemKalendarVonTestUser(String title, String startString, String endString) throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");
        Event event = CalendarTestUtils.findEvent(title, startString, endString, token);
        if (event == null) {
            CalendarTestUtils.createEvent(new Event(title, startString, endString), token);
        }
    }

    //endregion
    //region wenn
    @Wenn("^TestUser ein Ereigniss mit dem Titel \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr erstellt$")
    public void testuserEinEreignissMitDemTitelVonUhrBisUhrErstellt(String title, String startString, String endString) throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");
        this.saveResponse = CalendarTestUtils.createEvent(new Event(title, startString, endString), token);
    }

    @Wenn("^TestUser das Ereignis \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr zu \"([^\"]*)\" zwischen \"([^\"]*)\"Uhr und \"([^\"]*)\"Uhr ändert$")
    public void testuserDasEreignisVonUhrBisUhrZuAmZwischenUhrUndUhrÄndert(String title1, String startString1, String endString1, String title2, String startString2, String endString2) throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");
        Event event = CalendarTestUtils.findEvent(title1, startString1, endString1, token);
        if (event == null) {
            fail("Kalendarereigniss konnte nicht gefunden werden.");
        } else {
            event.setTitle(title2);
            event.setStart(startString2);
            event.setEnd(endString2);
            this.saveResponse = CalendarTestUtils.editEvent(event.getId(), event, token);
        }
    }

    //endregion
    //region dann
    @Dann("^steht das Ereignis \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr in dem Kalender von TestUser$")
    public void stehtDasEreignisVonUhrBisUhrInDemKalenderVonTestUser(String title, String startString, String endString) throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");

        assertThat(this.saveResponse.getStatus(), anyOf(is(201), is(200)));

        Event event = CalendarTestUtils.getEvent(saveResponse.getJSONObject().getLong("id"), token);
        if (event == null) {
            fail("Kalendarereignis konnte nicht gefunen werden.");
        } else {
            assertThat(event.getTitle(), is(title));
            assertThat(event.getStart(), is(CalendarUtils.parse(startString)));
            assertThat(event.getEnd(), is(CalendarUtils.parse(endString)));
        }
    }

    @Wenn("^TestUser das ganztägige Ereignis anhand der ID von abruft$")
    public void testuserDasGanztägigeEreignisAnhandDerIDVonAbruft() throws Throwable {
        token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");
        events = new ArrayList<>();
        events.add(CalendarTestUtils.getEvent(lastID, token));
    }
    //endregion
    //endregion
}
