package project.bookstore.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.order.RequestOrderDto;
import project.bookstore.dto.order.ResponseOrderDto;
import project.bookstore.dto.order.item.OrderItemDto;

public interface OrderService {
    ResponseOrderDto placeOrder(RequestOrderDto requestDto, Long userId);

    List<ResponseOrderDto> getUserHistory(Pageable pageable, Long userId);

    ResponseOrderDto updateOrderStatus(RequestOrderDto requestDto, Long id, Long userId);

    List<OrderItemDto> getOrderItems(Long orderId, Long userId);

    OrderItemDto getOrderItemById(Long orderId, Long itemId, Long userId);
}
