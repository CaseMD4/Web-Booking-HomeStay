package com.example.case_team_3.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {

    @GetMapping("/cashierhome")
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    public String cashierHome() {
        return "cashierhome";
    }

    @GetMapping("/cleanerhome")
    @PreAuthorize("hasRole('ROLE_CLEANER')")
    public String cleanerHome() {
        return "cleanerhome";
    }
}
