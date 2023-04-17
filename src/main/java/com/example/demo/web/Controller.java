package com.example.demo.web;

import com.example.demo.model.dto.FileContentDto;
import com.example.demo.model.dto.FileDto;
import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {

    private final FileService fileService;

    @PostMapping(value = "/file", produces = "application/json")
    public FileContentDto uploadFile(@RequestBody MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
    }

    @PutMapping(value = "/file", consumes = "application/json", produces = "application/json")
    public FileContentDto updateFile(@RequestBody FileContentDto fileContentDto) throws IOException {
        return fileService.updateFile(fileContentDto);
    }

    @GetMapping(value = "/file", produces = "application/json")
    public FileDto downloadFile() throws IOException {
        return fileService.downloadFile();
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> deleteFile() throws IOException {
        fileService.deleteFile();
        return ResponseEntity.ok("File deleted successfully");
    }
}
