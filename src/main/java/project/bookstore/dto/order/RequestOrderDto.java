package project.bookstore.dto.order;

import lombok.Getter;
import project.bookstore.model.Status;

@Getter
public class RequestOrderDto {
    String shippingAddress;
    Status status;
}
