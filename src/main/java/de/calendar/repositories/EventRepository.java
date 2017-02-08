package de.calendar.repositories;

import de.calendar.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * calendar:
 * * de.calendar.repositories:
 * * * Created by KAABERT on 23.01.2017.
 */
@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
}
