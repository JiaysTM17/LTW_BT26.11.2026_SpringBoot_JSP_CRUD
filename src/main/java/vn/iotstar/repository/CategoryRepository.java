package vn.iotstar.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByCategoryname(String categoryname);

    Page<Category> findByCategorynameContaining(String keyword, Pageable page);

    Page<Category> findByCategorynameContainingIgnoreCase(String keyword, Pageable page);
}
