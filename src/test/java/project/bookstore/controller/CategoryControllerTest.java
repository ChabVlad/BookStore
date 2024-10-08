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
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CategoryRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    private static final String URL = "/categories";
    private static final Long VALID_CATEGORY_ID = 1L;
    private static final Long DELETE_CATEGORY_ID = 3L;
    private static MockMvc mockMvc;
    private CategoryDto categoryDto1;
    private CategoryDto categoryDto2;
    private CategoryDto categoryDto3;
    private CategoryRequestDto requestDto;
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
        categoryDto1 = new CategoryDto();
        categoryDto1.setId(1L);
        categoryDto1.setName("Horror");

        categoryDto2 = new CategoryDto();
        categoryDto2.setId(2L);
        categoryDto2.setName("Adventures");

        categoryDto3 = new CategoryDto();
        categoryDto3.setId(3L);
        categoryDto3.setName("Fantasy");

        requestDto = new CategoryRequestDto("Fantasy", null);
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
            Create category
            """)
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void createCategory_ValidRequestDto_ReturnCategoryDto() throws Exception {
        String requestJson = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc
                .perform(post(URL)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto expected = new CategoryDto();
        expected.setId(categoryDto3.getId());
        expected.setName(categoryDto3.getName());

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get all categories from database
            """)
    @WithMockUser(username = "user", roles = {"USER"})
    void getAll_TwoCategoriesInDb_ReturnListOfTwoCategoryDto() throws Exception {
        MvcResult result = mockMvc
                .perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> expected = List.of(categoryDto1, categoryDto2);
        List<CategoryDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<CategoryDto>>() {}
        );

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get category by id from database
            """)
    @WithMockUser(username = "user", roles = {"USER"})
    void getCategoryById_ValidCategoryId_ReturnCategoryDto() throws Exception {
        MvcResult result = mockMvc
                .perform(get(URL + "/{id}", VALID_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto expected = categoryDto3;

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Update category by id
            """)
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void updateCategory_ValidRequestDto_ReturnCategoryDto() throws Exception {
        String requestJson = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc
                .perform(put(URL + "/{id}", VALID_CATEGORY_ID)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto expected = new CategoryDto();
        expected.setName(categoryDto2.getName());

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Delete category by id from database
            """)
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void deleteCategory_ValidCategoryID_StatusIsNoContent() throws Exception {
        mockMvc.perform(delete(URL + "/{id}", DELETE_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("""
            Get books by category id from database
            """)
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void getBooksByCategoryId_ValidRequestDto_ReturnCategoryDto() throws Exception {
        MvcResult result = mockMvc
                .perform(get(URL + "/{id}/books", VALID_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds book1 = new BookDtoWithoutCategoryIds(
                "1984",
                "G. Orwell",
                "9781234567897",
                BigDecimal.valueOf(5.95),
                null,
                null
        );
        BookDtoWithoutCategoryIds book2 = new BookDtoWithoutCategoryIds(
                "Tarzan",
                "Tarzan",
                "9789876637889",
                BigDecimal.valueOf(7.95),
                null,
                null
        );

        List<BookDtoWithoutCategoryIds> expected = List.of(book1, book2);

        List<BookDtoWithoutCategoryIds> actual = objectMapper
                .readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<List<BookDtoWithoutCategoryIds>>() {}
                );

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }
}
