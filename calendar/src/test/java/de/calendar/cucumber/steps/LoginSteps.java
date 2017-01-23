package de.calendar.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Wenn;
import de.calendar.Response;
import de.calendar.TestUtils;
import org.json.JSONObject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * calendar:
 * * de.calendar.cucumber.steps:
 * * * Created by KAABERT on 18.01.2017.
 */
public class LoginSteps {

    private Response response;

    @Wenn("^man sich mit dem Benutzername \"([^\"]*)\" und dem Passwort \"([^\"]*)\" anmeldet$")
    public void manSichMitDemBenutzernameUndDemPasswortAnmeldet(String username, String password) throws Throwable {
        response = TestUtils.login(username, password);
    }

    @Dann("^wird der Zugriff verweigert$")
    public void wirdIhmDerZugriffVerweigert() throws Throwable {
        assertThat(response.getStatus(), is(403));
    }

    @Dann("^wird der Zugriff gewährt$")
    public void wirdDerZugriffGewährt() throws Throwable {
        JSONObject responsedata = response.getJSONObject();

        assertTrue("Wrong Response Format", responsedata.has("token") && responsedata.has("expiring"));
        assertThat(response.getStatus(), is(200));
    }
}
