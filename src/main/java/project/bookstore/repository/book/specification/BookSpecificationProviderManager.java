package project.bookstore.repository.book.specification;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.bookstore.model.Book;
import project.bookstore.repository.filter.SpecificationProvider;
import project.bookstore.repository.filter.SpecificationProviderManager;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> specificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(()
                        -> new RuntimeException("No specification provider found for key: " + key));
    }
}
