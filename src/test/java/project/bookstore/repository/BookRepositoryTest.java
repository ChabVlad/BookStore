package project.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import project.bookstore.model.Book;
import project.bookstore.repository.book.BookRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    private static final Long BOOKS_CATEGORY_ID = 1L;
    private static final Long BOOKS_CATEGORY_NOT_EXISTED_ID = 10L;
    private static final int BOOKS_FROM_CATEGORY_ONE = 2;
    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("db/books/add-books-to-test-db.sql")
            );
        }
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
            Find all books by category id from database
            """)
    void findAllByCategoriesId_CategoryIdExists_ReturnsTwoBooks() {
        List<Book> books = bookRepository.findAllByCategoriesId(BOOKS_CATEGORY_ID);
        int actual = books.size();
        int expected = BOOKS_FROM_CATEGORY_ONE;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("""
            Find all books by not existed category id from database
            """)
    void findAllByCategoriesId_CategoryIdNotExists_ReturnsEmptyList() {
        List<Book> books = bookRepository.findAllByCategoriesId(BOOKS_CATEGORY_NOT_EXISTED_ID);
        assertTrue(books.isEmpty());
    }
}
