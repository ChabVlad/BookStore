package project.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import project.bookstore.model.Book;

public interface BookRepository extends
        JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book>,
        PagingAndSortingRepository<Book, Long> {
}
