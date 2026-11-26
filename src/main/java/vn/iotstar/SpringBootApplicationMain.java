package vn.iotstar;

import java.util.Date;
import java.util.List;

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
            // Giải phóng email sinh viên nếu đang bị tài khoản test cũ chiếm giữ
            userRepository.findByEmail("24110032@student.hcmute.edu.vn").ifPresent(oldUser -> {
                if ("kiettg".equals(oldUser.getUsername())) {
                    userRepository.delete(oldUser);
                }
            });

            // Seed / Cập nhật tài khoản Admin với mật khẩu BCrypt chuẩn '123456'
            User admin = userRepository.findByUsername("admin").orElse(new User());
            admin.setUsername("admin");
            admin.setPassword(PasswordUtil.hashPassword("123456"));
            admin.setEmail("admin@iotstar.vn");
            admin.setFullname("Quản Trị Viên");
            admin.setPhone("0909123456");
            admin.setAvatar("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200");
            admin.setRoleid(2);
            admin.setStatus(1);
            if (admin.getCreateddate() == null) {
                admin.setCreateddate(new Date());
            }
            userRepository.save(admin);

            // Seed / Cập nhật tài khoản Sinh viên với mật khẩu BCrypt chuẩn '123456'
            User student = userRepository.findByUsername("kiet24110032").orElse(new User());
            student.setUsername("kiet24110032");
            student.setPassword(PasswordUtil.hashPassword("123456"));
            student.setEmail("kiet@iotstar.vn");
            student.setFullname("Trương Gia Kiệt");
            student.setPhone("0987654321");
            student.setAvatar("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200");
            student.setRoleid(1);
            student.setStatus(1);
            if (student.getCreateddate() == null) {
                student.setCreateddate(new Date());
            }
            userRepository.save(student);

            // Danh sách các môn học chuẩn ngành Công nghệ Thông tin (HCMUTE)
            String[][] itCourses = {
                {"Lập Trình Web với Java Spring Boot", "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=600"},
                {"Công Nghệ Phần Mềm & DevOps", "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=600"},
                {"Kiến Trúc Microservices & Cloud", "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=600"},
                {"Cơ Sở Dữ Liệu & Tối Ưu Hóa SQL", "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=600"},
                {"Lập Trình Thiết Bị Di Động Flutter", "https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=600"},
                {"Trí Tuệ Nhân Tạo & Học Máy (AI/ML)", "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=600"},
                {"An Toàn & Bảo Mật Hệ Thống Web", "https://images.unsplash.com/photo-1550751827-4bd374c3f58b?w=600"},
                {"Cấu Trúc Dữ Liệu & Giải Thuật", "https://images.unsplash.com/photo-1509228468518-180dd4864904?w=600"}
            };

            // Cập nhật và chuẩn hóa danh mục môn học (thay thế Iphone/tên rác bằng tên môn học, tránh lỗi FK)
            List<Category> existing = categoryRepository.findAll();
            for (int i = 0; i < existing.size(); i++) {
                Category c = existing.get(i);
                String oldName = c.getCategoryname();
                if (oldName == null || oldName.trim().isEmpty() || oldName.toLowerCase().contains("iphone") || oldName.contains("Công Nghệ Lập Trình")) {
                    int courseIndex = i % itCourses.length;
                    c.setCategoryname(itCourses[courseIndex][0]);
                    c.setImages(itCourses[courseIndex][1]);
                    c.setStatus(1);
                    categoryRepository.save(c);
                } else if (c.getImages() == null || !c.getImages().startsWith("http")) {
                    c.setImages("https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=600");
                    categoryRepository.save(c);
                }
            }

            // Bổ sung các môn học còn thiếu nếu chưa có
            for (String[] course : itCourses) {
                if (!categoryRepository.existsByCategoryname(course[0])) {
                    categoryRepository.save(new Category(null, course[0], course[1], 1));
                }
            }
        };
    }
}
