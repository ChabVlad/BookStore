package project.bookstore.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.cartItem.CreateCartItemDto;
import project.bookstore.dto.shoppingCart.ShoppingCartDto;
import project.bookstore.service.ShoppingCartService;

@Tag(name = "Shopping cart controller", description = "endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ShoppingCartDto getShoppingCart(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return shoppingCartService.getShoppingCart(userDetails.getUsername());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ShoppingCartDto addItemToShoppingCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CreateCartItemDto cartItemDto
    ) {
        return shoppingCartService.addItemToShoppingCart(cartItemDto, userDetails.getUsername());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/items/{id}")
    public ShoppingCartDto updateItem(
            @PathVariable("id") Long cartItemId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CreateCartItemDto cartItemDto
    ) {
        return shoppingCartService.updateItemInShoppingCart(cartItemId, cartItemDto, userDetails.getUsername());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/items/{id}")
    public void deleteItem(
            @PathVariable("id") Long cartItemId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        shoppingCartService.deleteItemFromShoppingCart(cartItemId);
    }

}
