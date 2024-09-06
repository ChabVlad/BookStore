package project.bookstore.repository;

import java.util.List;
import project.bookstore.dto.BookDto;
import project.bookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Book getBookById(Long id);
}
