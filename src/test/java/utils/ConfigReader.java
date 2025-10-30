package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    static {
        try (FileInputStream input = new FileInputStream("src/test/resources/config/config.properties")) {
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load config.properties file: " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("❌ Missing key in config.properties: " + key);
        }
        return value.trim();
    }
}
