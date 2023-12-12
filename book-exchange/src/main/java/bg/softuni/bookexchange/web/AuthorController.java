package bg.softuni.bookexchange.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @GetMapping("/add")
    public ModelAndView getAuthorAdd() {

        return new ModelAndView("author-add");
    }
}
