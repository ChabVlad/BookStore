package project.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookSearchParameters;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.model.Category;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static final String URL = "/books";
    private static final Long VALID_BOOK_ID = 1L;
    private static MockMvc mockMvc;
    private BookDto bookDto1;
    private BookDto bookDto2;
    private BookDto bookDto3;
    private CreateBookRequestDto requestDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("db/books/add-books-to-test-db.sql")
            );
        }
        createDto();
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
        MvcResult result = mockMvc
                .perform(get(URL + "/{id}", VALID_BOOK_ID).contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(status().isCreated())
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
        String requestJson = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc
                .perform(put(URL + "/{id}", VALID_BOOK_ID)
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
    void deleteBook_ValidId_StatusIsNoContent() throws Exception {
        Long validBookId = 2L;
        mockMvc.perform(delete(URL + "/{id}", validBookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("""
            Search books by parameters
            """)
    @WithMockUser(username = "user", roles = {"USER"})
    void searchBook_ValidSearchParameters_ReturnsListOfBookDtos() throws Exception {
        BookSearchParameters searchParameters =
                new BookSearchParameters(new String[]{"Tarzan"}, null, null);
        String requestJson = objectMapper.writeValueAsString(searchParameters);
        MvcResult result = mockMvc
                .perform(get(URL + "/search")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> expected = List.of(bookDto2);
        List<BookDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<BookDto>>() {}
        );

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    private void createDto() {
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
}
