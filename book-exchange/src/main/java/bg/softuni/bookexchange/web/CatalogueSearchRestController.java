package bg.softuni.bookexchange.web;

import bg.softuni.bookexchange.model.dto.*;
import bg.softuni.bookexchange.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class CatalogueSearchRestController {

    private final SearchService searchService;

    @Autowired
    public CatalogueSearchRestController(SearchService searchService) {
        this.searchService = searchService;
    }

//    changed from get to post, get request should not have a body due interoperation ability between systems (older)
//    possible - done with insomnia get requests
//    standard semantics
//    fetch API and Spring (HttpMessageNotReadableException) should throw errors when body is passed
//    http query method ?
    @PostMapping("/catalogue/search")
    @ResponseBody
    public Set<BookSearchResultDTO> getBooksSearchResults(
            @RequestBody CatalogueSearchRequestDTO searchRequest
    ) {
//        TODO:
        Set<BookSearchResultDTO> searchResult = this.searchService.getSearchResult(searchRequest);

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
