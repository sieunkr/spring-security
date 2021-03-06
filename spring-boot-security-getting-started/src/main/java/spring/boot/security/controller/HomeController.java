package spring.boot.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/cafe_login")
    public String login(){
        return "login";
    }

    @GetMapping("/coffees")
    public String list(){
        return "list";
    }

}
