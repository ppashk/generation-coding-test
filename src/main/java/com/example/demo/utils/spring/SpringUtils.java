package com.example.demo.utils.spring;

import lombok.experimental.UtilityClass;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@UtilityClass
public class SpringUtils {

    public static void checkArgumentCustom(boolean argument, String errorMessage) {
        if (!argument) {
            throw buildHttpBadRequestException(errorMessage);
        }
    }

    public static HttpClientErrorException buildHttpBadRequestException(String errorMessage) {
        return new HttpClientErrorException(BAD_REQUEST, errorMessage);
    }
}
