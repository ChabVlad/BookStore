package project.bookstore.repository.filter;

import org.springframework.data.jpa.domain.Specification;
import project.bookstore.dto.BookSearchParameters;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParameters searchParameters);
}
