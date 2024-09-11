package project.bookstore.repository.filter;

public interface SpecificationProviderManager<T> {

    SpecificationProvider<T> getSpecificationProvider(String key);
}
