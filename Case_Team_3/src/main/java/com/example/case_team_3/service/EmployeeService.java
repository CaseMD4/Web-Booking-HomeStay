package com.example.case_team_3.service;

import com.example.case_team_3.model.Employee;
import com.example.case_team_3.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsAdminAccount() {
        return employeeRepository.existsByEmployeeRole(Employee.EmployeeRole.admin);
    }

    public Employee registerEmployee(Employee employee) {
        if (employeeRepository.findByEmployeeUsername(employee.getEmployeeUsername()) != null) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        employee.setEmployeePassword(passwordEncoder.encode(employee.getEmployeePassword()));
        return employeeRepository.save(employee);
    }

    public Employee findByUsername(String username) {
        return employeeRepository.findByEmployeeUsername(username);
    }
}
