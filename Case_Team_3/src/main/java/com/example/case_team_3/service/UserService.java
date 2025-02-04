package com.example.case_team_3.service;

import com.example.case_team_3.model.User;
import com.example.case_team_3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.findByUserUsername(user.getUserUsername()) != null) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByUserEmail(user.getUserEmail()) != null) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        // Mã hóa mật khẩu trước khi lưu
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        User user = userRepository.findByUserUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getUserPassword())) {
            return user;
        }
        throw new RuntimeException("Username hoặc mật khẩu không đúng!");
    }
}
