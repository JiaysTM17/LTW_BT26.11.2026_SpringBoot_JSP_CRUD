package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.CategoryService;

@Controller
public class HomeController {

    private final CategoryService categoryService;

    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping({"/", "/home"})
    public String index(
            @RequestParam(value = "error", required = false) String error,
            Model model,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("categories", categoryService.getActiveCategories());

        if ("access_denied".equals(error)) {
            model.addAttribute("errorMessage", "Bạn không có quyền truy cập khu vực Quản trị viên (Admin).");
        }

        return "home/index";
    }
}
