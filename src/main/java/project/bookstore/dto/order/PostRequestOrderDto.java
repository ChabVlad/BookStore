package project.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestOrderDto {
    @NotBlank
    private String shippingAddress;
}
