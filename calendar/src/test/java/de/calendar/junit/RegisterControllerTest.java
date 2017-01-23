package de.calendar.junit;

import de.calendar.controller.RegisterController;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created RegisterControllerTest in de.calendar.junit
 * by ARSTULKE on 19.01.2017.
 */
public class RegisterControllerTest {
    /**
     * Spezifiziert das Verhalten von {@link RegisterController#valid(JSONObject)}.
     */
    @Test
    public void validatesdataPost() {
        assertThat(valid(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "arne@example.com")
                .put("password", "Test1234")
        ), is(true));

        assertThat(valid(new JSONObject()
                .put("firstname", "Arnedewiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "arne@example.com")
                .put("password", "Test1234")
        ), is(false));

        assertThat(valid(new JSONObject()
                .put("firstname", "")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "arne@example.com")
                .put("password", "Test1234")
        ), is(false));


        assertThat(valid(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "")
                .put("username", "arstulke")
                .put("email", "arne@example.com")
                .put("password", "Test1234")
        ), is(false));

        assertThat(valid(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "Stulken")
                .put("username", "ar")
                .put("email", "arne@example.com")
                .put("password", "Test1234")
        ), is(false));

        assertThat(valid(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "arne_example.com")
                .put("password", "Test1234")
        ), is(false));

        assertThat(valid(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "")
                .put("password", "Test1234")
        ), is(false));

        assertThat(valid(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "arne@example.com")
                .put("password", "zukurz")
        ), is(false));

        assertThat(valid(new JSONObject()
                .put("username", "arstulke")
                .put("password", "Test1234")
        ), is(false));
    }

    private boolean valid(JSONObject jsonObject) {
        return RegisterController.valid(jsonObject);
    }
}
