package com.example.case_team_3.repository;

import com.example.case_team_3.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByEmployeeUsername(String username);
    boolean existsByEmployeeRole(Employee.EmployeeRole role);
    Employee findByEmployeeEmail(String email);

    List<Employee> findByEmployeeRole(Employee.EmployeeRole employeeRole);
}

