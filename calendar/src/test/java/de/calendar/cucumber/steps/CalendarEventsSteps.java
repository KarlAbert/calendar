package de.calendar.cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import de.calendar.Response;
import de.calendar.TestUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * calendar:
 * * PACKAGE_NAME:
 * * * Created by KAABERT on 18.01.2017.
 */
public class CalendarEventsSteps {
    private final String domain = "http://localhost:8080";

    private final DateFormat DATE_FORMAT_DAYLONG = new SimpleDateFormat("dd.mm.YYYY");
    private final DateFormat DATE_FORMAT_TIMESPAN = new SimpleDateFormat("dd.mm.YYYY HH:MM");

    //region ganztägig
    //region gegeben sei
    @Gegebensei("^das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" in dem Kalendar von TestUser$")
    public void dasEreignisAmInDemKalendarVonTestUser(String title, String dateString) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
    //endregion

    //region wenn
    @Wenn("^TestUser das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" löscht$")
    public void testuserDasEreignisAmLöscht(String title, String dateString) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Wenn("^TestUser das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" zu \"([^\"]*)\" am \"([^\"]*)\" ändert$")
    public void testuserDasEreignisAmZuAmÄndert(String title1, String dateString1, String title2, String dateString2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Wenn("^TestUser die Ereignisse zwischen dem \"([^\"]*)\" und dem \"([^\"]*)\" anzeigen lässt$")
    public void testuserDieEreignisseZwischenDemUndDemAnzeigenLässt(String fromString, String untilString) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Wenn("^TestUser ein ganztägiges Ereigniss am \"([^\"]*)\" mit dem Titel \"([^\"]*)\" erstellt$")
    public void testuserEinEreignissAmMitDemTitelErstellt(String dateString, String title) throws Throwable {
        Response response = TestUtils.post(domain, "/event/create", token, json);
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
    //endregion

    //region dann
    @Dann("^existiert kein Ereignis am \"([^\"]*)\" im Kalendar von TestUser$")
    public void existiertKeinEreignisAmImKalendarVonTestUser(String dateString) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Dann("^werden TestUser folgende Ergebnisse zurückgegeben:$")
    public void werdenTestUserFolgendeErgebnisseZurückgegeben(Map<String, String> data) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Dann("^steht das ganztägige Ereignis \"([^\"]*)\" am \"([^\"]*)\" in dem Kalender von TestUser$")
    public void stehtDasEreignisAmInDemKalenderVonTestUser(String title, String dateString) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
    //endregion
    //endregion

    //region zeitgebunden
    //region gegenben sei
    @Gegebensei("^das Ereignis \"([^\"]*)\"  von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr in dem Kalendar von TestUser$")
    public void dasEreignisVonUhrBisUhrInDemKalendarVonTestUser(String arg0, String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
    //endregion
    //region wenn
    @Wenn("^TestUser ein Ereigniss mit dem Titel \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr erstellt$")
    public void testuserEinEreignissMitDemTitelVonUhrBisUhrErstellt(String arg0, String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Wenn("^TestUser das Ereignis \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr zu \"([^\"]*)\" am \"([^\"]*)\" zwischen \"([^\"]*)\"Uhr und \"([^\"]*)\"Uhr ändert$")
    public void testuserDasEreignisVonUhrBisUhrZuAmZwischenUhrUndUhrÄndert(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
    //endregion
    //region dann
    @Dann("^steht das Ereignis \"([^\"]*)\" von \"([^\"]*)\"Uhr bis \"([^\"]*)\"Uhr in dem Kalender von TestUser$")
    public void stehtDasEreignisVonUhrBisUhrInDemKalenderVonTestUser(String arg0, String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
    //endregion
    //endregion
}
