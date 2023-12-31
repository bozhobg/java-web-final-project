package bg.softuni.bookexchange.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class TransactionEntity extends BaseEntity{

    @OneToOne(optional = false)
    private CopyRequestEntity request;
//    @OneToOne(mappedBy = "currentTransaction")
//    private CopyEntity copy;
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    @Column(name = "return_date")
    private LocalDate returnDate;

    public TransactionEntity() {}

    public CopyRequestEntity getCopyRequest() {
        return request;
    }

    public TransactionEntity setCopyRequest(CopyRequestEntity request) {
        this.request = request;
        return this;
    }

//    public CopyEntity getCopy() {
//        return copy;
//    }
//
//    public TransactionEntity setCopy(CopyEntity copy) {
//        this.copy = copy;
//        return this;
//    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public TransactionEntity setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
        return this;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public TransactionEntity setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public TransactionEntity setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        return this;
    }
}
