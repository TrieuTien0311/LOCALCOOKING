package com.android.be.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private Path uploadPath;

    @PostConstruct
    public void init() {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục upload", e);
        }
    }

    /**
     * Lưu file vào thư mục con
     * @param file File cần lưu
     * @param subFolder Thư mục con (ví dụ: "reviews")
     * @return Tên file đã lưu
     */
    public String storeFile(MultipartFile file, String subFolder) {
        // Validate file
        if (file.isEmpty()) {
            throw new RuntimeException("File rỗng");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Kiểm tra tên file hợp lệ
        if (originalFileName.contains("..")) {
            throw new RuntimeException("Tên file không hợp lệ: " + originalFileName);
        }

        // Tạo tên file unique
        String fileExtension = getFileExtension(originalFileName);
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            // Tạo thư mục con nếu chưa có
            Path targetDir = this.uploadPath.resolve(subFolder);
            Files.createDirectories(targetDir);

            // Copy file
            Path targetPath = targetDir.resolve(newFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file: " + originalFileName, e);
        }
    }

    /**
     * Xóa file
     */
    public boolean deleteFile(String fileName, String subFolder) {
        try {
            Path filePath = this.uploadPath.resolve(subFolder).resolve(fileName);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
