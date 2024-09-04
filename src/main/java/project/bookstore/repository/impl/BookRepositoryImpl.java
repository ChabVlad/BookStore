package project.bookstore.repository.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import project.bookstore.model.Book;
import project.bookstore.repository.BookRepository;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public List findAll() {
        return List.of();
    }
}
