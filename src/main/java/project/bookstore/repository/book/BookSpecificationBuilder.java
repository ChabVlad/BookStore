package project.bookstore.repository.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.bookstore.dto.book.BookSearchParameters;
import project.bookstore.model.Book;
import project.bookstore.repository.SpecificationBuilder;
import project.bookstore.repository.SpecificationProviderManager;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(searchParameters.title()));
        }
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.price() != null && searchParameters.price().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("price")
                    .getSpecification(searchParameters.price()));
        }
        return spec;
    }
}
