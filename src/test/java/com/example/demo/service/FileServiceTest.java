package com.example.demo.service;

import com.example.demo.model.dto.FileContentDto;
import com.example.demo.model.dto.FileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.demo.model.enums.ErrorEnum.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private File file;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        file = ResourceUtils.getFile(ORIGINAL_FILE_NAME);
    }

    @Test
    public void should_upload_file_if_everything_is_ok() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile(FILE_NAME, ORIGINAL_FILE_NAME, CONTENT_TYPE, CONTENT);
        ReflectionTestUtils.setField(fileService, "fileName", ORIGINAL_FILE_NAME);
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(false);
        when(objectMapper.readValue(multipartFile.getInputStream(), HashMap.class))
                .thenReturn(getData());
        when(objectMapper.convertValue(getData(), FileContentDto.class)).thenReturn(getFileContentDto());

        FileContentDto fileContentDto = fileService.uploadFile(multipartFile);

        verify(objectMapper, times(1)).writeValue((File) any(), any());
        verifyNoMoreInteractions(objectMapper);
    }

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
    public void should_update_file_if_everything_is_ok() {

    }

    @Test
    public void should_download_file_if_everything_is_ok() throws IOException {
        Map<String, Integer> fileData = new HashMap<>();
        fileData.put("valueX", VALUE_X);
        fileData.put("valueY", VALUE_Y);
        fileData.put("result", VALUE_X + VALUE_Y);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, fileData);
        byte[] expectedContent = outputStream.toByteArray();

        when(yourClass.getFile()).thenReturn(sampleJsonFile);
        when(yourClass.readJsonFile(sampleJsonFile)).thenReturn(expectedData);

        // Call the downloadFile method
        FileDto fileDto = fileService.downloadFile();

        assertNotNull(fileDto);
        assertEquals(file.getName(), fileDto.getName());
        assertArrayEquals(expectedContent, fileDto.getContent());
    }

    @Test
    public void should_throw_exception_if_file_is_not_exists() {
        MultipartFile multipartFile = new MockMultipartFile(FILE_NAME, ORIGINAL_FILE_NAME, CONTENT_TYPE, CONTENT);
        ReflectionTestUtils.setField(fileService, "fileName", ORIGINAL_FILE_NAME);
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(false);

        HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class, () -> fileService.uploadFile(multipartFile));
        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains(FILE_NAME_NOT_SUPPORTED.toString()));

        verifyNoInteractions(objectMapper);
    }

    private HashMap<String, Integer> getData() {
        return new HashMap<>(
                Map.of("valueX", VALUE_X, "valueY", VALUE_Y)
        );
    }

    private FileContentDto getFileContentDto() {
        return new FileContentDto(VALUE_X, VALUE_Y);
    }
}