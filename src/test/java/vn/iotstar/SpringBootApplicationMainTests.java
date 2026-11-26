package vn.iotstar;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.UserService;
import vn.iotstar.util.PasswordUtil;

@SpringBootTest
class SpringBootApplicationMainTests {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanStrayColumns() {
        try {
            jdbcTemplate.execute("ALTER TABLE categories DROP COLUMN category_name");
        } catch (Exception ignored) {
        }
        try {
            jdbcTemplate.execute("ALTER TABLE categories DROP COLUMN category_id");
        } catch (Exception ignored) {
        }
    }

    @Test
    void testPasswordUtil() {
        String raw = "secretPassword123";
        String hashed = PasswordUtil.hashPassword(raw);
        Assertions.assertNotNull(hashed);
        Assertions.assertTrue(PasswordUtil.checkPassword(raw, hashed));
        Assertions.assertFalse(PasswordUtil.checkPassword("wrongPassword", hashed));
    }

    @Test
    void testUserLoginAndRegistration() {
        String testUser = "testuser_" + System.currentTimeMillis();
        User user = new User();
        user.setUsername(testUser);
        user.setPassword("password123");
        user.setEmail(testUser + "@test.com");
        user.setFullname("Test User");
        user.setRoleid(1);
        user.setStatus(1);

        User saved = userService.register(user);
        Assertions.assertNotNull(saved.getId());

        // Test Login
        Optional<User> loggedIn = userService.login(testUser, "password123");
        Assertions.assertTrue(loggedIn.isPresent());
        Assertions.assertEquals(testUser, loggedIn.get().getUsername());

        // Test Pagination & Search
        Page<User> searchResult = userService.getAllUsers(0, 5, testUser);
        Assertions.assertTrue(searchResult.getTotalElements() >= 1);
    }

    @Test
    void testCategoryCrudAndSearch() {
        String catName = "Test Cat " + System.currentTimeMillis();
        Category cat = new Category(null, catName, "https://example.com/cat.jpg", 1);
        Category saved = categoryService.save(cat);
        Assertions.assertNotNull(saved.getCategoryid());

        // Test Search & Pagination
        Page<Category> page = categoryService.getCategories(0, 5, "Test Cat");
        Assertions.assertTrue(page.getTotalElements() >= 1);

        // Test Update
        saved.setCategoryname(catName + " Updated");
        Category updated = categoryService.update(saved.getCategoryid(), saved);
        Assertions.assertEquals(catName + " Updated", updated.getCategoryname());

        // Test Delete
        boolean deleted = categoryService.delete(saved.getCategoryid());
        Assertions.assertTrue(deleted);
    }
}
