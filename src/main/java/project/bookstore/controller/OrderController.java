package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.order.RequestOrderDto;
import project.bookstore.dto.order.ResponseOrderDto;

@Tag(name = "Order controller", description = "endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(
            summary = "Place an order",
            description = "Place an order to db")
    public ResponseOrderDto placeOrder(@Valid @RequestBody RequestOrderDto requestDto) {
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    @Operation(
            summary = "Get user's order history",
            description = "Retrieve user's order history")
    public ResponseOrderDto getUserHistory() {
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping
    @Operation(
            summary = "Update an order",
            description = "Update an order in db")
    private ResponseOrderDto updateOrder(@Valid @RequestBody RequestOrderDto requestDto) {
        return null;
    }
}
