package org.wdt.platform.gson;

import com.google.gson.*;
import org.wdt.platform.gson.TypeAdapter.FileTypeAdapter;

import java.io.File;

public class JSON {
    public static Gson FILE_GSON = getBuilder().setPrettyPrinting().create();
    public static Gson GSON = getBuilder().create();
    public static GsonBuilder GSONBUILDER = getBuilder();
    public static JsonObject parseJsonObject(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    public static JsonArray parseJsonArray(String json) {
        return JsonParser.parseString(json).getAsJsonArray();
    }

    public static JSONObject parseJSONObject(String json) {
        return new JSONObject(parseJsonObject(json));
    }

    public static JSONArray parseJSONArray(String json) {
        return new JSONArray(parseJsonArray(json));
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T parseObject(JSONObject json, Class<T> clazz) {
        return parseObject(json.getJsonObjects(), clazz);
    }

    public static <T> T parseObject(JsonElement json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static GsonBuilder getBuilder() {
        return new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(File.class, new FileTypeAdapter());
    }
}
