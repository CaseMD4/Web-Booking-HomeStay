package com.example.case_team_3.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Cashier extends Employee {
    public Cashier() {
        this.setEmployeeRole(EmployeeRole.cashier);
    }
}