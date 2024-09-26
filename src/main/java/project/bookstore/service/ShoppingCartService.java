package project.bookstore.service;

import jakarta.validation.Valid;
import project.bookstore.dto.cart.ShoppingCartDto;
import project.bookstore.dto.item.CreateCartItemDto;
import project.bookstore.dto.item.UpdateCartItemDto;
import project.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addItemToShoppingCart(
            @Valid CreateCartItemDto cartItemDto,
            String username
    );

    ShoppingCartDto updateItemInShoppingCart(
            Long cartItemId,
            @Valid UpdateCartItemDto updateCartItemDto,
            String username
    );

    void deleteItemFromShoppingCart(Long cartItemId);

    void addShoppingCart(User user);
}
