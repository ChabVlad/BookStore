package project.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import project.bookstore.model.Status;

@Getter
@Setter
public class PatchRequestOrderDto {
    @NotNull
    private Status status;
}
