package org.wdt.platform.gson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.hildan.fxgson.FxGsonBuilder;

public class JSONObject extends JSON {
    private final JsonObject JsonObjects;

    public JSONObject(JsonObject JsonObjects) {
        this.JsonObjects = JsonObjects;
    }

    public JSONObject(String json) {
        this.JsonObjects = parseGsonObject(json);
    }

    public static String toJSONString(Object o) {
        return getGson().toJson(o);
    }


    public static JSONObject parseObject(String json) {
        return parseWdtObject(json);
    }

    public String getString(String str) {
        return JsonObjects.get(str).getAsString();
    }

    public JSONObject getJSONObject(String str) {
        return new JSONObject(JsonObjects.getAsJsonObject(str));
    }

    public boolean getBoolean(String str) {
        return JsonObjects.get(str).getAsBoolean();
    }

    public int getInt(String str) {
        return JsonObjects.get(str).getAsInt();
    }

    public JSONArray getJSONArray(String str) {
        return new JSONArray(JsonObjects.getAsJsonArray(str));
    }

    public JsonObject getJsonObjects() {
        return JsonObjects;
    }

    @Override
    public String toString() {
        return JsonObjects.toString();
    }

    public boolean has(String str) {
        return JsonObjects.has(str);
    }

    public com.alibaba.fastjson2.JSONObject getFastJSONObject() {
        return com.alibaba.fastjson2.JSONObject.parseObject(JsonObjects.toString());
    }

    public double getDouble(String str) {
        return JsonObjects.get(str).getAsDouble();
    }

    public static Gson getGson() {
        return new FxGsonBuilder().builder().setPrettyPrinting().create();
    }

}
