package project.bookstore.service;

import java.util.List;
import org.springframework.stereotype.Service;
import project.bookstore.model.Book;

@Service
public interface BookService {
    Book save(Book book);

    List findAll();
}
