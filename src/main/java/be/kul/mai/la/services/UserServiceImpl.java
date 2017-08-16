package be.kul.mai.la.services;

import be.kul.mai.la.domain.users.User;
import be.kul.mai.la.repositories.UserRepository;
import be.kul.mai.la.services.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by wouter on 21.12.16.
 */
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User addUser(User user) {

        user.setEncryptedPassword(passwordEncoder.encode(user.getEncryptedPassword()));
        return userRepository.save(user);
    }
}
