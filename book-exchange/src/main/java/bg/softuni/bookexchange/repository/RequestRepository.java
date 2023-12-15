package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.CopyEntity;
import bg.softuni.bookexchange.model.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    List<RequestEntity> findAllByCopyAndDateCreatedIsBetween(CopyEntity copy, LocalDate lowerBound, LocalDate upperBound);
    List<RequestEntity> findAllByCopyAndDateResolvedBetween(CopyEntity copy, LocalDate borrowDate, LocalDate returnDate);
    List<RequestEntity> findAllByCopyAndDateCreatedBeforeAndDateResolvedIsNull(
            CopyEntity copy,
            LocalDate borrowDate
    );

}
