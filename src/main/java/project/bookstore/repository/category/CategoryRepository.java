package project.bookstore.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    //Category updateCategoryById(Long id, Category category);
}
