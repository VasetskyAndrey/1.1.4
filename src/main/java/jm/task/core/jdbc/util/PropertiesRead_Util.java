package jm.task.core.jdbc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public final class PropertiesRead_Util {

    private static final Properties PROPERTIES = new Properties();
    private static final String PATH = "src/main/resources/application.properties";

    static {
        loadProperties();
    }

    private PropertiesRead_Util() {
    }

    private static void loadProperties() {

        //        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {

        try (var inputStream = Files.newInputStream(Paths.get(PATH))) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            System.out.println("Не удалось загрузить файл свойств");
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}