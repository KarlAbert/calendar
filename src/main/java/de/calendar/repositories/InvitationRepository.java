package de.calendar.repositories;

import de.calendar.model.Invitation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * calendar:
 * * de.calendar.repositories:
 * * * Created by KAABERT on 26.01.2017.
 */
@Repository
public interface InvitationRepository extends CrudRepository<Invitation, String> {
}
