package project.bookstore.service;

import project.bookstore.dto.cart.ShoppingCartDto;
import project.bookstore.dto.item.CreateCartItemDto;
import project.bookstore.dto.item.UpdateCartItemDto;
import project.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addItemToShoppingCart(
            CreateCartItemDto cartItemDto,
            Long userId
    );

    ShoppingCartDto updateItemInShoppingCart(
            Long cartItemId,
            UpdateCartItemDto updateCartItemDto,
            Long userId
    );

    void deleteItemFromShoppingCart(
            Long cartItemId,
            Long userId
    );

    void addShoppingCart(User user);
}
