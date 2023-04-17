package com.example.demo.utils.spring;

import lombok.experimental.UtilityClass;
import org.springframework.web.client.HttpClientErrorException;

import static com.example.demo.utils.java.JavaUtils.getOrNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@UtilityClass
public class SpringUtils {

    public static void checkArgumentCustom(boolean argument, Enum<?> errorMessage) {
        if (!argument) {
            throw buildHttpBadRequestException(errorMessage);
        }
    }

    public static HttpClientErrorException buildHttpBadRequestException(Enum<?> errorMessage) {
        return new HttpClientErrorException(BAD_REQUEST, getOrNull(errorMessage, Enum::name));
    }
}
