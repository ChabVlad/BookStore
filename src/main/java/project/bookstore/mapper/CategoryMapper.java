package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CategoryRequestDto;
import project.bookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CategoryRequestDto requestDto);

    void updateCategoryFromDto(CategoryRequestDto requestDto,@MappingTarget Category category);
}
