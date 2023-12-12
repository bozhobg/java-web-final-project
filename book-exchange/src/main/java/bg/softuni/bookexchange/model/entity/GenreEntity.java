package bg.softuni.bookexchange.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "genres")
public class GenreEntity extends BaseEntity {
    @Column
    private String name;

    public GenreEntity() {}

    public String getName() {
        return name;
    }

    public GenreEntity setName(String bookCategory) {
        this.name = bookCategory;
        return this;
    }
}
