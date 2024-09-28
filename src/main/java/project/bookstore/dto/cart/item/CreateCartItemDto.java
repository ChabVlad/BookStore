package project.bookstore.dto.cart.item;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCartItemDto {
    private Long bookId;
    @Positive
    private int quantity;
}
