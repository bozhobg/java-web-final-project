package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.CopyEntity;
import bg.softuni.bookexchange.model.entity.CopyRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<CopyRequestEntity, Long> {
    List<CopyRequestEntity> findAllByCopyAndDateCreatedIsBetween(CopyEntity copy, LocalDate lowerBound, LocalDate upperBound);
    List<CopyRequestEntity> findAllByCopyAndDateResolvedBetween(CopyEntity copy, LocalDate borrowDate, LocalDate returnDate);
    List<CopyRequestEntity> findAllByCopyAndDateCreatedBeforeAndDateResolvedIsNull(
            CopyEntity copy,
            LocalDate borrowDate
    );

}
