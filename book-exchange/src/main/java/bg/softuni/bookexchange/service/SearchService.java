package bg.softuni.bookexchange.service;

import bg.softuni.bookexchange.model.dto.BookSearchResultDTO;
import bg.softuni.bookexchange.model.dto.CatalogueSearchRequestDTO;

import java.util.List;

public interface SearchService {

    List<BookSearchResultDTO> getSearchResult(
            CatalogueSearchRequestDTO searchRequestDTO
    );
}
