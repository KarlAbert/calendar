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
     * Spezifiziert das Verhalten von {@link RegisterController#validate(JSONObject)}.
     */
    @Test
    public void validatesDataPost() {
        assertThat(validate(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "arne@example.com")
                .put("password", "Test1234")
        ), is(nullValue()));

        assertThat(validate(new JSONObject()
                .put("firstname", "Arnedewiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "arne@example.com")
                .put("password", "Test1234")
        ), is(notNullValue()));

        assertThat(validate(new JSONObject()
                .put("firstname", "")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "arne@example.com")
                .put("password", "Test1234")
        ), is(notNullValue()));


        assertThat(validate(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "")
                .put("username", "arstulke")
                .put("email", "arne@example.com")
                .put("password", "Test1234")
        ), is(notNullValue()));

        assertThat(validate(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "Stulken")
                .put("username", "ar")
                .put("email", "arne@example.com")
                .put("password", "Test1234")
        ), is(notNullValue()));

        assertThat(validate(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "arne_example.com")
                .put("password", "Test1234")
        ), is(notNullValue()));

        assertThat(validate(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "")
                .put("password", "Test1234")
        ), is(notNullValue()));

        assertThat(validate(new JSONObject()
                .put("firstname", "Arne")
                .put("lastname", "Stulken")
                .put("username", "arstulke")
                .put("email", "arne@example.com")
                .put("password", "zukurz")
        ), is(notNullValue()));

        assertThat(validate(new JSONObject()
                .put("username", "arstulke")
                .put("password", "Test1234")
        ), is(notNullValue()));
    }

    private String validate(JSONObject jsonObject) {
        return RegisterController.validate(jsonObject);
    }
}
