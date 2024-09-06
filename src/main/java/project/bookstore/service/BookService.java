package project.bookstore.service;

import java.util.List;
import project.bookstore.dto.BookDto;
import project.bookstore.dto.CreateBookRequestDto;
import project.bookstore.model.Book;

public interface BookService {
    Book save(CreateBookRequestDto requestBookDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);
}
