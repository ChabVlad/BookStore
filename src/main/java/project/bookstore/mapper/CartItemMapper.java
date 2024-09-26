package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.item.CartItemDto;
import project.bookstore.dto.item.CreateCartItemDto;
import project.bookstore.model.CartItem;

@Mapper(config = MapperConfig.class, uses = {BookMapper.class, ShoppingCartMapper.class})
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(source = "bookId", target = "book", qualifiedByName = "bookFromId")
    CartItem toModel(CreateCartItemDto cartItemDto);
}
