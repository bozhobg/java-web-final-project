package bg.softuni.bookexchange.web;

import bg.softuni.bookexchange.model.dto.*;
import bg.softuni.bookexchange.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class CatalogueRestController {

    private final SearchService searchService;

    @Autowired
    public CatalogueRestController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/catalogue/search")
    @ResponseBody
    public List<BookSearchResultDTO> getBooksSearchResults(
            @RequestBody CatalogueSearchRequestDTO searchRequest
    ) {
//        TODO:
        List<BookSearchResultDTO> searchResult = this.searchService.getSearchResult(searchRequest);

        return searchResult;
    }

    @GetMapping("/authors/search")
    public List<AuthorBasicDTO> getAuthorsSearchResults(
            AuthorSearchRequestDTO searchRequest
    ) {
//    TODO:
        return List.of(new AuthorBasicDTO());
    }

    @GetMapping("/genres/search")
    public List<GenreBasicDTO> getGenresSearchResults(
            GenreSearchRequestDTO searchRequest
    ) {
//    TODO:
        return List.of(new GenreBasicDTO());
    }

}
