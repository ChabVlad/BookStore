package project.bookstore.repository.order.item;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrderIdAndOrderUserId(Long itemId, Long orderId, Long userId);
}
