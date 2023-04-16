package com.example.demo.utils.exception;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;
import java.util.concurrent.Callable;

import static com.example.demo.utils.java.JavaUtils.isBlank;

@UtilityClass
public class ExceptionUtil {

    @SneakyThrows
    public static <T> T propagateCatchableException(Callable<T> callable) {
        return callable.call();
    }

    public static String getErrorMessageFromClientException(RestClientResponseException e) {
        if (isBlank(e.getResponseBodyAsString())) {
            return e.getStatusText();
        } else {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            JsonNode root = propagateCatchableException(() -> mapper.readTree(e.getResponseBodyAsString()));
            return Optional.ofNullable(root)
                    .map(r -> r.findValue("message"))
                    .map(JsonNode::asText)
                    .orElse("");
        }
    }
}
