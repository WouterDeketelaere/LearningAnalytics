package be.kul.mai.la.services.api;


import be.kul.mai.la.domain.users.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by wouter on 21.12.16.
 */
public interface UserService extends UserDetailsService {

    User addUser(User user);
}
