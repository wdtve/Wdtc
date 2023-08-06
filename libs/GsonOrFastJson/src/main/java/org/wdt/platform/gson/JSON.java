package org.wdt.platform.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSON {
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
}
