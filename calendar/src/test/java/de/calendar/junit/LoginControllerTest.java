package de.calendar.junit;

import de.calendar.controller.LoginController;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created RegisterControllerTest in de.calendar.junit
 * by ARSTULKE on 19.01.2017.
 */
public class LoginControllerTest {
    /**
     * Spezifiziert das Verhalten von {@link LoginController#valid(JSONObject)}.
     */
    @Test
    public void validatesdataPost() {
        assertThat(valid(new JSONObject()
                .put("username", "arstulke")
                .put("password", "Test1234")
        ), is(true));

        assertThat(valid(new JSONObject()
                .put("username", "arstulke")
        ), is(false));

        assertThat(valid(new JSONObject()
                .put("password", "Test1234")
        ), is(false));
    }

    private boolean valid(JSONObject jsonObject) {
        return LoginController.valid(jsonObject);
    }
}
