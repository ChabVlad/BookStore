package project.bookstore.service;

import jakarta.validation.Valid;
import project.bookstore.dto.cartItem.CreateCartItemDto;
import project.bookstore.dto.shoppingCart.ShoppingCartDto;
import project.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addItemToShoppingCart(@Valid CreateCartItemDto cartItemDto, String username);

    ShoppingCartDto updateItemInShoppingCart(Long cartItemId, @Valid CreateCartItemDto cartItemDto, String username);

    void deleteItemFromShoppingCart(Long cartItemId);

    void addShoppingCart(User user);
}
