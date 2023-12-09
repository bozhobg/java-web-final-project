package bg.softuni.bookexchange.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "requests")
public class RequestEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private UserEntity borrower;
    @ManyToOne
    @JoinColumn(name = "copy_id", nullable = false)
    private CopyEntity copy;
    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;
//    TODO: use scheduler to delete after 30 days stopping re-requests for that period
    @Column(name = "date_declined")
    private LocalDate dateDeclined;
//    used to lock creation of new transactions if borrowed
    @OneToOne
    @JoinColumn(name = "current_transaction")
    private TransactionEntity currentTransaction;


    public RequestEntity() {}

    public UserEntity getBorrower() {
        return borrower;
    }

    public RequestEntity setBorrower(UserEntity borrower) {
        this.borrower = borrower;
        return this;
    }

    public CopyEntity getCopy() {
        return copy;
    }

    public RequestEntity setCopy(CopyEntity copy) {
        this.copy = copy;
        return this;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public RequestEntity setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public LocalDate getDateDeclined() {
        return dateDeclined;
    }

    public RequestEntity setDateDeclined(LocalDate dateDeclined) {
        this.dateDeclined = dateDeclined;
        return this;
    }

    public TransactionEntity getCurrentTransaction() {
        return currentTransaction;
    }

    public RequestEntity setCurrentTransaction(TransactionEntity currentTransaction) {
        this.currentTransaction = currentTransaction;
        return this;
    }

}
