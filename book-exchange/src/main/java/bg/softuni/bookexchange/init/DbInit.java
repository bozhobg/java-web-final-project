package bg.softuni.bookexchange.init;


import bg.softuni.bookexchange.model.entity.AuthorEntity;
import bg.softuni.bookexchange.model.entity.BookCategoryEntity;
import bg.softuni.bookexchange.model.entity.BookEntity;
import bg.softuni.bookexchange.repository.AuthorRepository;
import bg.softuni.bookexchange.repository.BookRepository;
import bg.softuni.bookexchange.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Component
public class DbInit implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public DbInit(
            CategoryRepository categoryRepository,
            BookRepository bookRepository,
            AuthorRepository authorRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public void run(String... args) {
//        read data from file
        persistBooksAuthorsCategories();
//        TODO: create copies and

    }

    private void persistBooksAuthorsCategories() {
        Path path = Path.of("src/main/resources/genres-top-10-books-authors.txt");
        BookCategoryEntity category = null;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine();

            while (line != null) {
                if (line.startsWith("#")) {
                    String categoryName = line.replaceAll("#\\d+\\s-\\s", "");
                    category = this.categoryRepository
                            .save(new BookCategoryEntity()
                                    .setBookCategory(categoryName));

                } else if (!line.isBlank()) {
                    line = line.trim();

                    String[] split = line.split(" - ");
                    String title = split[0];

                    Set<AuthorEntity> authors = getAuthors(split[1]);

                    BookEntity book = this.bookRepository.findByTitle(title);
                    if (book == null) book = new BookEntity().setTitle(title);
                    if (category != null) book.getCategories().add(category);
                    book.getAuthors().addAll(authors);

                    this.bookRepository.save(book);
                }

                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Set<AuthorEntity> getAuthors(String unprocessed) {
        List<String> authorFullNames = getAuthorsFullNames(unprocessed);
        Set<AuthorEntity> authors = new HashSet<>();

        for (String aFullName : authorFullNames) {
            AuthorEntity author = this.mapToAuthorFromNames(aFullName);
            authors.add(author);
        }

        return authors;
    }

    private List<String> getAuthorsFullNames(String unprocessed) {
        List<String> authorsFullNames = new ArrayList<>();
        String[] split = unprocessed.trim().split(", ");

        for (String nextSplit : split) {
            if (nextSplit.contains(" and ")) {
                authorsFullNames.addAll(List.of(nextSplit.split(" and ")));
            } else {
                authorsFullNames.add(nextSplit);
            }
        }

        return authorsFullNames;
    }

    private AuthorEntity mapToAuthorFromNames(String fullName) {
        String[] splitNames = fullName.split("\\s");
        String firstName = null;
        String middleName = null;
        String lastName = splitNames[splitNames.length - 1];


        if (splitNames.length == 2) {
            firstName = splitNames[0];
        } else if (splitNames.length > 2) {
            firstName = splitNames[0];
            middleName = String.join(" ", Arrays.copyOfRange(splitNames, 1, splitNames.length - 1));
        }

        Optional<AuthorEntity> optAuthor = this.authorRepository
                .findByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName);

        if (optAuthor.isPresent()) return optAuthor.get();

        return this.authorRepository.save(
                new AuthorEntity()
                        .setFirstName(firstName)
                        .setMiddleName(middleName)
                        .setLastName(lastName)
        );
    }
}
