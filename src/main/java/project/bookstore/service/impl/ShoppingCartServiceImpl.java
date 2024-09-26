package project.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.dto.cartItem.CreateCartItemDto;
import project.bookstore.dto.shoppingCart.ShoppingCartDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.CartItemMapper;
import project.bookstore.mapper.ShoppingCartMapper;
import project.bookstore.model.Book;
import project.bookstore.model.CartItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import project.bookstore.repository.book.BookRepository;
import project.bookstore.repository.cartItem.CartItemRepository;
import project.bookstore.repository.shoppingCart.ShoppingCartRepository;
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
    public ShoppingCartDto getShoppingCart(String username) {
        return shoppingCartMapper
                .toDto(shoppingCartRepository
                        .findByUserEmail(username)
                        .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found with username: " + username)));
    }

    @Transactional
    @Override
    public ShoppingCartDto addItemToShoppingCart(CreateCartItemDto cartItemDto, String username) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found with username: " + username));
        Book book = bookRepository.findById(cartItemDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with Id: " + cartItemDto.getBookId()));
        if (shoppingCart.getCartItems() != null) {
            if (shoppingCart.getCartItems()
                    .stream()
                    .anyMatch(cartItem -> cartItem.getBook()
                            .getId()
                            .equals(cartItemDto.getBookId()))) {
                cartItemDto.setQuantity(cartItemDto
                        .getQuantity() + shoppingCart
                        .getCartItems()
                        .stream()
                        .filter(cartItem -> cartItem.getBook().getId().equals(cartItemDto.getBookId()))
                        .findFirst()
                        .get()
                        .getQuantity());
                //cartItemRepository.save(cartItemMapper.toModel(cartItemDto));
                shoppingCartRepository.save(shoppingCart);
                return shoppingCartMapper.toDto(shoppingCart);
            }
        }
        CartItem cartItem = cartItemMapper.toModel(cartItemDto);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto updateItemInShoppingCart(
            Long cartItemId,
            CreateCartItemDto cartItemDto,
            String userName
    ) {
        ShoppingCart shoppingCart = shoppingCartMapper.toModel(getShoppingCart(userName));
        CartItem cartItem = getCartItem(cartItemId, shoppingCart.getId(), userName);
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem.setBook(bookRepository.findById(cartItemDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with Id: " + cartItemDto.getBookId())));
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteItemFromShoppingCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void addShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private CartItem getCartItem(Long cartItemId, Long shoppingCartId, String userName) {
        return cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, shoppingCartId)
                .orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by id: " + cartItemId
                        + " for user: " + userName)
        );
    }
}
