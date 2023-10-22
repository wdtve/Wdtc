package org.wdt.wdtc.utils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.wdt.utils.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JSONUtils extends JSON {
    public static JSONObject readJsonFiletoJSONObject(File file) throws IOException {
        return new JSONObject(FileUtils.readFileToString(file));
    }

    public static JSONObject readJsonFiletoJSONObject(String filepath) throws IOException {
        return readJsonFiletoJSONObject(FileUtils.toFile(filepath));
    }

    public static JsonObject readJsonFiletoJsonObject(File JsonFile) throws IOException {
        return parseJsonObject(FileUtils.readFileToString(JsonFile));
    }

    public static JsonObject readJsonFiletoJsonObject(String JsonFilePath) throws IOException {
        return readJsonFiletoJsonObject(FileUtils.toFile(JsonFilePath));
    }

    public static <T> T readJsonFileToClass(File JsonFile, Class<T> clazz) throws IOException {
        return parseObject(FileUtils.readFileToString(JsonFile), clazz);
    }

    public static <T> T readJsonFileToClass(String JsonFilePath, Class<T> clazz) throws IOException {
        return readJsonFileToClass(FileUtils.toFile(JsonFilePath), clazz);
    }

    @SneakyThrows(IOException.class)
    public static void writeObjectToJsonFile(File JsonFile, Object object) {
        FileUtils.writeStringToFile(JsonFile, FILE_GSON.toJson(object));
    }

    public static void writeObjectToJsonFile(String JsonFilePath, Object object) {
        writeObjectToJsonFile(FileUtils.toFile(JsonFilePath), object);
    }

    public static JsonArray readJsonFiletoJsonArray(File JsonFile) throws IOException {
        return parseJsonArray(FileUtils.readFileToString(JsonFile));
    }

    public static JsonArray readJsonFiletoJsonArray(String JsonFilePath) throws IOException {
        return readJsonFiletoJsonArray(FileUtils.toFile(JsonFilePath));
    }

    public static JSONArray readJsonFiletoJSONArray(File JsonFile) throws IOException {
        return new JSONArray(readJsonFiletoJsonArray(JsonFile));
    }

    public static JSONArray getJSONArray(String JsonFilePath) throws IOException {
        return new JSONArray(readJsonFiletoJsonArray(JsonFilePath));
    }
}
