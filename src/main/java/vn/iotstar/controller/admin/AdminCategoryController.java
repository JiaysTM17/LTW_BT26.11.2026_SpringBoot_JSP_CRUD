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

import jakarta.validation.Valid;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        Page<Category> categoryPage = categoryService.getCategories(page, size, keyword);

        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("totalElements", categoryPage.getTotalElements());

        return "admin/category/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("isEdit", false);
        return "admin/category/form";
    }

    @PostMapping("/save")
    public String saveCategory(
            @Valid @ModelAttribute("category") Category category,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "admin/category/form";
        }

        categoryService.save(category);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm danh mục mới thành công.");
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Category> optCategory = categoryService.findById(id);
        if (optCategory.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy danh mục yêu cầu.");
            return "redirect:/admin/categories";
        }

        model.addAttribute("category", optCategory.get());
        model.addAttribute("isEdit", true);
        return "admin/category/form";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("category") Category category,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "admin/category/form";
        }

        categoryService.update(id, category);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật danh mục thành công.");
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        boolean deleted = categoryService.delete(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Xóa danh mục thành công.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa danh mục đã chọn.");
        }
        return "redirect:/admin/categories";
    }
}
