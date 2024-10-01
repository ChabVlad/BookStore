package project.bookstore.mapper;

import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.order.item.OrderItemDto;
import project.bookstore.model.CartItem;
import project.bookstore.model.OrderItem;

@Mapper(config = MapperConfig.class, uses = {BookMapper.class})
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "book", target = "book")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "book.price", target = "price")
    OrderItem toOrderItem(CartItem cartItem);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);

    List<OrderItemDto> toOrderItemDtoList(Set<OrderItem> orderItems);
}
