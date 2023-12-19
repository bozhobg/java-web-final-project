package bg.softuni.bookexchange.model.dto;

import java.util.ArrayList;
import java.util.List;

public class BookSearchResultDTO {
    private Long bookId;
    private String title;
    private List<String> authorsFullNames;
    private List<String> genres;
    private Integer totalCopiesCount;
    private Integer availableCopiesCount;

    public BookSearchResultDTO(){
        this.authorsFullNames = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public Long getBookId() {
        return bookId;
    }

    public BookSearchResultDTO setBookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BookSearchResultDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getAuthorsFullNames() {
        return authorsFullNames;
    }

    public BookSearchResultDTO setAuthorsFullNames(List<String> authorsFullNames) {
        this.authorsFullNames = authorsFullNames;
        return this;
    }

    public List<String> getGenres() {
        return genres;
    }

    public BookSearchResultDTO setGenres(List<String> genres) {
        this.genres = genres;
        return this;
    }

    public Integer getTotalCopiesCount() {
        return totalCopiesCount;
    }

    public BookSearchResultDTO setTotalCopiesCount(Integer totalCopiesCount) {
        this.totalCopiesCount = totalCopiesCount;
        return this;
    }

    public Integer getAvailableCopiesCount() {
        return availableCopiesCount;
    }

    public BookSearchResultDTO setAvailableCopiesCount(Integer availableCopiesCount) {
        this.availableCopiesCount = availableCopiesCount;
        return this;
    }
}
