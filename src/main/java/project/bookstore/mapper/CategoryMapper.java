package project.bookstore.mapper;

import project.bookstore.dto.category.CategoryDto;
import project.bookstore.model.Category;

public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDTO);
}
