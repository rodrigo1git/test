package com.prototype.socialNetwork.controller;

import com.prototype.socialNetwork.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        // 1. Llamar al servicio
        String imageUrl = minioService.uploadImage(file);

        // 2. Retornar la URL en un JSON
        return ResponseEntity.ok(Map.of("url", imageUrl));
    }
}