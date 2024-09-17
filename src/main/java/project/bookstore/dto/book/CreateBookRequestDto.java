package project.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @Pattern(regexp = "//d{13}", message = "Should be a valid ISBN number: 13 numbers only!")
    private String isbn;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    private String description;
    private String coverImage;
}
