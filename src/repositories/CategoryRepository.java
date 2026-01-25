package repositories;

import domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);

    Optional<Category> findById(int id);

    List<Category> findAll();
}
