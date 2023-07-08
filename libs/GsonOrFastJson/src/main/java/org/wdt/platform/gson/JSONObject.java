package org.wdt.platform.gson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JSONObject extends JSON {
    private final JsonObject JsonObjects;

    public JSONObject(JsonObject JsonObjects) {
        this.JsonObjects = JsonObjects;
    }

    public JSONObject(String json) {
        this.JsonObjects = parseGsonObject(json);
    }

    public static String toJSONString(Object o) {
        return new Gson().toJson(o);
    }

    public static void PutKeyToFile(File file, JSONObject jsonObject, String str, String put) throws IOException {
        FileUtils.writeStringToFile(file, jsonObject.put(str, put).toString(), "UTF-8");
    }

    public static void PutKeyToFile(String s, JSONObject jsonObject, String str, String put) throws IOException {
        PutKeyToFile(new File(s), jsonObject, str, put);
    }

    public static void PutKeyToFile(File file, JSONObject jsonObject, String str, boolean put) throws IOException {
        FileUtils.writeStringToFile(file, jsonObject.put(str, put).toString(), "UTF-8");
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

    public JsonObject put(String str, String o) {
        JsonObjects.addProperty(str, o);
        return JsonObjects;
    }

    public JsonObject put(String str, Boolean o) {
        JsonObjects.addProperty(str, o);
        return JsonObjects;
    }

    public JsonObject put(String str, Integer o) {
        JsonObjects.addProperty(str, o);
        return JsonObjects;
    }

    public JsonObject put(String str, Number o) {
        JsonObjects.addProperty(str, o);
        return JsonObjects;
    }

    public void put(String str, JSONObject o) {
        JsonObjects.add(str, o.getJsonObjects());
    }

    public void put(String str, JsonObject o) {
        JsonObjects.add(str, o);
    }

    public boolean has(String str) {
        return JsonObjects.has(str);
    }

    public com.alibaba.fastjson2.JSONObject getFastJSONObject() {
        return com.alibaba.fastjson2.JSONObject.parseObject(JsonObjects.toString());
    }

}
