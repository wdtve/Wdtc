package org.wdt.platform.gson;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JSONUtils {
    public static JSONObject getJSONObject(File file) throws IOException {
        return new JSONObject(FileUtils.readFileToString(file, "UTF-8"));
    }

    public static JSONObject getJSONObject(String filepath) throws IOException {
        return getJSONObject(new File(filepath));
    }

    public static <T> T JsonFileToClass(File JsonFile, Class<T> clazz) {
        try {
            return JSONObject.getGson().fromJson(FileUtils.readFileToString(JsonFile, "UTF-8"), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T JsonFileToClass(String JsonFilePath, Class<T> clazz) {
        return JsonFileToClass(new File(JsonFilePath), clazz);
    }

    public static void ObjectToJsonFile(File JsonFile, Object object) {
        try {
            FileUtils.writeStringToFile(JsonFile, JSONObject.toJSONString(object), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
