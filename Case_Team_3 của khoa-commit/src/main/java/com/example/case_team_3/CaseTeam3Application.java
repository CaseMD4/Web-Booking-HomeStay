package com.example.case_team_3;

import com.example.case_team_3.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CaseTeam3Application {

    public static void main(String[] args) {
        SpringApplication.run(CaseTeam3Application.class, args);
    }

    @Bean
    public CommandLineRunner sendTestEmail(EmailService emailService) {
        return args -> {
            try {
                String verificationCode = emailService.generateNumericCode(6);
                System.out.println("Mã xác nhận: " + verificationCode);

                // Gửi email
                String recipientEmail = "anehhau29817998@gmail.com"; // Thay bằng email người nhận
                String subject = "Mã xác nhận của bạn";
                String content = "Mã xác nhận của bạn là: " + verificationCode;

                emailService.sendEmail(recipientEmail, subject, content);
                System.out.println("Email đã được gửi thành công!");
            } catch (Exception e) {
                System.err.println("Lỗi khi gửi email: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
