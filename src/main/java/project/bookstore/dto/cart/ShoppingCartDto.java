package project.bookstore.dto.cart;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import project.bookstore.dto.cart.item.CartItemDto;

@Getter
@Setter
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems;
}
