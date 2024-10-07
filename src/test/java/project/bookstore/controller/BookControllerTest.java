package project.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.model.Book;
import project.bookstore.model.Category;
import project.bookstore.repository.book.BookRepository;
import project.bookstore.service.BookService;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static final String URL = "/books";
    private static MockMvc mockMvc;
    private BookDto bookDto1;
    private BookDto bookDto2;
    private BookDto bookDto3;
    private CreateBookRequestDto requestDto;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
            ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("db/books/add-books-to-test-db.sql")
            );
        }
    }

    @BeforeEach
    void setUp() {
        bookDto1 = new BookDto();
        bookDto1.setTitle("1984");
        bookDto1.setAuthor("G. Orwell");
        bookDto1.setIsbn("9781234567897");
        bookDto1.setPrice(BigDecimal.valueOf(5.95));
        bookDto1.setCategories(Set.of(new Category()));

        bookDto2 = new BookDto();
        bookDto2.setTitle("Tarzan");
        bookDto2.setAuthor("Tarzan");
        bookDto2.setIsbn("9789876637889");
        bookDto2.setPrice(BigDecimal.valueOf(7.95));
        bookDto2.setCategories(Set.of(new Category()));

        bookDto3 = new BookDto();
        bookDto3.setTitle("Lisova Mavka");
        bookDto3.setAuthor("L. Ukrainka");
        bookDto3.setIsbn("9781122334111");
        bookDto3.setPrice(BigDecimal.valueOf(12.95));
        bookDto3.setCategories(Set.of(new Category()));

        requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Robinzon");
        requestDto.setAuthor("Cruzo");
        requestDto.setIsbn("9789877777889");
        requestDto.setPrice(BigDecimal.valueOf(14.95));
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("db/books/clear-books-table-from-test-db.sql")
            );
        }
    }

    @Test
    @DisplayName("""
            Get all books from database
            """)
    @WithMockUser(username = "user", roles = {"USER"})
    void getAll_ThreeBooksInDb_ReturnsListOfThreeBookDto() throws Exception {
        MvcResult result = mockMvc
                .perform(get(URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> expected = List.of(bookDto1, bookDto2, bookDto3);
        List<BookDto> actual = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<List<BookDto>>() {});

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get book by id from database
            """)
    @WithMockUser(username = "user", roles = {"USER"})
    void getBookById_ValidBookId_ReturnsBookDto() throws Exception {
        Long validBookId = 1L;
        MvcResult result = mockMvc
                .perform(get(URL + "/{id}", validBookId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto expected = bookDto1;
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Create new book and save to database
            """)
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void createBook_ValidRequestDto_ReturnsBookDto() throws Exception {
        String requestJson = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc
                .perform(post(URL)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto expected = new BookDto();
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setIsbn(requestDto.getIsbn());
        expected.setPrice(requestDto.getPrice());
        expected.setCategories(Set.of(new Category()));

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Update book from database
            """)
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void updateBook_ValidRequestDto_ReturnsBookDto() throws Exception {
        Long validBookId = 1L;
        String requestJson = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc
                .perform(put(URL + "/{id}", validBookId)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto expected = new BookDto();
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setIsbn(requestDto.getIsbn());
        expected.setPrice(requestDto.getPrice());
        expected.setCategories(Set.of(new Category()));

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Delete book from database
            """)
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Transactional
    void deleteBook_ValidId_BookIsDeletedTrue() throws Exception {
        Long validBookId = 2L;
        mockMvc.perform(delete(URL + "/{id}", validBookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Book deletedBook = entityManager.createQuery(
                        "SELECT b FROM Book b WHERE b.id = :id AND b.isDeleted = true", Book.class)
                .setParameter("id", validBookId)
                .getSingleResult();

        assertTrue(deletedBook.isDeleted());
    }
}
