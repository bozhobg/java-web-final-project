package bg.softuni.bookexchange.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class TransactionEntity extends BaseEntity{

    @OneToOne
    private RequestEntity request;
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    @Column(name = "is_open", nullable = false)
    private Boolean isOpen;

    public TransactionEntity() {}

    public RequestEntity getRequest() {
        return request;
    }

    public TransactionEntity setRequest(RequestEntity request) {
        this.request = request;
        return this;
    }

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

    public Boolean getOpen() {
        return isOpen;
    }

    public TransactionEntity setOpen(Boolean open) {
        isOpen = open;
        return this;
    }
}
