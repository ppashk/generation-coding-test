package com.example.demo.service;

import com.example.demo.model.dto.FileContentDto;
import com.example.demo.model.dto.FileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static com.example.demo.model.enums.ErrorEnum.*;
import static com.example.demo.utils.java.JavaUtils.getFileExtension;
import static com.example.demo.utils.java.JavaUtils.getFileNameWithoutExtension;
import static com.example.demo.utils.spring.SpringUtils.checkArgumentCustom;
import static java.nio.file.Files.readAllBytes;


@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.name}")
    private String fileName;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public FileContentDto uploadFile(MultipartFile multipartFile) {
        validateFile(multipartFile);

        File file = getFile();
        checkArgumentCustom(!file.exists(), FILE_ALREADY_EXISTS);

        Map<String, Integer> data = objectMapper.readValue(multipartFile.getInputStream(), HashMap.class);
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            checkArgumentCustom(entry.getKey().equals("valueX") || entry.getKey().equals("valueY"), WRONG_DATA_FORMAT);
        }

        writeJsonFile(file, data);

        return objectMapper.convertValue(data, FileContentDto.class);
    }

    public FileContentDto updateFile(FileContentDto fileContentDto) {
        File file = getFile();

        checkArgumentCustom(file.exists(), FILE_NOT_FOUND);

        Map<String, Integer> updates = objectMapper.convertValue(fileContentDto, HashMap.class);
        Map<String, Integer> data = readJsonFile(file);

        for (Map.Entry<String, Integer> entry : updates.entrySet()) {
            checkArgumentCustom(data.containsKey(entry.getKey()), WRONG_DATA_FORMAT);
            if (entry.getValue() != null) {
                data.put(entry.getKey(), entry.getValue());
            }
        }

        writeJsonFile(file, data);

        return objectMapper.convertValue(data, FileContentDto.class);
    }

    @SneakyThrows
    public FileDto downloadFile() {
        File file = getFile();

        checkArgumentCustom(file.exists(), FILE_NOT_FOUND);

        Map<String, Integer> data = readJsonFile(file);
        data.put("result", data.get("valueX") + data.get("valueY"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, data);

        return FileDto.builder()
                .name(file.getName())
                .content(outputStream.toByteArray())
                .build();
    }

    @SneakyThrows
    public void deleteFile() {
        File file = getFile();

        checkArgumentCustom(file.exists(), FILE_NOT_FOUND);

        Files.delete(file.toPath());
    }

    @SneakyThrows
    private Map<String, Integer> readJsonFile(File file) {
        byte[] bytes = readAllBytes(file.toPath());
        return objectMapper.readValue(bytes, HashMap.class);
    }

    @SneakyThrows
    private void writeJsonFile(File file, Map<String, Integer> data) {
        objectMapper.writeValue(file, data);
    }

    private File getFile() {
        return new File(fileName);
    }

    private void validateFile(MultipartFile file) {
        checkArgumentCustom(!file.isEmpty(), FILE_IS_EMPTY);
        checkArgumentCustom(getFileNameWithoutExtension(file.getOriginalFilename()).equals(getFileNameWithoutExtension(fileName)), FILE_NAME_NOT_SUPPORTED);
        checkArgumentCustom(getFileExtension(file.getOriginalFilename()).equals(getFileExtension(fileName)), FILE_EXTENSION_NOT_SUPPORTED);
    }
}
