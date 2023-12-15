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
    @Column(name = "date_resolved")
    private LocalDate dateResolved;
//    used to lock creation of new transactions if borrowed
    @OneToOne
    @JoinColumn(name = "opened_tx_id")
    private TransactionEntity openedTransaction;


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

    public LocalDate getDateResolved() {
        return dateResolved;
    }

    public RequestEntity setDateResolved(LocalDate dateDeclined) {
        this.dateResolved = dateDeclined;
        return this;
    }

    public TransactionEntity getOpenedTransaction() {
        return openedTransaction;
    }

    public RequestEntity setOpenedTransaction(TransactionEntity currentTransaction) {
        this.openedTransaction = currentTransaction;
        return this;
    }



}
