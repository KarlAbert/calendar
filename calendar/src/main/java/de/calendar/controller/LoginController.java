package de.calendar.controller;

import de.calendar.model.User;
import de.calendar.repositories.UserRepository;
import de.calendar.utils.TokenGenerator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created LoginController in de.calendar.controller
 * by ARSTULKE on 19.01.2017.
 */
@RestController
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenGenerator tokenGenerator;

    @RequestMapping("/login")
    public ResponseEntity<String> login(@RequestHeader(name = "Data") String dataString) {
        JSONObject data = new JSONObject(dataString);
        if (valid(data)) {
            User user = userRepository.findOneByUsernameAndPassword(data.getString("username"), data.getString("password"));
            if (user == null) {
                return new ResponseEntity<>(
                        new JSONObject()
                                .put("status", HttpStatus.FORBIDDEN.value())
                                .put("reason", "Unknown Credentials")
                                .toString(),
                        HttpStatus.FORBIDDEN
                );
            } else {
                if(user.getToken() == null || user.getToken().isExpired()) {
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
            return new ResponseEntity<>("You have to set the http header \"Data\" as json.\n" +
                    "The json object should contain the following keys:\n" +
                    " - username\n" +
                    " - password", HttpStatus.BAD_REQUEST);
        }
    }

    public static boolean valid(JSONObject data) {
        if (!data.has("username")) return false;
        if (!data.has("password")) return false;

        return true;
    }
}
