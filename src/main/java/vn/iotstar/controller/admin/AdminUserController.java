package vn.iotstar.controller.admin;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        Page<User> userPage = userService.getAllUsers(page, size, keyword);

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalElements", userPage.getTotalElements());

        return "admin/user/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("isEdit", false);
        return "admin/user/form";
    }

    @PostMapping("/save")
    public String saveUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (userService.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "duplicate.username", "Tên đăng nhập đã tồn tại");
        }
        if (userService.existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "duplicate.email", "Email đã tồn tại");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "admin/user/form";
        }

        userService.createUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm người dùng mới thành công.");
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> optUser = userService.getUserById(id);
        if (optUser.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy người dùng.");
            return "redirect:/admin/users";
        }

        User user = optUser.get();
        user.setPassword(""); // Do not expose hashed password in form
        model.addAttribute("user", user);
        model.addAttribute("isEdit", true);
        return "admin/user/form";
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") Integer id,
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes) {

        try {
            userService.updateUser(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin người dùng thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi cập nhật: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(
            @PathVariable("id") Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null && currentUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không thể tự xóa tài khoản đang đăng nhập.");
            return "redirect:/admin/users";
        }

        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Xóa người dùng thành công.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy người dùng để xóa.");
        }
        return "redirect:/admin/users";
    }
}
