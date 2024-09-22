package project.bookstore.dto.book;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import project.bookstore.model.Category;

@Getter
@Setter
public class BookDto {
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Category> categories;
}
