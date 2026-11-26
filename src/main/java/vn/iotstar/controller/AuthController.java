package vn.iotstar.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            Model model,
            HttpSession session) {

        // If already logged in, redirect to home or admin
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            if (currentUser.isAdmin()) {
                return "redirect:/admin/categories/searchpaginated";
            }
            return "redirect:/home";
        }

        if ("invalid_credentials".equals(error)) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác.");
        } else if ("login_required".equals(error)) {
            model.addAttribute("errorMessage", "Vui lòng đăng nhập để tiếp tục.");
        } else if ("account_locked".equals(error)) {
            model.addAttribute("errorMessage", "Tài khoản của bạn đang bị tạm khóa.");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "Bạn đã đăng xuất thành công.");
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Đăng ký tài khoản thành công. Mời bạn đăng nhập.");
        }

        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            redirectAttributes.addAttribute("error", "invalid_credentials");
            return "redirect:/login";
        }

        Optional<User> userOpt = userService.login(username.trim(), password.trim());
        if (userOpt.isEmpty()) {
            redirectAttributes.addAttribute("error", "invalid_credentials");
            return "redirect:/login";
        }

        User user = userOpt.get();
        if (user.getStatus() != null && user.getStatus() == 0) {
            redirectAttributes.addAttribute("error", "account_locked");
            return "redirect:/login";
        }

        session.setAttribute("currentUser", user);
        session.setAttribute("isAdmin", user.isAdmin());

        if (user.isAdmin()) {
            return "redirect:/admin/categories/searchpaginated";
        }
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            return "redirect:/home";
        }

        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Mật khẩu xác nhận không khớp.");
            return "auth/register";
        }

        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("errorMessage", "Tên đăng nhập đã tồn tại trong hệ thống.");
            return "auth/register";
        }

        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("errorMessage", "Địa chỉ email đã được sử dụng.");
            return "auth/register";
        }

        user.setRoleid(1); // Mặc định role User
        user.setStatus(1); // Kích hoạt tài khoản
        userService.register(user);

        redirectAttributes.addAttribute("registered", "true");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String processLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addAttribute("logout", "true");
        return "redirect:/login";
    }
}
