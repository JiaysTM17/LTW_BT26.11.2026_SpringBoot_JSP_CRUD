package vn.iotstar.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
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

    @GetMapping({"", "/", "/list"})
    public String index(ModelMap model,
                        @RequestParam(name = "name", required = false) String name,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        return search(model, name, page, size);
    }

    @RequestMapping("/searchpaginated")
    public String search(ModelMap model,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {

        int count = (int) userService.count();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<User> resultPage = null;

        if (StringUtils.hasText(name)) {
            resultPage = userService.getAllUsers(currentPage - 1, pageSize, name);
            model.addAttribute("name", name);
        } else {
            resultPage = userService.findAll(pageable);
        }

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);
            if (totalPages > count) {
                if (end == totalPages) start = end - count;
                else if (start == 1) end = start + count;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("userPage", resultPage);
        return "admin/user/list";
    }

    @GetMapping({"/add", "/create"})
    public String showCreateForm(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("isEdit", false);
        return "admin/user/form";
    }

    @PostMapping({"/save", "/saveOrUpdate"})
    public String saveUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            ModelMap model,
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
        redirectAttributes.addFlashAttribute("message", "Thêm người dùng mới thành công.");
        return "redirect:/admin/users/searchpaginated";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, ModelMap model, RedirectAttributes redirectAttributes) {
        Optional<User> optUser = userService.getUserById(id);
        if (optUser.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy người dùng.");
            return "redirect:/admin/users/searchpaginated";
        }

        User user = optUser.get();
        user.setPassword("");
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
            redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin người dùng thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi cập nhật: " + e.getMessage());
        }

        return "redirect:/admin/users/searchpaginated";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(
            @PathVariable("id") Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null && currentUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("message", "Bạn không thể tự xóa tài khoản đang đăng nhập.");
            return "redirect:/admin/users/searchpaginated";
        }

        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "Xóa người dùng thành công.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy người dùng để xóa.");
        }
        return "redirect:/admin/users/searchpaginated";
    }
}
