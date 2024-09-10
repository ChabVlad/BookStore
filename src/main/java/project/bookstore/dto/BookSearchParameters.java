package project.bookstore.dto;

public record BookSearchParameters(
        String[] title,
        String[] author,
        String[] price
) {
}
