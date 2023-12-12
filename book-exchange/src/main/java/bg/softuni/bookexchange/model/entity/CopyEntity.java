package bg.softuni.bookexchange.model.entity;


import bg.softuni.bookexchange.model.entity.enums.BookConditionEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "copies")
public class CopyEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    private BookEntity book;
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
    @Column(name = "book_condition")
    @Enumerated(EnumType.STRING)
    private BookConditionEnum bookCondition;

    @OneToOne
    @JoinColumn(name = "current_tx_id")
    private TransactionEntity currentTransaction;

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

    public TransactionEntity getCurrentTransaction() {
        return currentTransaction;
    }

    public CopyEntity setCurrentTransaction(TransactionEntity transaction) {
        this.currentTransaction = transaction;
        return this;
    }
}
