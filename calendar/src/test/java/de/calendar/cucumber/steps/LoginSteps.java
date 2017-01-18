package de.calendar.cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Wenn;

/**
 * calendar:
 * * de.calendar.cucumber.steps:
 * * * Created by KAABERT on 18.01.2017.
 */
public class LoginSteps {

    @Wenn("^man sich mit dem Benutzername \"([^\"]*)\" und dem Passwort \"([^\"]*)\" anmeldet$")
    public void manSichMitDemBenutzernameUndDemPasswortAnmeldet(String username, String password) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Dann("^wird der Zugriff verweigert$")
    public void wirdIhmDerZugriffVerweigert() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Dann("^wird der Zugriff gewährt$")
    public void wirdDerZugriffGewährt() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
