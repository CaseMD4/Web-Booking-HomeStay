package com.example.case_team_3.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Cleaner extends Employee {
    public Cleaner() {
        this.setEmployeeRole(EmployeeRole.cleaner);
    }
}
