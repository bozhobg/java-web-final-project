package bg.softuni.bookexchange.web;

import bg.softuni.bookexchange.model.dto.BookCreateDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/books")
public class BookController {

    @GetMapping("/{id}")
    public ModelAndView getBookDetails(
            @PathVariable Long id
    ) {

        return new ModelAndView("book-details");
    }

    @GetMapping("/add")
    public ModelAndView getBookAdd() {

        return new ModelAndView("book-add");
    }

    @PostMapping("/add")
    public ModelAndView postBookAdd(
        BookCreateDTO createDTO
    ) {

        Long newBookId = 0L;

        return new ModelAndView("redirect:/books/" + newBookId);
    }
}
