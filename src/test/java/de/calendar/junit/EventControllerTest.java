package de.calendar.junit;

import de.calendar.controller.EventController;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created RegisterControllerTest in de.calendar.junit
 * by ARSTULKE on 19.01.2017.
 */
public class EventControllerTest {
    /**
     * Spezifiziert das Verhalten von {@link EventController#valid(JSONObject)}.
     */
    @Test
    public void validatesdataPost() {
        assertThat(valid(new JSONObject()
                .put("title", "Test1")
                .put("start", "2017-01-21T00:00")
                .put("end", "2017-01-21T00:30")
        ), is(true));

        assertThat(valid(new JSONObject()
                .put("title", "Test1")
                .put("start", "")
                .put("end", "2017-01-21T00:30")
        ), is(true));

        assertThat(valid(new JSONObject()
                .put("title", "Test1")
                .put("start", "2017-01-21T00:00")
                .put("end", "")
        ), is(true));

        assertThat(valid(new JSONObject()
                .put("title", "Test1")
                .put("start", "2017-01-21T00:30")
        ), is(true));

        assertThat(valid(new JSONObject()
                .put("title", "Test1")
                .put("end", "2017-01-21T00:00")
        ), is(true));

        assertThat(valid(new JSONObject()
                .put("start", "2017-01-21T00:00")
                .put("end", "2017-01-21T00:30")
        ), is(false));

        assertThat(valid(new JSONObject()
                .put("title", "Test1")
        ), is(false));

        assertThat(valid(new JSONObject()
                .put("title", "Test1")
                .put("start", "")
                .put("end", "")
        ), is(false));
    }

    private boolean valid(JSONObject jsonObject) {
        return EventController.valid(jsonObject);
    }
}
