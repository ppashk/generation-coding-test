package com.example.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import static com.example.demo.model.enums.ErrorEnum.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FileServiceTest {

    private static final String FILE_NAME = "sample";
    private static final String ORIGINAL_FILE_NAME = "sample.json";
    private static final String WRONG_FILE_NAME = "wrong.json";
    private static final String WRONG_FILE_EXTENSION = "sample.wrong";
    private static final String CONTENT_TYPE = "application/json";
    private static final Integer VALUE_X = 1;
    private static final Integer VALUE_Y = 2;
    private static final byte[] CONTENT = ("{\"valueX\": " + VALUE_X + ", \"valueY\": " + VALUE_Y + "}").getBytes();


    @InjectMocks
    private FileService fileService;
    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void should_throw_exception_on_upload_if_file_is_empty() {
        MultipartFile multipartFile = new MockMultipartFile(FILE_NAME, ORIGINAL_FILE_NAME, CONTENT_TYPE, new byte[0]);
        ReflectionTestUtils.setField(fileService, "fileName", ORIGINAL_FILE_NAME);

        HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class, () -> fileService.uploadFile(multipartFile));
        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains(FILE_IS_EMPTY.toString()));

        verifyNoInteractions(objectMapper);
    }

    @Test
    public void should_throw_exception_on_upload_if_file_name_is_not_supported() {
        MultipartFile multipartFile = new MockMultipartFile(FILE_NAME, WRONG_FILE_NAME, CONTENT_TYPE, CONTENT);
        ReflectionTestUtils.setField(fileService, "fileName", ORIGINAL_FILE_NAME);

        HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class, () -> fileService.uploadFile(multipartFile));
        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains(FILE_NAME_NOT_SUPPORTED.toString()));

        verifyNoInteractions(objectMapper);
    }

    @Test
    public void should_throw_exception_on_upload_if_file_type_is_not_supported() {
        MultipartFile multipartFile = new MockMultipartFile(FILE_NAME, WRONG_FILE_EXTENSION, CONTENT_TYPE, CONTENT);
        ReflectionTestUtils.setField(fileService, "fileName", ORIGINAL_FILE_NAME);

        HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class, () -> fileService.uploadFile(multipartFile));
        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains(FILE_EXTENSION_NOT_SUPPORTED.toString()));

        verifyNoInteractions(objectMapper);
    }

    @Test
    public void should_throw_exception_if_file_is_not_exists() {
        ReflectionTestUtils.setField(fileService, "fileName", WRONG_FILE_NAME);

        HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class, () -> fileService.downloadFile());
        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains(FILE_NOT_FOUND.toString()));

        verifyNoInteractions(objectMapper);
    }
}