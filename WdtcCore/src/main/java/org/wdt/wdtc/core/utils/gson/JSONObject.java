package org.wdt.wdtc.core.utils.gson;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class JSONObject extends JSON {
  private final JsonObject jsonObjects;

  public JSONObject(@NonNull JsonObject JsonObjects) {
    this.jsonObjects = JsonObjects;
  }

  public JSONObject(String json) {
    this.jsonObjects = parseJsonObject(json);
  }

  public static String toJSONString(Object o) {
    return GSON.toJson(o);
  }

  public static String toJSONString(JSONObject object) {
    return toJSONString(object.getJsonObjects());
  }

  public static JSONObject parseObject(String json) {
    return parseJSONObject(json);
  }

  public String getString(String str) {
    return jsonObjects.get(str).getAsString();
  }

  public JSONObject getJSONObject(String str) {
    return new JSONObject(jsonObjects.getAsJsonObject(str));
  }

  public boolean getBoolean(String str) {
    return jsonObjects.get(str).getAsBoolean();
  }

  public int getInt(String str) {
    return jsonObjects.get(str).getAsInt();
  }

  public JSONArray getJSONArray(String str) {
    return new JSONArray(jsonObjects.getAsJsonArray(str));
  }

  @Override
  public String toString() {
    return jsonObjects.toString();
  }

  public boolean has(String str) {
    return jsonObjects.has(str);
  }


  public double getDouble(String str) {
    return jsonObjects.get(str).getAsDouble();
  }


}
