package org.wdt.platform.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSON {
    public static JsonObject parseGsonObject(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    public static JsonArray parseGsonArray(String json) {
        return JsonParser.parseString(json).getAsJsonArray();
    }

    public static JSONObject parseWdtObject(String json) {
        return new JSONObject(parseGsonObject(json));
    }

    public static JSONArray parseWdtArray(String json) {
        return new JSONArray(parseGsonArray(json));
    }
}
