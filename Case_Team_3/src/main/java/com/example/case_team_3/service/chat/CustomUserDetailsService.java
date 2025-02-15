package com.example.case_team_3.service.chat;

import com.example.case_team_3.model.User;
import com.example.case_team_3.model.Employee;
import com.example.case_team_3.repository.UserRepository;
import com.example.case_team_3.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
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

        User user = userRepository.findByUserUsername(username);
        if (user != null) {
            return new UserIdAwareUserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                }

                @Override
                public String getPassword() {
                    return user.getUserPassword();
                }

                @Override
                public String getUsername() {
                    return user.getUserUsername();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }

                @Override
                public Integer getUserId() {
                    return user.getUserId();
                }
            };
        }

        throw new UsernameNotFoundException("Tài khoản không tồn tại");
    }
}
