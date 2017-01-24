package de.calendar.cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;
import de.calendar.CalendarTestUtils;
import de.calendar.Response;
import de.calendar.model.Event;
import de.calendar.utils.CalendarUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import static de.calendar.TestUtils.tryLogin;
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
    private JSONArray arr;
    private int eventCount;
    private Long lastID;

    //region ganztägig
    //region gegeben sei
    @Gegebensei("^das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" in dem Kalendar von TestUser$")
    public void dasEreignisAmInDemKalendarVonTestUser(String title, String dateString) throws Throwable {
        token = tryLogin(token);
        Event event = CalendarTestUtils.findOneEventByTitleAndDate(title, dateString + " 00:00", dateString + " 23:59", token);
        if (event == null) {
            Response response = CalendarTestUtils.createDaylongEvent(title, dateString, token);
            lastID = response.getJSONObject().getLong("id");
        } else{
            lastID = event.getID();
        }
    }
    //endregion

    //region wenn
    @Wenn("^TestUser das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" löscht$")
    public void testuserDasEreignisAmLöscht(String title, String dateString) throws Throwable {
        token = tryLogin(token);
        Event event = CalendarTestUtils.findOneEventByTitleAndDate(title, dateString + " 00:00", dateString + " 23:59", token);

        this.eventCount = CalendarTestUtils.findAllEventsByDate(dateString + " 00:00", dateString + " 23:59", token).length();
        this.saveResponse = CalendarTestUtils.deleteEvent(event, token);
    }

    @Wenn("^TestUser das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" zu \"([^\"]*)\" am \"([^\"]*)\" ändert$")
    public void testuserDasEreignisAmZuAmÄndert(String title1, String dateString1, String title2, String dateString2) throws Throwable {
        token = tryLogin(token);

        Event event = CalendarTestUtils.findOneEventByTitleAndDate(title1, dateString1 + " 00:00", dateString1 + " 23:59", token);
        if (event == null) {
            fail("Kalendarereigniss konnte nicht gefunden werden.");
        }
        event.setTitle(title2);
        event.setStartString(dateString2 + " 00:00");
        event.setEndString(dateString2 + " 23:59");

        this.saveResponse = CalendarTestUtils.saveEvent(event, token);
    }

    @Wenn("^TestUser die Ereignisse zwischen dem \"([^\"]*)\" und dem \"([^\"]*)\" anzeigen lässt$")
    public void testuserDieEreignisseZwischenDemUndDemAnzeigenLässt(String from, String until) throws Throwable {
        token = tryLogin(token);
        arr = CalendarTestUtils.findAllEventsByDate(from + " 00:00", until + " 23:59", token);
    }

    @Wenn("^TestUser ein ganztägiges Ereigniss am \"([^\"]*)\" mit dem Titel \"([^\"]*)\" erstellt$")
    public void testuserEinEreignissAmMitDemTitelErstellt(String dateString, String title) throws Throwable {
        token = tryLogin(token);
        this.saveResponse = CalendarTestUtils.createDaylongEvent(title, dateString, token);
    }
    //endregion

    //region dann
    @Dann("^existiert kein Ereignis am \"([^\"]*)\" im Kalendar von TestUser$")
    public void existiertKeinEreignisAmImKalendarVonTestUser(String dateString) throws Throwable {
        token = tryLogin(token);

        assertThat(this.saveResponse.getStatus(), is(200));

        JSONArray arr = CalendarTestUtils.findAllEventsByDate(dateString + " 00:00", dateString + " 23:59", token);

        assertThat(arr.length(), is(eventCount - 1));
    }

    @Dann("^werden TestUser folgende Ergebnisse zurückgegeben:$")
    public void werdenTestUserFolgendeErgebnisseZurückgegeben(Map<String, String> data) throws Throwable {
        for (Map.Entry<String, String> eventEntry : data.entrySet()) {
            boolean found = false;
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonObject = arr.getJSONObject(i);
                if (jsonObject.getString("title").equals(eventEntry.getKey())
                        && jsonObject.getString("start").equals(eventEntry.getValue() + " 00:00")
                        && jsonObject.getString("end").equals(eventEntry.getValue() + " 23:59")) {
                    found = true;
                    break;
                }
            }
            assertTrue(String.format("Keine Übereinstimmung gefunden!\n%s\n%s", arr, eventEntry), found);
        }
    }

    @Dann("^steht das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" in dem Kalender von TestUser$")
    public void stehtDasEreignisAmInDemKalenderVonTestUser(String title, String dateString) throws Throwable {
        token = tryLogin(token);
        assertThat(saveResponse.getStatus(), is(200));

        JSONArray events = CalendarTestUtils.findAllEventsByDate(dateString + " 00:00", dateString + " 23:59", token);
        boolean contains1 = false;
        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.getJSONObject(i);
            if (event.getString("title").equals(title) && event.getString("start").contains(dateString) && event.getString("end").contains(dateString)) {
                contains1 = true;
                break;
            }
        }
        boolean contains = contains1;
        assertTrue(String.format("Kalenderereignis:{%s, %s} konnte nicht gefnunden werden.", title, dateString), contains);
    }

    //endregion
    //endregion

    //region zeitgebunden
    //region gegenben sei
    @Gegebensei("^das Ereignis \"([^\"]*)\"  von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr in dem Kalendar von TestUser$")
    public void dasEreignisVonUhrBisUhrInDemKalendarVonTestUser(String title, String startString, String endString) throws Throwable {
        token = tryLogin(token);
        Event event = CalendarTestUtils.findOneEventByTitleAndDate(title, startString, endString, token);
        if (event == null) {
            CalendarTestUtils.createTimespanEvent(title, startString, endString, token);
        }
    }

    //endregion
    //region wenn
    @Wenn("^TestUser ein Ereigniss mit dem Titel \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr erstellt$")
    public void testuserEinEreignissMitDemTitelVonUhrBisUhrErstellt(String title, String startString, String endString) throws Throwable {
        token = tryLogin(token);
        this.saveResponse = CalendarTestUtils.createTimespanEvent(title, startString, endString, token);
    }

    @Wenn("^TestUser das Ereignis \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr zu \"([^\"]*)\" zwischen \"([^\"]*)\"Uhr und \"([^\"]*)\"Uhr ändert$")
    public void testuserDasEreignisVonUhrBisUhrZuAmZwischenUhrUndUhrÄndert(String title1, String startString1, String endString1, String title2, String startString2, String endString2) throws Throwable {
        token = tryLogin(token);
        Event event = CalendarTestUtils.findOneEventByTitleAndDate(title1, startString1, endString1, token);
        if (event == null) {
            fail("Kalendarereigniss konnte nicht gefunden werden.");
        } else {
            event.setTitle(title2);
            event.setStartString(startString2);
            event.setEndString(endString2);
            this.saveResponse = CalendarTestUtils.saveEvent(event, token);
        }
    }

    //endregion
    //region dann
    @Dann("^steht das Ereignis \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr in dem Kalender von TestUser$")
    public void stehtDasEreignisVonUhrBisUhrInDemKalenderVonTestUser(String title, String startString, String endString) throws Throwable {
        token = tryLogin(token);

        assertThat(this.saveResponse.getStatus(), is(200));

        Event event = CalendarTestUtils.findOneEventByTitleAndDate(title, startString, endString, token);
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
        token = tryLogin(null);
        arr = new JSONArray();
        arr.put(0, CalendarTestUtils.findOneEventByID(lastID,token).getJSONObject());
    }
    //endregion
    //endregion
}
