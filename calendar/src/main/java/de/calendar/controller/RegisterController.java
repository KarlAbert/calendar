package de.calendar.controller;

import de.calendar.model.User;
import de.calendar.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created RegisterController in de.calendar.controller
 * by ARSTULKE on 19.01.2017.
 */
@RestController
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/register")
    public ResponseEntity<String> register(@RequestHeader("data") String dataString) {
        JSONObject data = new JSONObject(dataString);
        if (valid(data)) {
            User user = new User(data);
            User savedInstance1 = userRepository.findOneByUsername(user.getUsername());
            User savedInstance2 = userRepository.findOneByEmail(user.getEmail());

            if (savedInstance1 == null && savedInstance2 == null) {
                userRepository.save(user);
                return new ResponseEntity<>("", HttpStatus.CREATED);
            } else {
                String reason = (savedInstance1 != null ? "Der \"username" : "Die \"E-Mail") + "\" ist bereits vergeben.";
                return new ResponseEntity<>(
                        new JSONObject()
                                .put("status", HttpStatus.CONFLICT.value())
                                .put("reason", reason)
                                .toString(),
                        HttpStatus.CONFLICT
                );
            }
        } else {
            return new ResponseEntity<>("You have to set the http header \"Data\" as json.\n" +
                    "The json object should contain the following keys:\n" +
                    " - firstname\n" +
                    " - lastname\n" +
                    " - username\n" +
                    " - email\n" +
                    " - password", HttpStatus.BAD_REQUEST);
        }
    }

    public static boolean valid(JSONObject data) {
        if (!data.has("firstname")) return false;
        if (!data.has("lastname")) return false;
        if (!data.has("username")) return false;
        if (!data.has("email")) return false;
        if (!data.has("password")) return false;

        if (data.getString("firstname").length() < 3) return false;
        if (data.getString("lastname").length() < 3) return false;
        if (data.getString("username").length() < 3) return false;
        if (!data.getString("email").contains("@")) return false;
        if (!data.getString("email").contains(".")) return false;
        if (data.getString("password").length() < 8) return false;

        if (data.getString("firstname").length() > 60) return false;
        if (data.getString("lastname").length() > 60) return false;
        if (data.getString("username").length() > 60) return false;

        return true;
    }
}
