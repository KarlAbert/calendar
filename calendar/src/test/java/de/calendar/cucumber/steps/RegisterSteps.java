package de.calendar.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;
import de.calendar.CalendarTestUtils;
import de.calendar.Response;
import de.calendar.model.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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
        CalendarTestUtils.register(new User(data.get(0), data.get(1), data.get(2),data.get(3),data.get(4)));
    }
    //endregion

    //region Wenn
    @Wenn("^man sich mit folgenden Benutzerdaten registriert:$")
    public void sichMitFolgendenBenutzerdatenRegistriert(List<String> data) throws Throwable {
        data = data.subList((data.size() / 2), data.size());
        this.response = CalendarTestUtils.register(new User(data.get(0), data.get(1), data.get(2),data.get(3),data.get(4)));;
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
