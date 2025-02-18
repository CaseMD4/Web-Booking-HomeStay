package com.example.case_team_3.service;

import com.example.case_team_3.model.User;
import com.example.case_team_3.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));
    }

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


    public void updateUser(Integer id, User userDetails) {
        User existingUser = findById(id);

        // Cập nhật thông tin
        existingUser.setUserUsername(userDetails.getUserUsername());
        existingUser.setUserEmail(userDetails.getUserEmail());

        // Nếu có mật khẩu mới
        if (userDetails.getUserPassword() != null && !userDetails.getUserPassword().isEmpty()) {
            existingUser.setUserPassword(passwordEncoder.encode(userDetails.getUserPassword()));
        }

        userRepository.save(existingUser);
    }

    public void deleteUser(Integer id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    public void createUser(User user) {
        if (userRepository.findByUserUsername(user.getUserUsername()) != null) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        if (userRepository.findByUserEmail(user.getUserEmail()) != null) {
            throw new RuntimeException("Email đã được sử dụng");
        }
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userRepository.save(user);
    }

    public User findByUserUsername(String name) {
        return userRepository.findByUserUsername(name);
    }
}
