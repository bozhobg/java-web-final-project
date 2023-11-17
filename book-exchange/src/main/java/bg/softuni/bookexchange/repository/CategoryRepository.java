package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.BookCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<BookCategoryEntity, Long> {
}
