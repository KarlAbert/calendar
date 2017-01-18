package de.calendar.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;
import de.calendar.Response;
import de.calendar.CalendarTestUtils;
import de.calendar.model.Event;
import org.json.JSONArray;

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
    private Response containsResponse;
    private JSONArray arr;

    //region ganztägig
    //region gegeben sei
    @Gegebensei("^das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" in dem Kalendar von TestUser$")
    public void dasEreignisAmInDemKalendarVonTestUser(String title, String dateString) throws Throwable {
        token = tryLogin(token);
        CalendarTestUtils.createDaylongEvent(title, dateString, token);
    }
    //endregion

    //region wenn
    @Wenn("^TestUser das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" löscht$")
    public void testuserDasEreignisAmLöscht(String title, String dateString) throws Throwable {
        token = tryLogin(token);
        Event event = CalendarTestUtils.findOneEventByTitleAndDate(title, dateString + " 00:00", dateString + " 23:59", token);
        CalendarTestUtils.deleteEvent(event, token);
    }

    @Wenn("^TestUser das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" zu \"([^\"]*)\" am \"([^\"]*)\" ändert$")
    public void testuserDasEreignisAmZuAmÄndert(String title1, String dateString1, String title2, String dateString2) throws Throwable {
        token = tryLogin(token);

        Event event = CalendarTestUtils.findOneEventByTitleAndDate(title1, dateString1 + " 00:00", dateString1 + " 23:59", token);
        if (event == null) {
            fail("Kalendarereigniss konnte nicht gefunden werden.");
        }
        event.setTitle(title2);
        event.setFrom(dateString2 + " 00:00");
        event.setUntil(dateString2 + " 23:59");

        CalendarTestUtils.saveEvent(event, token);
    }

    @Wenn("^TestUser die Ereignisse zwischen dem \"([^\"]*)\" und dem \"([^\"]*)\" anzeigen lässt$")
    public void testuserDieEreignisseZwischenDemUndDemAnzeigenLässt(String fromString, String untilString) throws Throwable {
        token = tryLogin(token);
        arr = CalendarTestUtils.findAllEventsByDate(fromString + " 00:00", untilString + " 23:59", token);
    }

    @Wenn("^TestUser ein ganztägiges Ereigniss am \"([^\"]*)\" mit dem Titel \"([^\"]*)\" erstellt$")
    public void testuserEinEreignissAmMitDemTitelErstellt(String dateString, String title) throws Throwable {
        token = tryLogin(token);
        this.containsResponse = CalendarTestUtils.createDaylongEvent(title, dateString, token);
    }
    //endregion

    //region dann
    @Dann("^existiert kein Ereignis am \"([^\"]*)\" im Kalendar von TestUser$")
    public void existiertKeinEreignisAmImKalendarVonTestUser(String dateString) throws Throwable {
        token = tryLogin(token);
        assertThat(CalendarTestUtils.findAllEventsByDate(dateString, dateString, token).length(), is(0));
    }

    @Dann("^werden TestUser folgende Ergebnisse zurückgegeben:$")
    public void werdenTestUserFolgendeErgebnisseZurückgegeben(Map<String, String> data) throws Throwable {
        for (Map.Entry<String, String> eventEntry : data.entrySet()) {
            boolean found = false;
            for (int i = 0; i < arr.length(); i++) {
                if (arr.getJSONObject(i).getString("title").equals(eventEntry.getKey())
                        && arr.getJSONObject(i).getString("from").equals(eventEntry.getValue() + " 00:00")
                        && arr.getJSONObject(i).getString("until").equals(eventEntry.getValue() + " 23:59")) {
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
        assertThat(containsResponse.getStatus(), is(200));

        JSONArray contains = CalendarTestUtils.findAllEventsByDate(dateString, dateString, token);
        fail("implement assertion");
    }
    //endregion
    //endregion

    //region zeitgebunden
    //region gegenben sei
    @Gegebensei("^das Ereignis \"([^\"]*)\"  von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr in dem Kalendar von TestUser$")
    public void dasEreignisVonUhrBisUhrInDemKalendarVonTestUser(String title, String fromString, String untilString) throws Throwable {
        token = tryLogin(token);
        CalendarTestUtils.createTimespanEvent(title, fromString, untilString, token);
    }

    //endregion
    //region wenn
    @Wenn("^TestUser ein Ereigniss mit dem Titel \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr erstellt$")
    public void testuserEinEreignissMitDemTitelVonUhrBisUhrErstellt(String title, String fromString, String untilString) throws Throwable {
        token = tryLogin(token);
        CalendarTestUtils.createTimespanEvent(title, fromString, untilString, token);
    }

    @Wenn("^TestUser das Ereignis \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr zu \"([^\"]*)\" zwischen \"([^\"]*)\"Uhr und \"([^\"]*)\"Uhr ändert$")
    public void testuserDasEreignisVonUhrBisUhrZuAmZwischenUhrUndUhrÄndert(String title1, String fromString1, String untilString1, String title2, String fromString2, String untilString2) throws Throwable {
        token = tryLogin(token);
        Event event = CalendarTestUtils.findOneEventByTitleAndDate(title1, fromString1, untilString1, token);
        if (event == null) {
            fail("Kalendarereigniss konnte nicht gefunden werden.");
        } else {
            event.setTitle(title2);
            event.setFrom(fromString2);
            event.setUntil(untilString2);
            CalendarTestUtils.saveEvent(event,token);
        }
    }

    //endregion
    //region dann
    @Dann("^steht das Ereignis \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr in dem Kalender von TestUser$")
    public void stehtDasEreignisVonUhrBisUhrInDemKalenderVonTestUser(String title, String fromString, String untilString) throws Throwable {
        token = tryLogin(token);
        Event event = CalendarTestUtils.findOneEventByTitleAndDate(title, fromString, untilString, token);
        if (event == null) {
            fail("Kalendarereignis konnte nicht gefunen werden.");
        } else {
            assertThat(event.getTitle(), is(title));
            assertThat(event.getFrom(), is(Event.parse(fromString)));
            assertThat(event.getUntil(), is(Event.parse(untilString)));
        }
    }
    //endregion
    //endregion
}
