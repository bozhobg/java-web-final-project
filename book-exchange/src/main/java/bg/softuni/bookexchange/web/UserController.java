package bg.softuni.bookexchange.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping("/login")
    public ModelAndView getLogin() {

        return new ModelAndView("user-login");
    }

    @PostMapping("/login")
    public ModelAndView postLogin() {

        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/register")
    public ModelAndView getRegistration() {

        return new ModelAndView("user-register");
    }

    @PostMapping("/register")
    public ModelAndView postRegistration() {

        return new ModelAndView("redirect:/users/login");
    }

    @GetMapping("/{username}")
    public ModelAndView getProfile(@PathVariable(name = "username") Long username) {
//        TODO: general other user info!
        return new ModelAndView("user-profile");
    }

    @GetMapping("/account")
    public ModelAndView getUserAccount() {

        return new ModelAndView("user-profile");
    }

//    TODO: add end point for editing user credentials and data
}
