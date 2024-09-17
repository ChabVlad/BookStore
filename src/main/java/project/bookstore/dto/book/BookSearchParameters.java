package project.bookstore.dto.book;

public record BookSearchParameters(
        String[] title,
        String[] author,
        String[] price
) {
}
