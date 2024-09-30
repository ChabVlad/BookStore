package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.order.RequestOrderDto;
import project.bookstore.dto.order.ResponseOrderDto;
import project.bookstore.dto.order.item.OrderItemDto;
import project.bookstore.model.User;
import project.bookstore.service.OrderService;

@Tag(name = "Order controller", description = "endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @Operation(
            summary = "Place an order",
            description = "Place an order to db")
    public ResponseOrderDto placeOrder(
            @Valid @RequestBody RequestOrderDto requestDto,
            Authentication authentication
    ) {
        return orderService.placeOrder(requestDto, getUserId(authentication));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(
            summary = "Get user's order history",
            description = "Retrieve user's order history")
    public List<ResponseOrderDto> getUserHistory(
            @ParameterObject Pageable pageable,
            Authentication authentication
    ) {
        return orderService.getUserHistory(pageable, getUserId(authentication));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    @Operation(
            summary = "Update an order",
            description = "Update an order in db")
    public ResponseOrderDto updateOrder(
            @Valid @RequestBody RequestOrderDto requestDto,
            @PathVariable Long id,
            Authentication authentication
    ) {
        return orderService.updateOrderStatus(requestDto, id, getUserId(authentication));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    @Operation(
            summary = "Retrieve all order items for a specific order",
            description = "Retrieve all order items for a specific order")
    public List<OrderItemDto> getOrderItems(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        return orderService.getOrderItems(orderId, getUserId(authentication));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(
            summary = "Retrieve a specific order item within an order",
            description = "Retrieve a specific order item within an order")
    public OrderItemDto getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            Authentication authentication
    ) {
        return orderService.getOrderItemById(orderId, itemId, getUserId(authentication));
    }

    private Long getUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
