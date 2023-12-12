package bg.softuni.bookexchange.model.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class BookEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<AuthorEntity> authors;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<GenreEntity> genres;

//    @OneToMany(mappedBy = "book")
//    private Set<CopyEntity> copies;

    public BookEntity() {
        this.authors = new HashSet<>();
        this.genres = new HashSet<>();
//        this.copies = new HashSet<>();
    }

    public String getTitle() {
        return title;
    }

    public BookEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public Set<AuthorEntity> getAuthors() {
        return authors;
    }

    public BookEntity setAuthors(Set<AuthorEntity> authors) {
        this.authors = authors;
        return this;
    }

    public Set<GenreEntity> getGenres() {
        return genres;
    }

    public BookEntity setGenres(Set<GenreEntity> genres) {
        this.genres = genres;
        return this;
    }

//    public Set<CopyEntity> getCopies() {
//        return copies;
//    }
//
//    public BookEntity setCopies(Set<CopyEntity> copies) {
//        this.copies = copies;
//        return this;
//    }
}
