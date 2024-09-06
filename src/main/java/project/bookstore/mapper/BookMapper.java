package project.bookstore.mapper;

import org.mapstruct.Mapper;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.BookDto;
import project.bookstore.dto.CreateBookRequestDto;
import project.bookstore.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    public BookDto toDto(Book book);

    public Book toModel(CreateBookRequestDto requestDto);
}
