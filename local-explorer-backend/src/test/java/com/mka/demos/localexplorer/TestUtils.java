package com.mka.demos.localexplorer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.net.URL;

@UtilityClass
public class TestUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public static <T> T getResponseFromJsonFile(String filePath, Class<T> clazz) {
        try {
            File file = getResourceFile(filePath);
            if (file != null) {
                return OBJECT_MAPPER.readerFor(clazz).readValue(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private File getResourceFile(String filePath) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL res = loader.getResource(filePath);
            if (res == null) {
                return null;
            }
            return new File(res.getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
