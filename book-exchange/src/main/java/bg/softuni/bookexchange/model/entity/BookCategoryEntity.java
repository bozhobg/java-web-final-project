package bg.softuni.bookexchange.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class BookCategoryEntity extends BaseEntity {
    @Column(name = "category")
    private String bookCategory;

    public BookCategoryEntity() {}

    public String getBookCategory() {
        return bookCategory;
    }

    public BookCategoryEntity setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
        return this;
    }
}
