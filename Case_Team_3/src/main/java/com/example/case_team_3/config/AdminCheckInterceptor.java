package com.example.case_team_3.config;

import com.example.case_team_3.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

    private final EmployeeService employeeService;

    public AdminCheckInterceptor(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        
        // Bỏ qua các đường dẫn static và setup-admin
        if (path.startsWith("/css") || path.startsWith("/js") || 
            path.equals("/setup-admin") || path.equals("/error") ||
            path.equals("/login")) {
            return true;
        }

        // Nếu chưa có admin và không phải đang ở trang setup-admin thì chuyển hướng
        if (!employeeService.existsAdminAccount()) {
            response.sendRedirect("/setup-admin");
            return false;
        }

        return true;
    }
}
