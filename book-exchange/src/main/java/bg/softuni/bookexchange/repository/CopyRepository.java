package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.BookEntity;
import bg.softuni.bookexchange.model.entity.CopyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CopyRepository extends JpaRepository<CopyEntity, Long> {

    Set<CopyEntity> findAllByCurrentTransactionNotNull();

}
