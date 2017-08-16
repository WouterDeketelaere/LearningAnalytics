package be.kul.mai.la.domain.users.roles;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by wouter on 21.12.16.
 */
@Document(collection = "roles")
public abstract class Role {

    @Id
    private String roleId;

    public abstract Collection<? extends GrantedAuthority> getAuthorities();
}
