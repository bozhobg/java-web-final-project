package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    Optional<AuthorEntity> findByFirstNameAndMiddleNameAndLastName(
            String firstName,
            String middleName,
            String lastName
    );


}
