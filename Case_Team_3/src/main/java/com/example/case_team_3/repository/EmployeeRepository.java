package com.example.case_team_3.repository;

import com.example.case_team_3.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByEmployeeUsername(String username);
    boolean existsByEmployeeRole(Employee.EmployeeRole role);
}
