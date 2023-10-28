package org.wdt.wdtc.core.utils.gson;

import com.google.gson.*;
import org.wdt.wdtc.core.utils.gson.typeadapter.FileTypeAdapter;

import java.io.File;

public class JSON {
    public static final Gson FILE_GSON = getBuilder().setPrettyPrinting().create();
    public static final Gson GSON = getBuilder().create();
    public static final GsonBuilder GSONBUILDER = getBuilder();
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
