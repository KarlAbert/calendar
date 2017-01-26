package de.calendar.controller;

import de.calendar.model.User;
import de.calendar.repositories.UserRepository;
import de.calendar.utils.TokenGenerator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created LoginController in de.calendar.controller
 * by ARSTULKE on 19.01.2017.
 */
@SuppressWarnings("unused")
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private Authorization authorization;

    @PostMapping("/user")
    public ResponseEntity<String> loginOrRegister(@RequestBody String dataString) {
        JSONObject data = new JSONObject(dataString);
        if (register(data)) {
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
        } else if (login(data)) {
            User user = userRepository.findOneByUsernameAndPassword(data.getString("username"), data.getString("password"));
            if (user == null) {
                return new ResponseEntity<>(
                        new JSONObject()
                                .put("status", HttpStatus.FORBIDDEN.value())
                                .put("reason", "Unknown credentials")
                                .toString(),
                        HttpStatus.FORBIDDEN
                );
            } else {
                if (user.getToken() == null || user.getToken().isExpired()) {
                    user.setToken(tokenGenerator.generate());
                    userRepository.save(user);
                }

                return new ResponseEntity<>(
                        new JSONObject()
                                .put("token", user.getToken().getValue())
                                .put("expiring", user.getToken().getExpiringDate())
                                .toString(),
                        HttpStatus.OK
                );

            }
        } else {
            return new ResponseEntity<>("--Login--\n" +
                    "You have to set the http header \"data\" as json.\n" +
                    "The json object should contain the following keys:\n" +
                    " - username\n" +
                    " - password" +
                    "\n" +
                    "\n" +
                    "--Register--\n" +
                    "You have to set the http header \"data\" as json.\n" +
                    "The json object should contain the following keys:\n" +
                    " - firstname\n" +
                    " - lastname\n" +
                    " - username\n" +
                    " - email\n" +
                    " - password", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUser(@RequestHeader(name = "Authorization") String token) {
        return authorization.authorize(token, null, (user, data) -> new ResponseEntity<>(new JSONObject()
                .put("username", user.getUsername())
                .put("email", user.getEmail())
                .put("firstname", user.getFirstname())
                .put("lastname", user.getLastname())
                .put("token", user.getToken().getValue())
                .put("expiring", user.getToken().getExpiringDate())
                .toString(), HttpStatus.OK));
    }

    public static boolean login(JSONObject data) {
        return data.has("username") && data.has("password");
    }


    public static boolean register(JSONObject data) {
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
