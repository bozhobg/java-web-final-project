package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    Set<GenreEntity> findAllByNameContainingIgnoreCase(String nameSearch);
}
