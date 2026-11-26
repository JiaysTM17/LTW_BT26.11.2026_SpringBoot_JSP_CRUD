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

        int count = (int) categoryService.count();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("categoryname"));
        Page<Category> resultPage = null;

        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByCategorynameContaining(name, pageable);
            model.addAttribute("name", name);
        } else {
            resultPage = categoryService.findAll(pageable);
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

        model.addAttribute("categoryPage", resultPage);
        return "admin/category/list";
    }

    @GetMapping({"/add", "/create"})
    public String showCreateForm(ModelMap model) {
        model.addAttribute("category", new Category());
        model.addAttribute("isEdit", false);
        return "admin/category/form";
    }

    @PostMapping({"/save", "/saveOrUpdate"})
    public String saveCategory(
            @Valid @ModelAttribute("category") Category category,
            BindingResult bindingResult,
            ModelMap model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "admin/category/form";
        }

        categoryService.save(category);
        redirectAttributes.addFlashAttribute("message", "Thêm mới danh mục thành công.");
        return "redirect:/admin/categories/searchpaginated";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, ModelMap model, RedirectAttributes redirectAttributes) {
        Optional<Category> optCategory = categoryService.findById(id);
        if (optCategory.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy danh mục yêu cầu.");
            return "redirect:/admin/categories/searchpaginated";
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
            ModelMap model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "admin/category/form";
        }

        categoryService.update(id, category);
        redirectAttributes.addFlashAttribute("message", "Cập nhật danh mục thành công.");
        return "redirect:/admin/categories/searchpaginated";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        boolean deleted = categoryService.delete(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Không thể xóa danh mục đã chọn.");
        }
        return "redirect:/admin/categories/searchpaginated";
    }
}
