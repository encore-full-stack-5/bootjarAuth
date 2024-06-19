package com.example.bootjarAuth.controller;

import com.example.bootjarAuth.service.GcsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/gcs")
@RequiredArgsConstructor
public class GcsController {
    private final GcsService gcsService;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile file) {
        try {
            gcsService.uploadFile(file.getOriginalFilename(), file.getBytes());
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException  e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }
}