package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.BookEntity;
import bg.softuni.bookexchange.model.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

//    Catalogue Search
    Set<BookEntity> findBookEntityByTitleContainingIgnoreCase(String titleSearchString);
    Set<BookEntity> findAllByGenresIn(Set<GenreEntity> searchGenres);



//    DB init
    BookEntity findBookEntityByTitle(String title);
}
