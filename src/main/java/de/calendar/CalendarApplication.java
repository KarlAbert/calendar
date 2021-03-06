package de.calendar;

import de.calendar.model.User;
import de.calendar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CalendarApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CalendarApplication.class, args);
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CalendarApplication.class);
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

    public static HttpHeaders setJSONContentType() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }
}
