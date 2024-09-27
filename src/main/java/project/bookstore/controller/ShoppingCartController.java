package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.cart.ShoppingCartDto;
import project.bookstore.dto.item.CreateCartItemDto;
import project.bookstore.dto.item.UpdateCartItemDto;
import project.bookstore.security.CustomUserDetails;
import project.bookstore.service.ShoppingCartService;

@Tag(name = "Shopping cart controller", description = "endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(
            summary = "Get shopping cart",
            description = "Get user's shopping cart")
    public ShoppingCartDto getShoppingCart(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return shoppingCartService.getShoppingCart(userDetails.getId());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @Operation(
            summary = "Add item to shopping cart",
            description = "Add item to user's shopping cart")
    public ShoppingCartDto addItemToShoppingCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CreateCartItemDto cartItemDto
    ) {
        return shoppingCartService.addItemToShoppingCart(cartItemDto, userDetails.getId());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/items/{id}")
    @Operation(
            summary = "Update cart item in shopping cart",
            description = "Update cart in user's shopping cart")
    public ShoppingCartDto updateItem(
            @PathVariable("id") Long cartItemId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UpdateCartItemDto updateCartItemDto
    ) {
        return shoppingCartService.updateItemInShoppingCart(
                cartItemId,
                updateCartItemDto,
                userDetails.getId()
        );
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/items/{id}")
    @Operation(
            summary = "Delete item from shopping cart",
            description = "Delete item from user's shopping cart")
    public void deleteItem(
            @PathVariable("id") Long cartItemId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        shoppingCartService.deleteItemFromShoppingCart(cartItemId, userDetails.getId());
    }
}
