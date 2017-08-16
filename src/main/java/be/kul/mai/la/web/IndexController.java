package be.kul.mai.la.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wouter on 31.01.17.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
