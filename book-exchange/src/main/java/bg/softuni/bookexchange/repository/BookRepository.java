package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    BookEntity findByTitle(String title);
}
