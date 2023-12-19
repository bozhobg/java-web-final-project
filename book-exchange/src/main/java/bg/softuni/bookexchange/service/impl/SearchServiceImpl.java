package bg.softuni.bookexchange.service.impl;

import bg.softuni.bookexchange.constants.ErrorMessages;
import bg.softuni.bookexchange.exceptions.SearchParameterException;
import bg.softuni.bookexchange.model.dto.BookSearchResultDTO;
import bg.softuni.bookexchange.model.dto.CatalogueSearchRequestDTO;
import bg.softuni.bookexchange.model.entity.AuthorEntity;
import bg.softuni.bookexchange.model.entity.BookEntity;
import bg.softuni.bookexchange.model.entity.GenreEntity;
import bg.softuni.bookexchange.repository.AuthorRepository;
import bg.softuni.bookexchange.repository.BookRepository;
import bg.softuni.bookexchange.repository.CopyRepository;
import bg.softuni.bookexchange.repository.GenreRepository;
import bg.softuni.bookexchange.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CopyRepository copyRepository;

    @Autowired
    public SearchServiceImpl(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            GenreRepository genreRepository,
            CopyRepository copyRepository
    ) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.copyRepository = copyRepository;
    }

    @Override
    public Set<BookSearchResultDTO> getSearchResult(
            @RequestBody CatalogueSearchRequestDTO searchRequestDTO // TODO: @Valid
    ) {
//        TODO: implement case insensitive searches
        String titleSearchString = searchRequestDTO.getTitle()
                .toLowerCase().trim();
        String authorSearchString = searchRequestDTO.getAuthor()
                .toLowerCase().trim();
        String genreSearchString = searchRequestDTO.getGenre()
                .toLowerCase().trim();
//        use correct getter and setter naming, getter isCopyAvailable() is not mapped at all -> null
        boolean hasAvailableCopy = searchRequestDTO.getIsCopyAvailable();

        Set<BookEntity> booksSearchResult = new HashSet<>();

        if (titleSearchString.isBlank()
                && authorSearchString.isBlank()
                && genreSearchString.isBlank()
        ) {
            booksSearchResult.addAll(this.bookRepository.findAll());
        }

//        Book title direct repo query
        if (!titleSearchString.isBlank()) {
            if (booksSearchResult.isEmpty()) {
//                in order to ignore hierarchy of criteria order of filtration
                booksSearchResult = this.bookRepository.findBookEntityByTitleContainingIgnoreCase(titleSearchString);
            } else {
                booksSearchResult = filterSearchResultByTitleSearchString(booksSearchResult, titleSearchString);
            }
        }

//        Authors direct repo query -> search by 1 or 2 words respectively first | last name or first & last name
        if (!authorSearchString.isBlank()) {
            String[] searchTokens = authorSearchString.split("\\s+");
            validateAuthorSearchStringCount(searchTokens);

            if (booksSearchResult.isEmpty()) {
                Set<BookEntity> booksResult = authorsDirectSearch(searchTokens);
                booksSearchResult.addAll(booksResult);
            } else {
//              filter on already found books
                booksSearchResult = filterSearchResultByAuthorSearchString(booksSearchResult, authorSearchString);
            }
        }

//        Genres direct repo query
        if (!genreSearchString.isBlank()) {
            validateGenreSearchStringCount(genreSearchString);

            if (booksSearchResult.isEmpty()) {
                booksSearchResult.addAll(genresDirectSearch(genreSearchString));
            } else {
//                TODO: filter on already found books
                booksSearchResult = filterSearchResultByGenreSearchString(booksSearchResult, genreSearchString);
            }
        }

        Set<BookSearchResultDTO> dtosSearchResult = getSetSearchResultsDTOs(booksSearchResult);

//        filter only books with available copies
        if (hasAvailableCopy) {
            dtosSearchResult = dtosSearchResult.stream()
                    .filter(dto -> dto.getAvailableCopiesCount().compareTo(0) > 0)
                    .collect(Collectors.toSet());
        }

//        TODO: sorting search results

        return dtosSearchResult;
    }

    private Set<BookEntity> filterSearchResultByTitleSearchString(Set<BookEntity> searchResult, String titleSearchString) {
        return searchResult.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(titleSearchString))
                .collect(Collectors.toSet());
    }

    private Set<BookEntity> authorsDirectSearch(String[] searchTokens) {
        Set<AuthorEntity> authorsResult = new HashSet<>();
        Set<BookEntity> booksByAuthorsResult = new HashSet<>();

        if (searchTokens.length == 2) {
            authorsResult = this.authorRepository
                    .findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(searchTokens[0], searchTokens[1]);
        } else if (searchTokens.length == 1) {
            authorsResult = this.authorRepository.findAllByFirstNameOrLastName(searchTokens[0]);
        }

        for (AuthorEntity author : authorsResult) {
            booksByAuthorsResult.addAll(author.getBooks());
        }

        return booksByAuthorsResult;
    }

    private Set<BookEntity> filterSearchResultByAuthorSearchString(Set<BookEntity> searchResult, String authorSearchString) {
        String[] searchTokens = authorSearchString.split("\\s+");
        Set<BookEntity> filteredResult = new LinkedHashSet<>();

        if (searchTokens.length == 1) {
            filteredResult = filterByAuthorNamesContainingSingleString(searchTokens[0], searchResult);
        } else if (searchTokens.length == 2) {
            filteredResult = filterByAuthorNamesContainingTwoStrings(searchTokens, searchResult);
        }

        return filteredResult;
    }

    private Set<BookEntity> filterByAuthorNamesContainingTwoStrings(
            String[] searchTokens, Set<BookEntity> searchResult
    ) {
        Set<BookEntity> filteredResults = new LinkedHashSet<>();

        for (BookEntity book : searchResult) {
            for (AuthorEntity author : book.getAuthors()) {
                if (author.getFirstName().toLowerCase().contains(searchTokens[0])
                        && author.getLastName().toLowerCase().contains(searchTokens[1])
                ) {
                    filteredResults.add(book);
                }
            }
        }

        return filteredResults;
    }

    private Set<BookEntity> filterByAuthorNamesContainingSingleString(
            String searchToken, Set<BookEntity> searchResult
    ) {
        Set<BookEntity> filteredResults = new LinkedHashSet<>();

        for (BookEntity book : searchResult) {
            for (AuthorEntity author : book.getAuthors()) {
                if (author.getFirstName().toLowerCase().contains(searchToken)
                        || author.getLastName().toLowerCase().contains(searchToken)
                ) {
                    filteredResults.add(book);
                }
            }
        }

        return filteredResults;
    }

    private void validateAuthorSearchStringCount(String[] authorSearchTokens) {
        if (authorSearchTokens.length > 2) {
            throw new SearchParameterException(ErrorMessages.AUTHOR_SEARCH_STRING_COUNT);
        }
    }

    private Set<BookEntity> genresDirectSearch(String genreSearchString) {
        Set<GenreEntity> genresResult = this.genreRepository.findAllByNameContainingIgnoreCase(genreSearchString);
        return new LinkedHashSet<>(
                this.bookRepository.findAllByGenresIn(genresResult)
        );
    }

    private Set<BookEntity> filterSearchResultByGenreSearchString(
            Set<BookEntity> searchResult, String genreSearchString
    ) {
        Set<BookEntity> filteredResult = new LinkedHashSet<>();

        for (BookEntity book : searchResult) {
            Set<GenreEntity> genres = book.getGenres();
            boolean isMatchFound = genres.stream()
                    .map(GenreEntity::getName)
                    .anyMatch(s -> s.toLowerCase().contains(genreSearchString));

            if (isMatchFound) filteredResult.add(book);
        }

        return filteredResult;
    }

    private void validateGenreSearchStringCount(String genreSearchString) {
        if (genreSearchString.split("\\s+").length > 1) {
            throw new SearchParameterException(ErrorMessages.GENRE_SEARCH_STRING);
        }
    }

    private Set<BookSearchResultDTO> getSetSearchResultsDTOs(Set<BookEntity> books) {
        return books.stream()
                .map(this::mapBookToSearchResult)
                .collect(Collectors.toSet());
    }

    private BookSearchResultDTO mapBookToSearchResult(BookEntity book) {

        BookSearchResultDTO searchDto = new BookSearchResultDTO()
                .setBookId(book.getId())
                .setTitle(book.getTitle())
                .setAuthorsFullNames(
                        this.getAuthorsFullNames(book)
                )
                .setGenres(
                        this.getBookGenres(book)
                );

        searchDto.setTotalCopiesCount(
                this.copyRepository.countByBook_Id(book.getId()));
        searchDto.setAvailableCopiesCount(
                this.copyRepository.countByBook_IdAndCurrentTransactionIsNotNull(book.getId()));

        return searchDto;

    }

    private List<String> getAuthorsFullNames(BookEntity bookEntity) {
        return bookEntity.getAuthors()
                .stream()
                .map(this::getAuthorFullName)
                .toList();
    }

    private String getAuthorFullName(AuthorEntity author) {
        String firstName = author.getFirstName() == null ? "" : author.getFirstName() + " ";
        String middleName = author.getMiddleName() == null ? "" : author.getMiddleName() + " ";
        String lastName = author.getLastName();

        return firstName + middleName + lastName;
    }

    private List<String> getBookGenres(BookEntity book) {

        return book.getGenres()
                .stream()
                .map(GenreEntity::getName)
                .toList();
    }

}
