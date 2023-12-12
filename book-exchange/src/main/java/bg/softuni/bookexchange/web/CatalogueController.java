package bg.softuni.bookexchange.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/catalogue")
public class CatalogueController {

    @GetMapping
    public ModelAndView getCatalogue() {

        return new ModelAndView("books-catalogue");
    }
}
