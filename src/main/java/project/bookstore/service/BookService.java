package project.bookstore.service;

import java.util.List;
import project.bookstore.dto.BookDto;
import project.bookstore.dto.BookSearchParameters;
import project.bookstore.dto.CreateBookRequestDto;
import project.bookstore.model.Book;

public interface BookService {
    Book save(CreateBookRequestDto requestBookDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    BookDto updateBook(CreateBookRequestDto requestDto, Long id);

    void deleteBookById(Long id);

    List<BookDto> search(BookSearchParameters searchParameters);
}
