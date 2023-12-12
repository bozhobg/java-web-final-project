package bg.softuni.bookexchange.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/copies")
public class CopyController {

    @GetMapping("/{id}")
    public ModelAndView getCopyDetails(
            @PathVariable Long id
    ) {

        return new ModelAndView("copy-details");
    }

    @PostMapping("/add")
    public ModelAndView createCopy() {

        Long newCopyId = 0L;

        return new ModelAndView("/copies/" + newCopyId);
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteCopy(
            @PathVariable Long id
    ) {

        return new ModelAndView("redirect:/books/catalogue");
    }
}
