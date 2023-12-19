package bg.softuni.bookexchange.init;


import bg.softuni.bookexchange.model.entity.*;
import bg.softuni.bookexchange.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

@Component
public class DbInit implements CommandLineRunner {

//    TODO: test w/ diff init params
    private final long REQUESTS_GENERATED_COUNT = 200;
    private final long AUTO_DECLINE_WEEKS_PERIOD = 3L;
    private final long DEFAULT_BORROW_WEEKS_PERIOD = 3L;
    private final long GENERATION_WEEKS_PERIOD = -9L;


    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final CopyRepository copyRepository;
    private final RequestRepository requestRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public DbInit(
            GenreRepository genreRepository,
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            UserRepository userRepository,
            CopyRepository copyRepository,
            RequestRepository requestRepository,
            TransactionRepository transactionRepository
    ) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
        this.copyRepository = copyRepository;
        this.requestRepository = requestRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {
//        read data from file
        persistBooksAuthorsCategories();
//        persistUsers(); -> sql init
        generateCopies();
        generateRequests();
    }

//    TODO: add users with roles
//    TODO: add copies (with owners)
//    TODO: add requests
//    TODO: add open and closed transactions
//    TODO: Handle errors and exc

    private void generateRequests() {
        long usersCount = this.userRepository.count();
        long copiesCount = this.copyRepository.count();
        Random random = new Random();

        for (int i = 0; i < REQUESTS_GENERATED_COUNT; i++) {
            CopyEntity copy = this.copyRepository.findById(random.nextLong(copiesCount) + 1)
                    .orElseThrow();
            UserEntity user = this.userRepository.findById(random.nextLong(usersCount) + 1)
                    .orElseThrow();

//            no requests for copies with open transactions.
            CopyRequestEntity newRequest = new CopyRequestEntity()
                    .setCopy(copy)
                    .setBorrower(user)
                    .setDateCreated(
                            this.getRandomDateWeeksAroundDate(LocalDate.now(), GENERATION_WEEKS_PERIOD)
                    );

            int existingTxForCreateDateCount = this.transactionRepository
                    .findAllByCopyAndDateBetweenBorrowAndReturnDate(copy, newRequest.getDateCreated())
                    .size();

            if (existingTxForCreateDateCount > 0) {
                continue;
            }

//            newRequest = this.requestRepository.save(newRequest);

//            TODO:
//            request older than certain period e.g. 3 weeks should be declined automatically
//            request in 3 weeks after creation stay open or a transaction is opened or declined by owner
//            variants (independent of now, depending on point in time):
//            0. declined
//            1. auto-declined (e.g. 3 weeks after created) OR open
//            2. tx opened
//            1:1:1 ratio ?
            int randomStatus = random.nextInt(3);

            LocalDate dateCreated = newRequest.getDateCreated();
            LocalDate currentAutoDeclineCreateDate = LocalDate.now().minusWeeks(AUTO_DECLINE_WEEKS_PERIOD);

            if (dateCreated.isAfter(currentAutoDeclineCreateDate)
                    || dateCreated.isEqual(currentAutoDeclineCreateDate)) {
//                for requests able to be open
                switch (randomStatus) {
                    case 0 -> declineRequest(newRequest, LocalDate.now());
                    case 1 -> generateTx(newRequest);
//                    case 3 -> leave open
//                    case 4 -> if ex there is tx and is extended
                }

            } else if (dateCreated.isBefore(currentAutoDeclineCreateDate)) {
//                for requests that are before autodecline request creation
                switch (randomStatus) {
                    case 0 -> declineRequest(
                            newRequest, newRequest.getDateCreated().plusWeeks(AUTO_DECLINE_WEEKS_PERIOD)
                    );
                    case 1 -> generateTx(newRequest);
                    case 2 -> autoDeclineRequest(newRequest);
                }

            }

//            this.requestRepository.save(newRequest);
        }
    }


    private void generateTx(CopyRequestEntity newRequest) {
//        generate in tx init?

        CopyEntity forCopy = newRequest.getCopy();
        TransactionEntity newTx = new TransactionEntity()
                .setCopyRequest(newRequest);
//                .setCopy(forCopy); -> inserts into copies.tx_id

//        get empty tx with dates data
        setTxBorrowDateAndReturnDate(newTx, newRequest);

//        if possible create tx if tx borrow and return are not in borrow period of another tx

//        if transactions exist in the new period don't crate
        int txAroundBorrowDateCount = this.transactionRepository
                .findAllByCopyAndDateBetweenBorrowAndReturnDate(forCopy, newTx.getBorrowDate())
                .size();

        int txAroundReturnDateCount = this.transactionRepository
                .findAllByCopyAndDateBetweenBorrowAndReturnDate(forCopy, newTx.getReturnDate())
                .size();

        int txOverlappingNewPeriodCount = this.transactionRepository
                .findAllByCopyAndWhereDatesOverlap(
                        forCopy,
                        newTx.getBorrowDate(),
                        newTx.getReturnDate() == null ? LocalDate.now() : newTx.getReturnDate())
                .size();

        if (txAroundBorrowDateCount > 0 || txAroundReturnDateCount > 0 || txOverlappingNewPeriodCount > 0) {
            return;
        }
//        existing open requests, edit to resolved
        List<CopyRequestEntity> requestsCreatedInTxPeriod = this.requestRepository.findAllByCopyAndDateCreatedIsBetween(
                forCopy,
                newTx.getBorrowDate(),
                newTx.getReturnDate() == null ? LocalDate.now() : newTx.getReturnDate()
        );
//        TODO: could not execute statement [Cannot delete or update a parent row: a foreign key constraint fails (`book_exchange`.`transactions`, CONSTRAINT `FKhuqxe4fc5whd4j7hxgymgorqe` FOREIGN KEY (`request_id`) REFERENCES `requests` (`id`))] [delete from requests where id=?]
        this.requestRepository.deleteAll(requestsCreatedInTxPeriod);

        List<CopyRequestEntity> requestsResolveDateInTxPeriod =
                this.requestRepository.findAllByCopyAndDateResolvedBetween(
                        forCopy,
                        newTx.getBorrowDate(),
                        newTx.getReturnDate() == null ? LocalDate.now() : newTx.getReturnDate()
                );
        List<CopyRequestEntity> requestsCreatedBeforeTxBorrowAndUnresolved =
                this.requestRepository.findAllByCopyAndDateCreatedBeforeAndDateResolvedIsNull(
                        forCopy,
                        newTx.getBorrowDate()
                );

        ArrayList<CopyRequestEntity> trimResolveDateRequests = new ArrayList<>();
        trimResolveDateRequests.addAll(requestsResolveDateInTxPeriod);
        trimResolveDateRequests.addAll(requestsCreatedBeforeTxBorrowAndUnresolved);

        for (CopyRequestEntity trimRequest : trimResolveDateRequests) {
            trimRequest.setDateResolved(newTx.getBorrowDate());
            this.requestRepository.save(trimRequest);
        }


//        place to updated request, transaction and copy if tx is ongoing
        newRequest.setDateResolved(newTx.getBorrowDate());
        newRequest = this.requestRepository.save(newRequest);
        newTx = this.transactionRepository.save(newTx);
        newRequest.setOpenedTransaction(newTx);

        if (newTx.getReturnDate() == null) {
            forCopy.setCurrentTransaction(newTx);
            this.copyRepository.save(forCopy);
        }
    }

    private void setTxBorrowDateAndReturnDate(TransactionEntity newTx, CopyRequestEntity newRequest) {
        LocalDate requestDateCreated = newRequest.getDateCreated();

        LocalDate autoDeclineBoundDate = LocalDate.now().minusWeeks(AUTO_DECLINE_WEEKS_PERIOD);
        Random random = new Random();

        if (requestDateCreated.isAfter(autoDeclineBoundDate)
                || requestDateCreated.isEqual(autoDeclineBoundDate)) {
            newTx.setBorrowDate(
                    this.getRandomDateBetweenTwoDates(LocalDate.now(), requestDateCreated)
            );
            newTx.setDueDate(newTx.getBorrowDate().plusWeeks(DEFAULT_BORROW_WEEKS_PERIOD));
            int chanceOfQuickReturn = random.nextInt(5);

            if (chanceOfQuickReturn == 0) {
                newTx.setReturnDate(this.getRandomDateBetweenTwoDates(LocalDate.now(), newTx.getBorrowDate()));
            }
//            TODO: may do an extension although never to be overdue
        } else {
//            handling before current autodecline req create date
            newTx.setBorrowDate(
                    this.getRandomDateBetweenTwoDates(
                            requestDateCreated,
                            requestDateCreated.plusWeeks(DEFAULT_BORROW_WEEKS_PERIOD))
            );
//            max extension 3 weeks
            int extension = random.nextInt(4);
            newTx.setDueDate(
                    newTx.getBorrowDate().plusWeeks(DEFAULT_BORROW_WEEKS_PERIOD + extension)
            );
//                is overdue, return in time, returned overdue
            boolean isOverdue = random.nextBoolean();
            boolean isReturned = random.nextBoolean();
            LocalDate dueDate = newTx.getDueDate();

            if (dueDate.isAfter(LocalDate.now())) {
                int chanceOfQuickReturn = random.nextInt(3);
                if (chanceOfQuickReturn == 0) {
                    newTx.setReturnDate(this.getRandomDateBetweenTwoDates(LocalDate.now(), newTx.getBorrowDate()));
                }
            } else {
                if (isReturned) {
                    if (isOverdue) {
                        newTx.setReturnDate(this.getRandomDateBetweenTwoDates(dueDate, LocalDate.now()));
                    } else {
                        newTx.setReturnDate(this.getRandomDateBetweenTwoDates(dueDate, newTx.getBorrowDate()));
                    }
                }
            }
        }
    }

    private void trimResolveDateIfInTxPeriod(CopyRequestEntity newRequest) {

        List<TransactionEntity> txBorrowDateInRequestPeriod =
                this.transactionRepository.findByRequest_CopyAndBorrowDateBetween(
                        newRequest.getCopy(),
                        newRequest.getDateCreated(),
                        newRequest.getDateResolved() == null
                                ? LocalDate.now()
                                : newRequest.getDateResolved()
                );

        if (txBorrowDateInRequestPeriod.isEmpty()) {
            return;
        }

        LocalDate minTrimDate = LocalDate.now();

        for (TransactionEntity tx : txBorrowDateInRequestPeriod) {
            if (tx.getBorrowDate().isBefore(minTrimDate)) {
                minTrimDate = tx.getBorrowDate();
            }
        }

        newRequest.setDateResolved(minTrimDate);
    }

    private void autoDeclineRequest(CopyRequestEntity newRequest) {
        LocalDate dateCreated = newRequest.getDateCreated();

        newRequest.setDateResolved(dateCreated.plusWeeks(AUTO_DECLINE_WEEKS_PERIOD));
        trimResolveDateIfInTxPeriod(newRequest);

        this.requestRepository.save(newRequest);
    }

    private void declineRequest(CopyRequestEntity newRequest, LocalDate upperBoundDate) {
        newRequest.setDateResolved(
                getRandomDateBetweenTwoDates(newRequest.getDateCreated(), upperBoundDate)
        );

        trimResolveDateIfInTxPeriod(newRequest);
        this.requestRepository.save(newRequest);
    }

    private LocalDate getRandomDateBetweenTwoDates(LocalDate date1, LocalDate date2) {
        long min = 0;
        long max = 0;
        Random random = new Random();

        if (date1.isAfter(LocalDate.now())) date1 = LocalDate.now();
        if (date2.isAfter(LocalDate.now())) date2 = LocalDate.now();

        if (date1.isBefore(date2)) {
            min = date1.toEpochDay();
            max = date2.toEpochDay();
        } else if (date1.isAfter(date2)) {
            min = date2.toEpochDay();
            max = date1.toEpochDay();
        } else {
            return date1;
        }

        return LocalDate.ofEpochDay(random.nextLong(max - min) + 1 + min);
    }

    private LocalDate getRandomDateWeeksAroundDate(LocalDate forDate, Long weeksOffset) {
        long lowerEpochDay = 0;
        long upperEpochDay = 0;

        if (weeksOffset < 0) {
            lowerEpochDay = forDate.plusWeeks(weeksOffset).toEpochDay();
            upperEpochDay = forDate.toEpochDay();
        } else if (weeksOffset > 0) {
            upperEpochDay = forDate.plusWeeks(weeksOffset).toEpochDay();
            lowerEpochDay = forDate.toEpochDay();
        } else {
            throw new IllegalStateException("Time is measured in weeks before or after - different than 0");
        }

        Random random = new Random();
        long randomEpochDay = random.nextLong(upperEpochDay - lowerEpochDay) + 1 + lowerEpochDay;

        return LocalDate.ofEpochDay(randomEpochDay);
    }

    private void generateCopies() {
        long usersCount = this.userRepository.count();
        long booksCount = this.bookRepository.count();
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            CopyEntity newCopy = new CopyEntity()
                    .setBook(this.bookRepository
                            .findById(random.nextLong(booksCount) + 1)
                            .orElseThrow())
                    .setOwner(this.userRepository
                            .findById(random.nextLong(usersCount) + 1)
                            .orElseThrow());

            this.copyRepository.save(newCopy);
        }
    }

    private void persistUsers() {
        UserEntity admin = new UserEntity()
                .setFirstName("Admin")
                .setLastName("Adminov")
                .setUsername("admin")
                .setEmail("admin@example.com")
                .setPassword("1234");

        UserEntity user1 = new UserEntity()
                .setFirstName("User1")
                .setLastName("Userov1")
                .setUsername("user1")
                .setEmail("user1@example.com")
                .setPassword("1234");

        UserEntity user2 = new UserEntity()
                .setFirstName("User2")
                .setLastName("Userov2")
                .setUsername("user2")
                .setEmail("user2@example.com")
                .setPassword("1234");

        this.userRepository.saveAll(List.of(admin, user1, user2));
    }

    private void persistBooksAuthorsCategories() {
        Path path = Path.of("src/main/resources/genres-top-10-books-authors.txt");
        GenreEntity category = null;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine();

            while (line != null) {
                if (line.startsWith("#")) {
                    String categoryName = line.replaceAll("#\\d+\\s-\\s", "");
                    category = this.genreRepository
                            .save(new GenreEntity()
                                    .setName(categoryName));

                } else if (!line.isBlank()) {
                    line = line.trim();

                    String[] split = line.split(" - ");
                    String title = split[0];

                    Set<AuthorEntity> authors = getAuthors(split[1]);

                    BookEntity book = this.bookRepository.findBookEntityByTitle(title);
                    if (book == null) book = new BookEntity().setTitle(title);
                    if (category != null) book.getGenres().add(category);
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
