package com.example.case_team_3.controller;

import com.example.case_team_3.model.Employee;
import com.example.case_team_3.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/register-employee")
    public String showEmployeeRegistrationForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "register-employee";
    }

    @PostMapping("/register-employee")
    public String registerEmployee(@ModelAttribute Employee employee) {
        employeeService.registerEmployee(employee);
        return "redirect:/admin/dashboard?employeeCreated=true";
    }
}
