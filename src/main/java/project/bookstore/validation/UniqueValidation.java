package project.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import project.bookstore.repository.BookRepository;

public class UniqueValidation implements ConstraintValidator<Unique, String> {
    @Autowired
    private BookRepository repository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !repository.existsByIsbn(value);
    }
}
