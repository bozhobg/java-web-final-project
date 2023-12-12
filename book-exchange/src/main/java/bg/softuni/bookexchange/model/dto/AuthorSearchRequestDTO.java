package bg.softuni.bookexchange.model.dto;

public class AuthorSearchRequestDTO {
    private String firstName;
    private String middleName;
    private String lastName;

    public AuthorSearchRequestDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public AuthorSearchRequestDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public AuthorSearchRequestDTO setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AuthorSearchRequestDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}
