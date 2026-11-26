package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.Category;

public interface CategoryService {

    Page<Category> getCategories(int pageNumber, int pageSize, String searchKeyword);

    Page<Category> findByCategorynameContaining(String keyword, Pageable page);

    Page<Category> findAll(Pageable pageable);

    List<Category> getActiveCategories();

    Optional<Category> findById(Integer categoryId);

    Category save(Category category);

    Category update(Integer categoryId, Category category);

    boolean delete(Integer categoryId);

    long count();
}
