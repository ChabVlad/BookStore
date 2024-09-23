package project.bookstore.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.book.BookSearchParameters;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.model.Book;

public interface BookService {
    Book save(CreateBookRequestDto requestBookDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto updateBook(CreateBookRequestDto requestDto, Long id);

    void deleteBookById(Long id);

    List<BookDto> search(BookSearchParameters searchParameters);

    List<BookDtoWithoutCategoryIds> getAllByCategoriesId(Long categoryId);
}
