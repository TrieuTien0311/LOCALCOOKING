package com.android.be.controller;

import com.android.be.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService fileStorageService;

    /**
     * Upload một file
     * POST /api/upload/image?folder=courses
     */
    @PostMapping("/image")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "reviews") String folder) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String fileName = fileStorageService.storeFile(file, folder);
            String fileUrl = "/uploads/" + folder + "/" + fileName;
            
            response.put("success", true);
            response.put("fileName", fileName);
            response.put("fileUrl", fileUrl);
            response.put("message", "Upload thành công");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Upload thất bại: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Upload nhiều file
     * POST /api/upload/images
     */
    @PostMapping("/images")
    public ResponseEntity<Map<String, Object>> uploadMultipleImages(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> response = new HashMap<>();
        List<String> fileUrls = new ArrayList<>();
        
        try {
            for (MultipartFile file : files) {
                String fileName = fileStorageService.storeFile(file, "reviews");
                fileUrls.add("/uploads/reviews/" + fileName);
            }
            
            response.put("success", true);
            response.put("fileUrls", fileUrls);
            response.put("count", fileUrls.size());
            response.put("message", "Upload " + fileUrls.size() + " file thành công");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Upload thất bại: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
