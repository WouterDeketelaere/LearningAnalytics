package be.kul.mai.la.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wouter on 31.01.17.
 */
@Controller
public class LoginController {

    // Login form
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    // Login form with error
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
