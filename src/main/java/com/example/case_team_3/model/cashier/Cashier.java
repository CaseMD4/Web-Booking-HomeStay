package com.example.case_team_3.model.cashier;

import com.example.case_team_3.model.Employee;
import jakarta.persistence.Entity;
import lombok.Data;


@Data
@Entity
public class Cashier extends Employee {
    public Cashier() {
        this.setEmployeeRole(EmployeeRole.cashier);
    }
}