package de.calendar.repositories;

import de.calendar.model.Event;
import org.springframework.data.repository.CrudRepository;

/**
 * calendar:
 * * de.calendar.repositories:
 * * * Created by KAABERT on 23.01.2017.
 */
public interface EventRepository extends CrudRepository<Event, Long> {
}
