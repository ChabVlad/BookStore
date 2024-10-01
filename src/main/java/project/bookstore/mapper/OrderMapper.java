package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.order.PostRequestOrderDto;
import project.bookstore.dto.order.ResponseOrderDto;
import project.bookstore.model.Order;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "orderItems", source = "orderItems")
    ResponseOrderDto toDto(Order order);

    Order toEntity(PostRequestOrderDto requestDto);
}
