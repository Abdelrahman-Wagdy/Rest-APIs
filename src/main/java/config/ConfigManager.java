package config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConfigManager {
    private static final Properties PROPS = new Properties();


    static {
        try (InputStream in = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config/endpoints.properties")) {
            if (in == null) throw new IllegalStateException("endpoints.properties not found");
            PROPS.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load endpoints.properties", e);
        }
    }


    public static String baseUrl() { return PROPS.getProperty("baseUrl"); }
    public static String objectsPath() { return PROPS.getProperty("objectsPath"); }
}