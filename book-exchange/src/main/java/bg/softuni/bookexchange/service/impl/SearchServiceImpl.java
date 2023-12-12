package bg.softuni.bookexchange.service.impl;

import bg.softuni.bookexchange.model.dto.BookSearchResultDTO;
import bg.softuni.bookexchange.model.dto.CatalogueSearchRequestDTO;
import bg.softuni.bookexchange.model.entity.AuthorEntity;
import bg.softuni.bookexchange.model.entity.BookEntity;
import bg.softuni.bookexchange.model.entity.CopyEntity;
import bg.softuni.bookexchange.model.entity.GenreEntity;
import bg.softuni.bookexchange.repository.AuthorRepository;
import bg.softuni.bookexchange.repository.BookRepository;
import bg.softuni.bookexchange.repository.CopyRepository;
import bg.softuni.bookexchange.repository.GenreRepository;
import bg.softuni.bookexchange.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
            GenreRepository genreRepository, CopyRepository copyRepository
    ) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.copyRepository = copyRepository;
    }

    @Override
    public List<BookSearchResultDTO> getSearchResult(
            CatalogueSearchRequestDTO searchRequestDTO
    ) {
        Set<BookEntity> searchResult = new HashSet<>();
        String titleSearchString = searchRequestDTO.getTitle();
        String authorSearchString = searchRequestDTO.getAuthor();
        String genreSearchString = searchRequestDTO.getGenre();
        boolean hasAvailableCopy = searchRequestDTO.isCopyAvailable();

//        Book title direct repo query
        if (!titleSearchString.isBlank()) {
            searchResult = this.bookRepository.findBookEntityByTitleContaining(titleSearchString);
        }

//        Authors direct repo query -> search by 1 or 2 words respectively first | last name or first & last name
//        TODO: filter on already found books
        if (!authorSearchString.isBlank() && searchResult.isEmpty()) {
            Set<BookEntity> booksResult = authorsDirectSearch(authorSearchString);
            searchResult.addAll(booksResult);
        }

//        Genres direct repo query
//        TODO: filter on already found books
        if (!genreSearchString.isBlank() && searchResult.isEmpty()) {
            searchResult.addAll(
                    genresDirectSearch(genreSearchString)
            );
        }

        if (searchResult.isEmpty()) {
            Map<BookEntity, Integer> mapBookAvailableCopies = new HashMap<>();
            Set<CopyEntity> availableCopies = this.copyRepository.findAllByCurrentTransactionNotNull();

            for (CopyEntity availableCopy : availableCopies) {
                BookEntity currentBook = availableCopy.getBook();
                mapBookAvailableCopies.putIfAbsent(currentBook, 0);
                mapBookAvailableCopies.compute(currentBook, (k, v) -> v++);
            }
        }

        return this.mapBookEntityToBookSearchResultDTO(searchResult);
    }

    private Set<BookEntity> authorsDirectSearch(String authorSearchString) {
        String[] searchTokens = authorSearchString.split("\\s+");

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

    private Set<BookEntity> genresDirectSearch(String genreSearchString) {
        Set<GenreEntity> genresResult = this.genreRepository.findAllByNameContaining(genreSearchString);
        return new HashSet<>(
                this.bookRepository.findAllByGenresIn(genresResult)
        );
    }

    private List<BookSearchResultDTO> mapBookEntityToBookSearchResultDTO(Set<BookEntity> books) {
        List<BookSearchResultDTO> resultDTOs = new ArrayList<>();

        for (BookEntity book : books) {
            resultDTOs.add(new BookSearchResultDTO()
                    .setId(book.getId())
                    .setTitle(book.getTitle())
                    .setAuthorsFullNames(
                            this.getAuthorsFullNames(book)
                    )
                    .setGenres(
                            this.getBookGenres(book)
                    )
            );
        }

        return resultDTOs;
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
