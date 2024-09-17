package project.bookstore.repository;

import org.springframework.data.jpa.domain.Specification;
import project.bookstore.dto.book.BookSearchParameters;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParameters searchParameters);
}
