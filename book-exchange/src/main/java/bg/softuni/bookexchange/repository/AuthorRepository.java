package bg.softuni.bookexchange.repository;

import bg.softuni.bookexchange.model.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    Optional<AuthorEntity> findByFirstNameAndMiddleNameAndLastName(
            String firstName,
            String middleName,
            String lastName
    );

    Set<AuthorEntity> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase (
            String firstNameSearch,
            String lastNameSearch
    );

    @Query(
            "SELECT a FROM AuthorEntity AS a " +
                    "WHERE LOWER(a.firstName) LIKE %:nameSearch% " +
                    "OR LOWER(a.lastName) LIKE %:nameSearch%"
    )
    Set<AuthorEntity> findAllByFirstNameOrLastName(@Param("nameSearch") String nameSearch);

}
