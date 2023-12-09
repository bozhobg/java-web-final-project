package bg.softuni.bookexchange.model.entity;


import bg.softuni.bookexchange.model.entity.enums.BookConditionEnum;
import jakarta.persistence.*;

import java.time.Year;

@Entity
@Table(name = "book_copies")
public class CopyEntity extends BaseEntity {
//    @Column(name = "year_published")
//    private Year yearPublished;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
    @Column(name = "book_condition")
    @Enumerated(EnumType.STRING)
    private BookConditionEnum bookCondition;

    public CopyEntity(){}

    public BookEntity getBook() {
        return book;
    }

    public CopyEntity setBook(BookEntity book) {
        this.book = book;
        return this;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public CopyEntity setOwner(UserEntity owner) {
        this.owner = owner;
        return this;
    }

    public BookConditionEnum getBookCondition() {
        return bookCondition;
    }

    public CopyEntity setBookCondition(BookConditionEnum bookCondition) {
        this.bookCondition = bookCondition;
        return this;
    }
}
