package project.bookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import project.bookstore.dto.order.item.OrderItemDto;
import project.bookstore.model.Status;

public class ResponseOrderDto {
    Long id;
    Long userId;
    Set<OrderItemDto> orderItems;
    LocalDateTime orderDate;
    BigDecimal total;
    Status status;
}
