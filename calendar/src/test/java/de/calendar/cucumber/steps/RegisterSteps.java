package de.calendar.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;
import de.calendar.Response;
import de.calendar.TestUtils;
import org.json.JSONObject;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * calendar:
 * * PACKAGE_NAME:
 * * * Created by KAABERT on 18.01.2017.
 */
public class RegisterSteps {
    private Response response;

    //region Gegeben sei
    @Gegebensei("^der Benutzer mit folgenden Benutzerdaten:$")
    public void derBenutzerMitFolgendenDaten(List<String> data) throws Throwable {
        data = data.subList((data.size() / 2), data.size());

        JSONObject json = new JSONObject()
                .put("firstname", data.get(0))
                .put("lastname", data.get(1))
                .put("username", data.get(2))
                .put("email", data.get(3))
                .put("password", data.get(4));

        TestUtils.post("/user", json);
    }
    //endregion

    //region Wenn
    @Wenn("^man sich mit folgenden Benutzerdaten registriert:$")
    public void sichMitFolgendenBenutzerdatenRegistriert(List<String> data) throws Throwable {
        data = data.subList((data.size() / 2), data.size());

        JSONObject json = new JSONObject()
                .put("firstname", data.get(0))
                .put("lastname", data.get(1))
                .put("username", data.get(2))
                .put("email", data.get(3))
                .put("password", data.get(4));

        this.response = TestUtils.post("/user", json);
    }
    //endregion

    //region Dann
    @Dann("^wird das Registrieren verweigert$")
    public void wirdDasRegistrierenVerweigert() throws Throwable {
        assertThat(response.getStatus(), not(201));
    }

    @Dann("^ist das Registrieren erfolgreich$")
    public void istDasRegistrierenErfolgreich() throws Throwable {
        assertThat(response.getStatus(), is(201));
        assertThat(response.getRawdata(), is(""));
    }
    //endregion
}
