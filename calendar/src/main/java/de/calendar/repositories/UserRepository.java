package de.calendar.repositories;

import de.calendar.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created UserRepository in de.calendar.controller
 * by ARSTULKE on 19.01.2017.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByUsername(String username);
    User findOneByEmail(String email);
}
