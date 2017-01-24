package de.calendar.repositories;

import de.calendar.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created UserRepository in de.calendar.controller
 * by ARSTULKE on 19.01.2017.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByUsername(String username);
    User findOneByEmail(String email);

    User findOneByUsernameAndPassword(String username, String password);

    User findOneByTokenValue(String value);

    List<User> findAll();
}
