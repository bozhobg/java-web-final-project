package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.BookEntity;
import bg.softuni.bookexchange.model.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    BookEntity findBookEntityByTitle(String title);
    Set<BookEntity> findBookEntityByTitleContaining(String titleSearchString);

    Set<BookEntity> findAllByGenresIn(Set<GenreEntity> searchGenres);


}
