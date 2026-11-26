package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import vn.iotstar.entity.Category;

public interface CategoryService {

    Page<Category> getCategories(int pageNumber, int pageSize, String searchKeyword);

    List<Category> getActiveCategories();

    Optional<Category> findById(Integer categoryId);

    Category save(Category category);

    Category update(Integer categoryId, Category category);

    boolean delete(Integer categoryId);

    long count();
}
