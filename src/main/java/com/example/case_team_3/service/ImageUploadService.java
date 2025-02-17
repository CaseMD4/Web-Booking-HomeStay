package com.example.case_team_3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageUploadService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public String uploadImages(MultipartFile[] files) {
        List<String> filePaths = new ArrayList<>();

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath); // Tạo thư mục nếu chưa có
            }

            for (MultipartFile file : files) {
                Path filePath = uploadPath.resolve(file.getOriginalFilename());

                // Ghi đè file nếu đã tồn tại
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Lưu đường dẫn tương đối để hiển thị ảnh
                filePaths.add("/uploads/" + file.getOriginalFilename());
            }



        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading files";
        }
        return String.join(", ", filePaths);
    }


}

