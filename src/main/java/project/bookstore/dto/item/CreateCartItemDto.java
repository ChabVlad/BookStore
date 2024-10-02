package project.bookstore.dto.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCartItemDto {
    @NotNull
    @Positive
    private Long bookId;
    @Positive
    private int quantity;
}