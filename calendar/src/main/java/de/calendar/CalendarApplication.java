package de.calendar;

import de.calendar.model.User;
import de.calendar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CalendarApplication {

    public CalendarApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(CalendarApplication.class, args);
    }

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        try {
            User user = userRepository.findOneByEmail("testuser1@example.com");
            if (user == null) {
                userRepository.save(new User(
                        "Test",
                        "User",
                        "TestUser1",
                        "testuser1@example.com",
                        "Password1"
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
