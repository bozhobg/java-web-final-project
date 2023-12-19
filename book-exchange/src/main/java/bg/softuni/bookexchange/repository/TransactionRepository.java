package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.CopyEntity;
import bg.softuni.bookexchange.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(
            "SELECT tx FROM TransactionEntity tx " +
                    "JOIN CopyRequestEntity r ON r.id = tx.request.id " +
                    "JOIN CopyEntity c ON c.id = r.copy.id " +
                    "WHERE c = :copy " +
                    "AND (:date >= tx.borrowDate " +
                        "AND (tx.returnDate IS NULL OR :date <= tx.returnDate))"
    ) // return date might be null
    List<TransactionEntity> findAllByCopyAndDateBetweenBorrowAndReturnDate(CopyEntity copy, LocalDate date);

    @Query(
            "SELECT tx FROM TransactionEntity tx " +
                    "JOIN CopyRequestEntity r ON r.id = tx.request.id " +
                    "JOIN CopyEntity c ON c.id = r.copy.id " +
                    "WHERE c = :copy " +
                    "AND (:borrowDate <= tx.borrowDate " +
                    "AND (tx.returnDate IS NULL OR :returnDate >= tx.returnDate))"
    ) // return date might be null
    List<TransactionEntity> findAllByCopyAndWhereDatesOverlap(
            CopyEntity copy, LocalDate borrowDate, LocalDate returnDate);


    List<TransactionEntity> findByRequest_CopyAndBorrowDateBetween(
            CopyEntity copy, LocalDate createDate, LocalDate declineDate);
}
