package com.example.case_team_3.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    @Column(unique = true)
    private String employeeUsername;
    
    private String employeePassword;
    
    @Column(unique = true)
    private String employeeEmail;
    
    private String employeeName;

    @Enumerated(EnumType.STRING)
    private EmployeeRole employeeRole;

    public enum EmployeeRole {
        cashier, cleaner, admin
    }
}
