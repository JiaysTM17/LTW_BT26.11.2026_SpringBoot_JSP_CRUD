package vn.iotstar.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> getCategories(int pageNumber, int pageSize, String searchKeyword) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), pageSize, Sort.by(Sort.Direction.DESC, "categoryid"));
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            return categoryRepository.findByCategorynameContainingIgnoreCase(searchKeyword.trim(), pageable);
        }
        return categoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getActiveCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findById(Integer categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Category save(Category category) {
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Integer categoryId, Category category) {
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay danh muc ID: " + categoryId));
        existing.setCategoryname(category.getCategoryname());
        existing.setImages(category.getImages());
        if (category.getStatus() != null) {
            existing.setStatus(category.getStatus());
        }
        return categoryRepository.save(existing);
    }

    @Override
    public boolean delete(Integer categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return categoryRepository.count();
    }
}
