package project.bookstore.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.BookDto;
import project.bookstore.dto.CreateBookRequestDto;
import project.bookstore.mapper.BookMapper;
import project.bookstore.service.BookService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    @GetMapping
    public BookDto getBookById(Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public BookDto createBook(CreateBookRequestDto requestBookDto) {
        return bookMapper.toDto(bookService.save(requestBookDto));
    }
}
