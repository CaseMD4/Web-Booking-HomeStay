package com.example.case_team_3.service;

import com.example.case_team_3.model.User;
import com.example.case_team_3.model.Employee;
import com.example.case_team_3.repository.UserRepository;
import com.example.case_team_3.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Kiểm tra trong bảng Employee trước
        Employee employee = employeeRepository.findByEmployeeUsername(username);
        if (employee != null) {
            return new org.springframework.security.core.userdetails.User(
                employee.getEmployeeUsername(),
                employee.getEmployeePassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + employee.getEmployeeRole().toString().toUpperCase()))
            );
        }

        // Nếu không tìm thấy trong Employee, kiểm tra trong User
        User user = userRepository.findByUserUsername(username);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                user.getUserUsername(),
                user.getUserPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        throw new UsernameNotFoundException("Tài khoản không tồn tại");
    }
}
