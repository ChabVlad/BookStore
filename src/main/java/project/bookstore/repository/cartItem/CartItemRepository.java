package project.bookstore.repository.cartItem;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);
}
