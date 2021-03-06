/**
 * This file was generated by the JPA Modeler
 */ 

package de.calendar.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Das sind die InvitationTokens zum beitreten eines Events
 * @author  KAABERT
 */

@Entity
@Table(name = "c_invitation")
public class Invitation implements Serializable { 

    @Id
    private String token;

    @Deprecated
    public Invitation() {
    }

    public Invitation(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
