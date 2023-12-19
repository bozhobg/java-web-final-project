package bg.softuni.bookexchange.service;

import bg.softuni.bookexchange.model.dto.BookSearchResultDTO;
import bg.softuni.bookexchange.model.dto.CatalogueSearchRequestDTO;

import java.util.Set;

public interface SearchService {

    Set<BookSearchResultDTO> getSearchResult(
            CatalogueSearchRequestDTO searchRequestDTO
    );
}
