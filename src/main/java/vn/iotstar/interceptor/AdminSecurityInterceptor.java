package vn.iotstar.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;

@Component
public class AdminSecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=login_required");
            return false;
        }

        User user = (User) session.getAttribute("currentUser");
        if (user.getRoleid() == null || user.getRoleid() != 2) {
            response.sendRedirect(request.getContextPath() + "/home?error=access_denied");
            return false;
        }

        return true;
    }
}
