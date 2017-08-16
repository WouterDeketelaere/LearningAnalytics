package be.kul.mai.la.repositories;

import be.kul.mai.la.domain.users.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by wouter on 21.12.16.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserByUsername(String username);
}
