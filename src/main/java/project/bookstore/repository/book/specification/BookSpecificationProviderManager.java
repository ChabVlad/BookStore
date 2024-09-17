<<<<<<<< HEAD:src/main/java/project/bookstore/repository/book/specification/BookSpecificationProviderManager.java
package project.bookstore.repository.book.specification;
========
package project.bookstore.repository.filter.book;
>>>>>>>> main:src/main/java/project/bookstore/repository/filter/book/BookSpecificationProviderManager.java

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.bookstore.model.Book;
<<<<<<<< HEAD:src/main/java/project/bookstore/repository/book/specification/BookSpecificationProviderManager.java
import project.bookstore.repository.SpecificationProvider;
import project.bookstore.repository.SpecificationProviderManager;
========
import project.bookstore.repository.filter.SpecificationProvider;
import project.bookstore.repository.filter.SpecificationProviderManager;
>>>>>>>> main:src/main/java/project/bookstore/repository/filter/book/BookSpecificationProviderManager.java

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
