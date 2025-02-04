package com.example.case_team_3.controller;

import com.example.case_team_3.model.Employee;
import com.example.case_team_3.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminSetupController {
    
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/setup-admin")
    public String showAdminSetupForm(Model model) {
        if (employeeService.existsAdminAccount()) {
            return "redirect:/login";
        }
        model.addAttribute("employee", new Employee());
        return "admin-setup";
    }

    @PostMapping("/setup-admin")
    public String setupAdmin(@ModelAttribute Employee employee) {
        if (employeeService.existsAdminAccount()) {
            return "redirect:/login";
        }
        employee.setEmployeeRole(Employee.EmployeeRole.admin);
        employeeService.registerEmployee(employee);
        return "redirect:/login?adminCreated=true";
    }
}
