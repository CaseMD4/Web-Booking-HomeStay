package com.example.case_team_3.config;

import com.example.case_team_3.model.Employee;
import com.example.case_team_3.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public void run(String... args) throws Exception {
        String adminUsername = "admin";
        String adminPassword = "123456";
        String adminEmail = "admin@gmail.com";
        if (employeeRepository.findByEmployeeUsername(adminUsername) == null) {
            Employee employee = new Employee();
            employee.setEmployeeUsername(adminUsername);
            employee.setEmployeePassword(passwordEncoder.encode(adminPassword));
            employee.setEmployeeEmail(adminEmail);
            employee.setEmployeeRole(Employee.EmployeeRole.admin);
            employee.setEmployeeName("admin");
            employeeRepository.save(employee);
            System.out.println("Employee created successfully");
        }else {
            System.out.println("Employee already exists");
        }
    }
}
