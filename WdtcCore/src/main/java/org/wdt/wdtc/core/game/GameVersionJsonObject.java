package org.wdt.wdtc.core.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
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

    @Setter
    @Getter
    public static class Arguments {
        @SerializedName("game")
        private JsonArray GameList;
        @SerializedName("jvm")
        private JsonArray JvmList;


    }
}
