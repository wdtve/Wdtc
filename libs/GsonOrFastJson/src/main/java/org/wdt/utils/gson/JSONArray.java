package org.wdt.utils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class JSONArray extends JSON implements Iterable<JsonElement> {
    private final JsonArray JsonArrays;

    public JSONArray(JsonArray JsonArrays) {
        this.JsonArrays = JsonArrays;
    }

    public JSONArray(String json) {
        this.JsonArrays = parseJsonArray(json);
    }

    public JSONObject getJSONObject(int index) {
        return new JSONObject(JsonArrays.get(index).getAsJsonObject());
    }

    public String getString(int index) {
        return JsonArrays.get(index).getAsString();
    }

    public int getInt(int index) {
        return JsonArrays.get(index).getAsInt();
    }

    public int size() {
        return JsonArrays.size();
    }

    public com.alibaba.fastjson2.JSONArray getFastJSONArray() {
        return com.alibaba.fastjson2.JSONArray.parseArray(JsonArrays.toString());
    }

    public void remove(int index) {
        JsonArrays.remove(index);
    }

    public JsonArray getJsonArrays() {
        return JsonArrays;
    }

    public void add(String str) {
        JsonArrays.add(str);
    }

    public void addAll(JSONArray jsonArray) {
        JsonArrays.addAll(jsonArray.getJsonArrays());
    }

    @NotNull
    @Override
    public Iterator<JsonElement> iterator() {
        return JsonArrays.iterator();
    }
}
