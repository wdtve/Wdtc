package org.wdt.wdtc.core.utils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@Getter
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

  public void remove(int index) {
    JsonArrays.remove(index);
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
