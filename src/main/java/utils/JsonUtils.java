package utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();


    public static <T> List<T> readListFromResource(String resource, TypeReference<List<T>> type) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            if (in == null) throw new IllegalStateException("Resource not found: " + resource);
            return MAPPER.readValue(in, type);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse: " + resource, e);
        }
    }
}