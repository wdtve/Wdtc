package org.wdt.platform.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.wdt.utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class JSONUtils extends JSON {
    public static JSONObject getJSONObject(File file) throws IOException {
        return new JSONObject(FileUtils.readFileToString(file));
    }

    public static JSONObject getJSONObject(String filepath) throws IOException {
        return getJSONObject(new File(filepath));
    }

    public static JsonObject getJsonObject(File JsonFile) throws IOException {
        return parseJsonObject(FileUtils.readFileToString(JsonFile));
    }

    public static JsonObject getJsonObject(String JsonFilePath) throws IOException {
        return getJsonObject(new File(JsonFilePath));
    }

    public static <T> T JsonFileToClass(File JsonFile, Class<T> clazz) throws IOException {
        return JSONObject.parseObject(FileUtils.readFileToString(JsonFile), clazz);
    }

    public static <T> T JsonFileToClass(String JsonFilePath, Class<T> clazz) throws IOException {
        return JsonFileToClass(new File(JsonFilePath), clazz);
    }

    public static void ObjectToJsonFile(File JsonFile, Object object) {
        try {
            FileUtils.writeStringToFile(JsonFile, FILE_GSON.toJson(object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ObjectToJsonFile(String JsonFilePath, Object object) {
        ObjectToJsonFile(new File(JsonFilePath), object);
    }

    public static JsonArray getJsonArray(File JsonFile) throws IOException {
        return parseJsonArray(FileUtils.readFileToString(JsonFile));
    }

    public static JsonArray getJsonArray(String JsonFilePath) throws IOException {
        return getJsonArray(new File(JsonFilePath));
    }

    public static JSONArray getJSONArray(File JsonFile) throws IOException {
        return new JSONArray(getJsonArray(JsonFile));
    }

    public static JSONArray getJSONArray(String JsonFilePath) throws IOException {
        return new JSONArray(getJsonArray(JsonFilePath));
    }
}
