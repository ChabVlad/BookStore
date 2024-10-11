package project.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
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
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CategoryRequestDto;
import project.bookstore.mapper.CategoryMapper;
import project.bookstore.model.Category;
import project.bookstore.repository.category.CategoryRepository;
import project.bookstore.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequestDto requestDto;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Test Category");

        requestDto = new CategoryRequestDto("Test Category", null);

        categoryDto = new CategoryDto();
        categoryDto.setName("Test Category");
    }

    @Test
    @DisplayName("""
            Get all categories from database
            """)
    void findAll_CategoriesExist_ReturnListOfCategoryDto() {
        Page<Category> mockedPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(mockedPage);
        when(categoryMapper.toDto(any())).thenReturn(categoryDto);

        List<CategoryDto> expected = List.of(categoryDto);
        List<CategoryDto> actual = categoryService.findAll(mock(Pageable.class));

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoryMapper, times(1)).toDto(any());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
            Get category by id from database
            """)
    void getById_ValidCategoryId_ReturnCategoryDto() {
        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.ofNullable(category));
        when(categoryMapper.toDto(any())).thenReturn(categoryDto);

        CategoryDto expected = categoryDto;
        CategoryDto actual = categoryService.getById(category.getId());

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoryMapper, times(1)).toDto(any());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
            Create category and add to database
            """)
    void save_ValidRequestDto_ReturnCategoryDto() {
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryMapper.toDto(any())).thenReturn(categoryDto);

        CategoryDto expected = categoryDto;
        CategoryDto actual = categoryService.save(requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoryMapper, times(1)).toDto(any());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
            Get all categories from database
            """)
    void update_ValidRequestDto_ReturnCategoryDto() {
        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.ofNullable(category));
        doAnswer(invocation -> {
            Category argCategory = invocation.getArgument(1);
            argCategory.setName("Updated Category");
            return null;
        })
                .when(categoryMapper)
                .updateCategoryFromDto(any(CategoryRequestDto.class), any(Category.class));
        when(categoryMapper.toDto(any())).thenReturn(categoryDto);

        CategoryDto expected = categoryDto;
        expected.setName("Updated Category");
        CategoryDto actual = categoryService.update(category.getId(), requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoryMapper, times(1)).updateCategoryFromDto(any(), any());
        verify(categoryMapper, times(1)).toDto(any());
        verify(categoryRepository, times(1)).findById(any());
    }
}
