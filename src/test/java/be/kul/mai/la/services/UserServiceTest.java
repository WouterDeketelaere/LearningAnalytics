package be.kul.mai.la.services;

import be.kul.mai.la.domain.users.User;
import be.kul.mai.la.domain.users.roles.Administrator;
import be.kul.mai.la.domain.users.roles.Role;
import be.kul.mai.la.services.api.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testAddUser() {

        List<Role> roles = new ArrayList<>();
        roles.add(new Administrator());
        User wouter = new User("wouter", "Wouter", "Deketelaere", "wouter.deketelaere@kdg.be", "wouter", roles);
        userService.addUser(wouter);
    }

}
