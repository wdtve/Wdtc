package org.wdt.wdtc.core.utils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.wdt.utils.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JSONUtils extends JSON {
  public static JSONObject readFiletoJSONObject(File file) throws IOException {
    return new JSONObject(FileUtils.readFileToString(file));
  }

  public static JSONObject readFiletoJSONObject(String filepath) throws IOException {
    return readFiletoJSONObject(FileUtils.toFile(filepath));
  }

  public static JsonObject readFiletoJsonObject(File JsonFile) throws IOException {
    return parseJsonObject(FileUtils.readFileToString(JsonFile));
  }

  public static JsonObject readFiletoJsonObject(String JsonFilePath) throws IOException {
    return readFiletoJsonObject(FileUtils.toFile(JsonFilePath));
  }

  public static <T> T readFileToClass(File JsonFile, Class<T> clazz) throws IOException {
    return parseObject(FileUtils.readFileToString(JsonFile), clazz);
  }

  public static <T> T readFileToClass(String JsonFilePath, Class<T> clazz) throws IOException {
    return readFileToClass(FileUtils.toFile(JsonFilePath), clazz);
  }

  @SneakyThrows(IOException.class)
  public static void writeObjectToFile(File JsonFile, Object object) {
    FileUtils.writeStringToFile(JsonFile, FILE_GSON.toJson(object));
  }

  public static void writeObjectToFile(String JsonFilePath, Object object) {
    writeObjectToFile(FileUtils.toFile(JsonFilePath), object);
  }

  public static JsonArray readFiletoJsonArray(File JsonFile) throws IOException {
    return parseJsonArray(FileUtils.readFileToString(JsonFile));
  }

  public static JsonArray readFiletoJsonArray(String JsonFilePath) throws IOException {
    return readFiletoJsonArray(FileUtils.toFile(JsonFilePath));
  }

  public static JSONArray readFiletoJSONArray(File JsonFile) throws IOException {
    return new JSONArray(readFiletoJsonArray(JsonFile));
  }

  public static JSONArray readFiletoJSONArray(String JsonFilePath) throws IOException {
    return new JSONArray(readFiletoJsonArray(JsonFilePath));
  }
}
