package project.bookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.dto.order.RequestOrderDto;
import project.bookstore.dto.order.ResponseOrderDto;
import project.bookstore.dto.order.item.OrderItemDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.OrderItemMapper;
import project.bookstore.mapper.OrderMapper;
import project.bookstore.model.Order;
import project.bookstore.model.OrderItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.Status;
import project.bookstore.repository.cart.ShoppingCartRepository;
import project.bookstore.repository.order.OrderRepository;
import project.bookstore.repository.order.item.OrderItemRepository;
import project.bookstore.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public ResponseOrderDto placeOrder(RequestOrderDto requestDto, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found with user id: " + userId));
        Order order = orderMapper.toEntity(requestDto);
        order.setUser(shoppingCart.getUser());
        order.setStatus(Status.COMPLETED);
        order.setOrderItems(shoppingCart.getCartItems()
                .stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.toOrderItem(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet()));
        order.setTotal(order.getOrderItems()
                .stream()
                .map(OrderItem::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(
                Optional.ofNullable(requestDto.getShippingAddress())
                        .orElseGet(() -> Optional.ofNullable(order.getUser().getShippingAddress())
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Shipping address not found")))
        );
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Transactional
    @Override
    public List<ResponseOrderDto> getUserHistory(Pageable pageable, Long userId) {
        Page<Order> orders = orderRepository.findAllByUserId(userId, pageable);
        return orders
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public ResponseOrderDto updateOrderStatus(RequestOrderDto requestDto, Long id, Long userId) {
        Order order = findByIdAndUserId(id, userId);
        if (requestDto.getStatus() != null) {
            order.setStatus(requestDto.getStatus());
            orderRepository.save(order);
            return orderMapper.toDto(order);
        } else {
            return orderMapper.toDto(order);
        }
    }

    @Transactional
    @Override
    public List<OrderItemDto> getOrderItems(Long id, Long userId) {
        Order order = findByIdAndUserId(id, userId);
        return order.getOrderItems()
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long itemId, Long userId) {
        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdAndOrderUserId(itemId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found with item id: " + itemId));
        if (!orderItem.getOrder().getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied");
        }
        return orderItemMapper.toDto(orderItem);
    }

    private Order findByIdAndUserId(Long id, Long userId) {
        return orderRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found by id: " + id));
    }
}
