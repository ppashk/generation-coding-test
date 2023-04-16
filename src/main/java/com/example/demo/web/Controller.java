package com.example.demo.web;

import com.example.demo.model.dto.FileDto;
import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {

    private final FileService fileService;

    @PostMapping(value = "/file")
    public ResponseEntity<String> uploadFile(@RequestBody MultipartFile file) throws IOException {
        fileService.uploadFile(file);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @PutMapping(value = "/file", consumes = "application/json")
    public ResponseEntity<String> updateFile(@RequestBody FileDto fileDto) throws IOException {
        fileService.updateFile(fileDto);
        return ResponseEntity.ok("File updated successfully");
    }

    @GetMapping("/file")
    public ResponseEntity<Map<String, Integer>> downloadFile() throws IOException {
        Map<String, Integer> data = fileService.downloadFile();
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> deleteFile() throws IOException {
        fileService.deleteFile();
        return ResponseEntity.ok("File deleted successfully");
    }
}
