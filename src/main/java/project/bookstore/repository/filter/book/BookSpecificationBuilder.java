package project.bookstore.repository.filter.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.bookstore.dto.BookSearchParameters;
import project.bookstore.model.Book;
import project.bookstore.repository.filter.SpecificationBuilder;
import project.bookstore.repository.filter.SpecificationProviderManager;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String PRICE = "price";
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(TITLE)
                    .getSpecification(searchParameters.title()));
        }
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR)
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.price() != null && searchParameters.price().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(PRICE)
                    .getSpecification(searchParameters.price()));
        }
        return spec;
    }
}
