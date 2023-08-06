package org.wdt.wdtc.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GameVersionJsonObject {
    @SerializedName("arguments")
    private Arguments arguments;
    @SerializedName("assetIndex")
    private JsonObject assetIndex;
    @SerializedName("assets")
    private String assets;
    @SerializedName("complianceLevel")
    private int complianceLevel;
    @SerializedName("downloads")
    private JsonObject downloads;
    @SerializedName("id")
    private String id;
    @SerializedName("javaVersion")
    private JsonObject javaVersion;
    @SerializedName("libraries")
    private List<LibraryObject> libraries;
    @SerializedName("logging")
    private JsonObject logging;
    @SerializedName("mainClass")
    private String mainClass;
    @SerializedName("patches")
    private List<JsonObject> JsonObject;
    @SerializedName("minimumLauncherVersion")
    private int minimumLauncherVersion;
    @SerializedName("releaseTime")
    private String releaseTime;
    @SerializedName("time")
    private String time;
    @SerializedName("type")
    private String type;

    public void setJsonObject(List<JsonObject> jsonObject) {
        JsonObject = jsonObject;
    }

    public Arguments getArguments() {
        return arguments;
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public JsonObject getAssetIndex() {
        return assetIndex;
    }

    public String getAssets() {
        return assets;
    }

    public int getComplianceLevel() {
        return complianceLevel;
    }

    public JsonObject getDownloads() {
        return downloads;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JsonObject getJavaVersion() {
        return javaVersion;
    }

    public List<LibraryObject> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<LibraryObject> libraries) {
        this.libraries = libraries;
    }

    public JsonObject getLogging() {
        return logging;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public int getMinimumLauncherVersion() {
        return minimumLauncherVersion;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public static class Arguments {
        @SerializedName("game")
        private JsonArray GameList;
        @SerializedName("jvm")
        private JsonArray JvmList;

        public JsonArray getGameList() {
            return GameList;
        }

        public JsonArray getJvmList() {
            return JvmList;
        }

        public void setJvmList(JsonArray jvmList) {
            JvmList = jvmList;
        }
    }
}
