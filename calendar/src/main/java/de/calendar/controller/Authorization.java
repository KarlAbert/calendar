package de.calendar.controller;

import de.calendar.CalendarApplication;
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
            return new ResponseEntity<>(new JSONObject()
                    .put("reason", "Invalid or expired token.")
                    .toString(),
                    CalendarApplication.setJSONContentType(),
                    HttpStatus.UNAUTHORIZED);
        } else {
            ResponseEntity<String> response = function.doSomething(user, dataString == null ? null : new JSONObject(dataString));
            if (response.hasBody() && (response.getBody().startsWith("{") || response.getBody().startsWith("["))) {
                return new ResponseEntity<>(response.getBody(), CalendarApplication.setJSONContentType(), response.getStatusCode());
            }
            return response;
        }
    }

    public interface Function {
        ResponseEntity<String> doSomething(User user, JSONObject data);
    }
}