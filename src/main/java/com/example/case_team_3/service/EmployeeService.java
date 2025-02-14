package com.example.case_team_3.service;

import com.example.case_team_3.model.Employee;
import com.example.case_team_3.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee findById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhân viên"));
    }

    public void createEmployee(Employee employee) {
        // Kiểm tra username và email đã tồn tại
        if (employeeRepository.findByEmployeeUsername(employee.getEmployeeUsername()) != null) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        if (employeeRepository.findByEmployeeEmail(employee.getEmployeeEmail()) != null) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        // Mã hóa mật khẩu
        employee.setEmployeePassword(passwordEncoder.encode(employee.getEmployeePassword()));
        employeeRepository.save(employee);
    }

    public void updateEmployee(Integer id, Employee employeeDetails) {
        Employee existingEmployee = findById(id);
        existingEmployee.setEmployeeName(employeeDetails.getEmployeeName());
        existingEmployee.setEmployeeEmail(employeeDetails.getEmployeeEmail());
        if (employeeDetails.getEmployeePassword() != null && !employeeDetails.getEmployeePassword().isEmpty()) {
            existingEmployee.setEmployeePassword(passwordEncoder.encode(employeeDetails.getEmployeePassword()));
        }
        employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Integer id) {
        Employee employee = findById(id);
        employeeRepository.delete(employee);
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
