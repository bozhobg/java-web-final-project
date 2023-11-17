package bg.softuni.bookexchange.model.entity;


import bg.softuni.bookexchange.model.entity.enums.BookConditionEnum;
import jakarta.persistence.*;

import java.time.Year;

@Entity
@Table(name = "book_copies")
public class BookCopyEntity extends BaseEntity {
    @Column(name = "year_published")
    private Year yearPublished;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
    @Column(name = "book_condition")
    @Enumerated(EnumType.STRING)
    private BookConditionEnum bookCondition;

    public BookCopyEntity(){}

    public Year getYearPublished() {
        return yearPublished;
    }

    public BookCopyEntity setYearPublished(Year yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }

    public BookEntity getBook() {
        return book;
    }

    public BookCopyEntity setBook(BookEntity book) {
        this.book = book;
        return this;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public BookCopyEntity setOwner(UserEntity owner) {
        this.owner = owner;
        return this;
    }

    public BookConditionEnum getBookCondition() {
        return bookCondition;
    }

    public BookCopyEntity setBookCondition(BookConditionEnum bookCondition) {
        this.bookCondition = bookCondition;
        return this;
    }
}
