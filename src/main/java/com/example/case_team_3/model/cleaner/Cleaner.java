package com.example.case_team_3.model.cleaner;

import com.example.case_team_3.model.Employee;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Cleaner extends Employee {
    public Cleaner() {
        this.setEmployeeRole(EmployeeRole.cleaner);
    }
}
