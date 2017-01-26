package de.calendar.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import de.calendar.CalendarTestUtils;
import de.calendar.Response;
import de.calendar.model.Event;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * calendar:
 * * de.calendar.cucumber.steps:
 * * * Created by KAABERT on 23.01.2017.
 */
public class InviteSteps {
    private Response response;

    private Long lastID;

    private Long joinID;
    private String invitationToken;

    @Gegebensei("^das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" in dem Kalendar von \"([^\"]*)\"$")
    public void dasGanztägigeEreignisAmInDemKalendarVon(String title, String date, String executer) throws Throwable {
        Response response = CalendarTestUtils.createEvent(new Event(title, date + " 00:00", date + " 23:59"), CalendarTestUtils.registerAndLogin(executer));
        lastID = response.getJSONObject().getLong("id");
    }

    @Wenn("^TestUser den Einladelink vom Ereignis \"([^\"]*)\" am \"([^\"]*)\" generiert$")
    public void testuserDenEinladelinkVomEreignisAmGeneriert(String title, String date) throws Throwable {
        String token = CalendarTestUtils.login(CalendarTestUtils.testUsername, CalendarTestUtils.testUserPassword).getJSONObject().getString("token");
        if (lastID == null) {
            Event event = CalendarTestUtils.findEvent(title, date + " 00:00", date + " 23:59", token);
            if(event != null){
                lastID = event.getId();
            }
        }
        response = CalendarTestUtils.inviteEvent(lastID, token);
        if (response.getStatus() == 201) {
            this.invitationToken = response.getJSONObject().getString("invitationToken");
            this.joinID = lastID;
        } else {
            this.invitationToken = null;
            this.joinID = null;
        }
        lastID = null;
    }

    @Und("^\"([^\"]*)\" den Einladelink aufruft$")
    public void denEinladelinkAufruft(String executer) throws Throwable {
        response = CalendarTestUtils.joinEvent(joinID, invitationToken, CalendarTestUtils.registerAndLogin(executer));
    }

    @Dann("^nimmt \"([^\"]*)\" an dem Ereignis \"([^\"]*)\" am \"([^\"]*)\" von TestUser teil$")
    public void nimmtAnDemEreignisAmVonTestUserTeil(String executer, String title, String date) throws Throwable {
        CalendarTestUtils.findEvent(title, date + " 00:00", date + " 23:59", CalendarTestUtils.login(executer, "passwort").getJSONObject().getString("token"));
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
