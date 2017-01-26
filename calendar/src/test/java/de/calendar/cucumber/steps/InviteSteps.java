package de.calendar.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import de.calendar.CalendarTestUtils;
import de.calendar.Response;
import de.calendar.TestUtils;
import de.calendar.model.Event;

import static de.calendar.TestUtils.forceLogin;
import static de.calendar.TestUtils.tryLogin;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * calendar:
 * * de.calendar.cucumber.steps:
 * * * Created by KAABERT on 23.01.2017.
 */
public class InviteSteps {
    private String token;
    private Response response;
    private String url;

    private Long lastID;

    @Gegebensei("^das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" in dem Kalendar von \"([^\"]*)\"$")
    public void dasGanztägigeEreignisAmInDemKalendarVon(String title, String date, String executer) throws Throwable {
        Response response = CalendarTestUtils.createDaylongEvent(title, date, forceLogin(null, executer, "passwort"));
        lastID = response.getJSONObject().getLong("id");
    }

    @Wenn("^TestUser den Einladelink vom Ereignis \"([^\"]*)\" am \"([^\"]*)\" generiert$")
    public void testuserDenEinladelinkVomEreignisAmGeneriert(String title, String date) throws Throwable {
        token = tryLogin(null);
        if (lastID == null) {
            Event event = CalendarTestUtils.findOneEventByTitleAndDate(title, date + " 00:00", date + " 23:59", token);
            if(event != null){
                lastID = event.getID();
            }
        }
        response = CalendarTestUtils.inviteToEvent(lastID, token);
        if (response.getStatus() == 200) {
            this.url = response.getJSONObject().getString("url");
        } else {
            this.url = null;
        }
        lastID = null;
    }

    @Und("^\"([^\"]*)\" den Einladelink aufruft$")
    public void denEinladelinkAufruft(String executer) throws Throwable {
        response = TestUtils.put(url, forceLogin(null, executer, "passwort"), null);
    }

    @Dann("^nimmt \"([^\"]*)\" an dem Ereignis \"([^\"]*)\" am \"([^\"]*)\" von TestUser teil$")
    public void nimmtAnDemEreignisAmVonTestUserTeil(String executer, String title, String date) throws Throwable {
        CalendarTestUtils.findOneEventByTitleAndDate(title, date + " 00:00", date + " 23:59", tryLogin(null, executer, "passwort"));
    }

    @Dann("^wird die Anfrage abgelehnt$")
    public void wirdDieAnfrageAbgelehnt() throws Throwable {
        assertThat(response.getStatus(), not(200));
    }

    @Dann("^wird die Einladung abgelehnt$")
    public void wirdDieEinladungAbgelehnt() throws Throwable {
        assertThat(response.getStatus(), not(200));
    }
}
