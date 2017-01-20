package de.calendar.utils;

import de.calendar.model.Token;
import de.calendar.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created TokenGenerator in de.calendar.utils
 * by ARSTULKE on 19.01.2017.
 */
@Component
public class TokenGenerator {
    @Autowired
    private UserRepository userRepository;


    public Token generate() {
        String value = generateRandom();
        while(userRepository.findOneByTokenValue(value) != null) {
            value = generateRandom();
        }

        return new Token(value, LocalDateTime.now().plusHours(24L));
    }

    private String generateRandom() {
        return RandomStringUtils.random(32, true, true);
    }
}
