package project.bookstore.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import project.bookstore.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends
        JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book>,
        PagingAndSortingRepository<Book, Long> {
    List<Book> findAllByCategoryId(Long categoryId);

}
