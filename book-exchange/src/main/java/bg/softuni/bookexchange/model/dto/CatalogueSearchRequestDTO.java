package bg.softuni.bookexchange.model.dto;

public class CatalogueSearchRequestDTO {
    private String title;
    private String author;
    private String genre;
    private Boolean isCopyAvailable;

    public CatalogueSearchRequestDTO() {
    }

    public String getTitle() {
        return title;
    }

    public CatalogueSearchRequestDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public CatalogueSearchRequestDTO setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public CatalogueSearchRequestDTO setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public boolean getIsCopyAvailable() {
        return isCopyAvailable;
    }

    public CatalogueSearchRequestDTO setIsCopyAvailable(boolean copyAvailable) {
        isCopyAvailable = copyAvailable;
        return this;
    }
}
