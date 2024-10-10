package project.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.book.BookSearchParameters;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.mapper.BookMapper;
import project.bookstore.model.Book;
import project.bookstore.model.Category;
import project.bookstore.repository.book.BookRepository;
import project.bookstore.repository.book.BookSpecificationBuilder;
import project.bookstore.service.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private CreateBookRequestDto requestDto;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(BigDecimal.valueOf(10.95));
        book.setIsbn("9781244567897");
        Category category = new Category();
        book.setCategories(Set.of(category));

        requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book Title");
        requestDto.setAuthor("Author");
        requestDto.setPrice(BigDecimal.valueOf(10.95));
        requestDto.setIsbn("9781244567897");

        bookDto = new BookDto();
        bookDto.setTitle("Book Title");
        bookDto.setAuthor("Author");
        bookDto.setPrice(BigDecimal.valueOf(10.95));
        bookDto.setIsbn("9781244567897");
        bookDto.setCategories(Set.of(category));
    }

    @Test
    @DisplayName("""
            Create a new book and save it to database
            """)
    void save_ValidRequestDto_ReturnsBook() {
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        Book actual = bookService.save(requestDto);
        Book expected = book;
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("""
            Find all books from database
            """)
    void findAll_BooksExist_ReturnListOfBookDto() {
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        Page<Book> mockedPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(mockedPage);

        List<BookDto> expected = List.of(bookDto);

        List<BookDto> actual = bookService.findAll(mock(Pageable.class));
        assertEquals(expected, actual);
        verify(bookMapper, times(1)).toDto(any());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            Find book from database by id
            """)
    void getBookById_BookExists_ReturnBookDto() {
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));

        BookDto expected = bookDto;
        BookDto actual = bookService.getBookById(book.getId());

        assertEquals(expected, actual);
        verify(bookMapper, times(1)).toDto(any());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            Update book and save it to database
            """)
    void updateBook_ValidRequestDto_ReturnsBookDto() {
        requestDto.setPrice(BigDecimal.valueOf(12.95));

        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));
        when(bookRepository.save(book)).thenReturn(book);

        BookDto expected = bookDto;
        expected.setPrice(requestDto.getPrice());
        BookDto actual = bookService.updateBook(requestDto, book.getId());

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("""
            Search books from database by parameters
            """)
    void search_ValidSearchParameters_ReturnsBookDto() {
        BookSearchParameters bookSearchParameters = new BookSearchParameters(
                new String[]{"Book Title"}, null, null);
        Specification<Book> bookSpecification = mock(Specification.class);
        List<Book> bookList = List.of(book);
        when(bookSpecificationBuilder.build(bookSearchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification)).thenReturn(bookList);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> actual = bookService.search(bookSearchParameters);
        List<BookDto> expected = List.of(bookDto);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookSpecificationBuilder, times(1)).build(bookSearchParameters);
        verify(bookRepository, times(1)).findAll(bookSpecification);
        verify(bookMapper, times(1)).toDto(any());
    }

    @Test
    @DisplayName("""
            Find all books by category id from database
            """)
    void getAllByCategoriesId_ValidCategoryId_ReturnsBookDto() {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds(
                "Book Title",
                "New Author",
                "9923773849217",
                BigDecimal.valueOf(5.95),
                null,
                null
        );
        Category category = new Category();
        category.setId(1L);
        when(bookRepository.findAllByCategoriesId(category.getId())).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds);
        List<BookDtoWithoutCategoryIds> actual = bookService.getAllByCategoriesId(category.getId());

        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(bookRepository, times(1)).findAllByCategoriesId(any());
    }
}
