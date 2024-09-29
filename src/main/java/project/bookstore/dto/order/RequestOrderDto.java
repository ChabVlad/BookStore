package project.bookstore.dto.order;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.model.Status;

@Getter
@Setter
public class RequestOrderDto {
    private String shippingAddress;
    private Status status;
}
