package vn.iotstar;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

import vn.iotstar.config.CustomSiteMeshFilter;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.util.PasswordUtil;

@SpringBootApplication
public class SpringBootApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationMain.class, args);
    }

    @Bean
    public FilterRegistrationBean<CustomSiteMeshFilter> siteMeshFilter() {
        FilterRegistrationBean<CustomSiteMeshFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CustomSiteMeshFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, CategoryRepository categoryRepository) {
        return args -> {
            // Seed Admin User
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(PasswordUtil.hashPassword("123456"));
                admin.setEmail("admin@iotstar.vn");
                admin.setFullname("Quản Trị Viên");
                admin.setPhone("0909123456");
                admin.setAvatar("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200");
                admin.setRoleid(2);
                admin.setStatus(1);
                admin.setCreateddate(new Date());
                userRepository.save(admin);
            }

            // Seed Student User
            if (!userRepository.existsByUsername("kiet24110032")) {
                User student = new User();
                student.setUsername("kiet24110032");
                student.setPassword(PasswordUtil.hashPassword("123456"));
                student.setEmail("kiet24110032@student.hcmute.edu.vn");
                student.setFullname("Trương Gia Kiệt");
                student.setPhone("0987654321");
                student.setAvatar("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200");
                student.setRoleid(1);
                student.setStatus(1);
                student.setCreateddate(new Date());
                userRepository.save(student);
            }

            // Seed Categories if empty
            if (categoryRepository.count() == 0) {
                categoryRepository.save(new Category(null, "Lập Trình Web Spring Boot", "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=600", 1));
                categoryRepository.save(new Category(null, "Công Nghệ Phần Mềm & DevOps", "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=600", 1));
                categoryRepository.save(new Category(null, "Kiến Trúc Microservices", "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=600", 1));
                categoryRepository.save(new Category(null, "Cơ Sở Dữ Liệu & Tối Ưu Hóa", "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=600", 1));
                categoryRepository.save(new Category(null, "Trí Tuệ Nhân Tạo Ứng Dụng", "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=600", 1));
                categoryRepository.save(new Category(null, "Bảo Mật Web & Xác Thực", "https://images.unsplash.com/photo-1550751827-4bd374c3f58b?w=600", 1));
            }
        };
    }
}
