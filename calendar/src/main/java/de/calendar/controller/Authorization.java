package de.calendar.controller;

import de.calendar.model.User;
import de.calendar.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
class Authorization {
    @Autowired
    private UserRepository userRepository;

    ResponseEntity<String> authorize(String token, String dataString, Function function) {
        User user = userRepository.findOneByTokenValue(token);
        if (user == null || user.getToken().isExpired()) {
            return new ResponseEntity<>("Invalid or expired token.", HttpStatus.UNAUTHORIZED);
        } else {
            return function.doSomething(user, dataString == null ? null : new JSONObject(dataString));
        }
    }

    public interface Function {
        ResponseEntity<String> doSomething(User user, JSONObject data);
    }
}