package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.CopyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CopyRepository extends JpaRepository<CopyEntity, Long> {
}
