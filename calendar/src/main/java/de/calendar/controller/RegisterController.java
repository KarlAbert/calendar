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

        String valid = validate(data);
        if (valid == null) {
            User user = new User(data);
            User savedInstance1 = userRepository.findOneByUsername(user.getUsername());
            User savedInstance2 = userRepository.findOneByEmail(user.getEmail());

            if (savedInstance1 == null && savedInstance2 == null) {
                userRepository.save(user);
                return new ResponseEntity<>(new JSONObject().toString(), HttpStatus.CREATED);
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
            return new ResponseEntity<>(
                    new JSONObject()
                            .put("status", HttpStatus.BAD_REQUEST.value())
                            .put("reason", "Der \"" + valid + "\" fehlt im JSONObject des \"Data\" Headers.")
                            .toString(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public static String validate(JSONObject data) {
        if (!data.has("firstname")) return "firstname";
        if (!data.has("lastname")) return "lastname";
        if (!data.has("username")) return "username";
        if (!data.has("email")) return "email";
        if (!data.has("password")) return "password";

        if (data.getString("firstname").length() < 3) return "firstname";
        if (data.getString("lastname").length() < 3) return "lastname";
        if (data.getString("username").length() < 3) return "username";
        if (!data.getString("email").contains("@")) return "email";
        if (!data.getString("email").contains(".")) return "email";
        if (data.getString("password").length() < 8) return "password";

        if (data.getString("firstname").length() > 60) return "firstname";
        if (data.getString("lastname").length() > 60) return "lastname";
        if (data.getString("username").length() > 60) return "username";

        return null;
    }
}
