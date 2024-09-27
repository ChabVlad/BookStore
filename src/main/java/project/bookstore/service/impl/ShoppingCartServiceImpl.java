package project.bookstore.service.impl;

import io.jsonwebtoken.security.SecurityException;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.dto.cart.ShoppingCartDto;
import project.bookstore.dto.item.CreateCartItemDto;
import project.bookstore.dto.item.UpdateCartItemDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.CartItemMapper;
import project.bookstore.mapper.ShoppingCartMapper;
import project.bookstore.model.Book;
import project.bookstore.model.CartItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import project.bookstore.repository.book.BookRepository;
import project.bookstore.repository.cart.ShoppingCartRepository;
import project.bookstore.repository.item.CartItemRepository;
import project.bookstore.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found by user id: " + userId));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto addItemToShoppingCart(
            CreateCartItemDto cartItemDto,
            Long userId
    ) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found by user id: " + userId));
        Book book = bookRepository.findById(cartItemDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with Id: " + cartItemDto.getBookId()));
        shoppingCart.getCartItems()
                .stream()
                .filter(item -> item.getBook().getId().equals(cartItemDto.getBookId()))
                .findFirst()
                .ifPresentOrElse(item -> item.setQuantity(
                        item.getQuantity() + cartItemDto.getQuantity()),
                        () -> addCartItemToShoppingCart(cartItemDto, book, shoppingCart));
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto updateItemInShoppingCart(
            Long cartItemId,
            UpdateCartItemDto updateCartItemDto,
            Long userId
    ) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found with user id: " + userId));
        Optional<CartItem> cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, shoppingCart.getId());
        cartItem.orElseThrow(() -> new EntityNotFoundException("Cart item not found"))
                .setQuantity(updateCartItemDto.getQuantity());
        cartItemRepository.save(cartItem.get());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteItemFromShoppingCart(
            Long cartItemId,
            Long userId
    ) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(userId)
                .orElseThrow(() -> new SecurityException("Access denied or cart not found"));
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Cart item not found in user's shopping cart"));
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void addShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private void addCartItemToShoppingCart(
            CreateCartItemDto cartItemDto,
            Book book,
            ShoppingCart shoppingCart
    ) {
        CartItem cartItem = cartItemMapper.toModel(cartItemDto);
        cartItem.setBook(book);
        shoppingCart.addItemToCart(cartItem);
    }
}
