package com.example.demo.service;

import com.example.demo.model.dto.FileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.example.demo.utils.java.JavaUtils.isNotBlank;
import static com.example.demo.utils.spring.SpringUtils.checkArgumentCustom;

@Service
@RequiredArgsConstructor
public class FileService {

//    @Value("${file.path}")
//    private final String filePath;

    private final ObjectMapper objectMapper;

    public void uploadFile(MultipartFile file) throws IOException {
        validateFile(file);
        Path path = Path.of(file.getOriginalFilename());
        Files.write(path, file.getBytes());
    }

    public void updateFile(FileDto fileDto) throws IOException {
        File file = getFile();

        checkArgumentCustom(file.exists(), "File not found");

        Map<String, Integer> updates = objectMapper.convertValue(fileDto, HashMap.class);
        Map<String, Integer> data = readJsonFile(file);

        for (Map.Entry<String, Integer> entry : updates.entrySet()) {
            if (entry.getValue() != null) {
                data.put(entry.getKey(), entry.getValue());
            }
        }

        writeJsonFile(file, data);
    }

    public Map<String, Integer> downloadFile() throws IOException {
        File file = getFile();

        checkArgumentCustom(file.exists(), "File not found");

        Map<String, Integer> data = readJsonFile(file);
        data.put("result", data.get("valueX") + data.get("valueY"));

        return data;
    }

    public void deleteFile() throws IOException {
        File file = getFile();

        checkArgumentCustom(file.exists(), "File not found");

        Files.delete(file.toPath());
    }

    private Map<String, Integer> readJsonFile(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        return objectMapper.readValue(bytes, HashMap.class);
    }

    private void writeJsonFile(File file, Map<String, Integer> data) throws IOException {
        objectMapper.writeValue(file, data);
    }

    private File getFile() {
        return new File("sample.json");
    }

    private void validateFile(MultipartFile file) {
        checkArgumentCustom(!file.isEmpty(), "File is empty");
        checkArgumentCustom(isNotBlank(file.getOriginalFilename()), "File name is empty");
    }
}
